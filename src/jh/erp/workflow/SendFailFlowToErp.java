package jh.erp.workflow;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import jh.erp.service.TIPTOPServiceGateWayStub;
import jh.erp.service.TIPTOPServiceGateWayStub.InvokeSrv;
import jh.erp.service.TIPTOPServiceGateWayStub.InvokeSrvResponse;
import jh.erp.util.ErpUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.system.SysRemindWorkflow;
/**
 * @Description: 定时扫描 outerdatawfdetail将触发失败的流程推送给ERP
* @version: 
* @author: jianyong.tang
* @date: 2019年11月5日 下午5:49:26
 */
public class SendFailFlowToErp extends BaseCronJob{
	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("SendFailFlowToErp", "开始定时处理");
		doJob();
		log.writeLog("SendFailFlowToErp", "开始处理之前失败的数据");
		doSendErrorList();
		log.writeLog("SendFailFlowToErp", "定时处理结束");
	}
	
	public void doJob() {
		//根据中间表的wfid和keyword外部表的主键（时间戳） 每次扫描wfid在表单建模配置中的触发失败的数据
		//扫描范围 每次循环时取最大的keyword（wfid范围）并保存，下次循环处理时就处理 保存的最大值到当前最大值中间的数据
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String startId = "";
		String endId = "";
		String modeid = getModeId("uf_oaerp_dscl");
		String sql = "select keyvalue from uf_oaerp_other_mt where type='MAXID'";
		rs.execute(sql);
		if(rs.next()) {
			startId = Util.null2String(rs.getString("keyvalue"));
		}
		if("".equals(startId)) {
			startId = "0";
		}
		sql = "select max(keyfieldvalue) as endid from outerdatawfdetail where workflowid in(select wfid from uf_wfid_erpwork )";
		rs.execute(sql);
		if(rs.next()) {
			endId = Util.null2String(rs.getString("endid"));
		}
		log.writeLog("SendFailFlowToErp", "定时处理范围：startID "+startId+" endID "+endId);
		//更新最大的id
		sql = "update uf_oaerp_other_mt set keyvalue='"+endId+"' where type='MAXID'";
		rs.execute(sql);
		//循环处理失败的触发请求
		String keyfieldvalue = "";
		sql = "select mainid,keyfieldvalue,workflowid from outerdatawfdetail where workflowid in(select wfid from uf_wfid_erpwork ) and isnull(requestid,-1)<0 and keyfieldvalue>'"+startId+"' and keyfieldvalue<='"+endId+"'";
		rs.execute(sql);
		while(rs.next()) {
			keyfieldvalue = Util.null2String(rs.getString("keyfieldvalue"));
			if(!"".equals(keyfieldvalue)) {
				//处理
				dealItem(keyfieldvalue,rs.getString("mainid"),rs.getString("workflowid"),modeid);
				
			}
		}
		
		
	}
	/**
	 * 处理之前调用接口失败的数据没调用成功
	 */
	public void doSendErrorList() {
		RecordSet rs = new RecordSet();
		String keyfieldvalue = "";
		String djh = "";
		String ent = "";
		String site = "";
		String erpworkcode = "";
		String billid = "";
		String sql = "select * from uf_oaerp_dscl where sfcl='0' or (sfcl='1' and code='')";
		rs.execute(sql);
		while(rs.next()) {
			billid = Util.null2String(rs.getString("id"));
			keyfieldvalue = Util.null2String(rs.getString("keyfieldvalue"));
			djh = Util.null2String(rs.getString("djh"));
			ent = Util.null2String(rs.getString("ent"));
			site = Util.null2String(rs.getString("site"));
			erpworkcode = Util.null2String(rs.getString("erpworkcode"));
			toErp(ent,site,djh,erpworkcode,keyfieldvalue,billid);
		}
				
	}
	/**
	 * 处理错误数据 推送erp 创建提醒流程
	 * @param keyfieldvalue
	 * @param mainid
	 * @param wfid
	 * @param modeid
	 */
	public void dealItem(String keyfieldvalue,String mainid,String wfid,String modeid) {
		BaseBean log = new BaseBean();
		ErpUtil eu = new ErpUtil();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datasource = getDataSource(mainid);
		RecordSet rs = new RecordSet();
		if("".equals(datasource)) {
			return;
		}
		Map<String, String> mtMap = eu.getzjbpzMap(wfid);
		String ent = "";
		String site = "";
		String djh = "";//单据号
		RecordSetDataSource rsd = new RecordSetDataSource(datasource);
		String sql = "select * from "+mtMap.get("erptb")+" where "+mtMap.get("erpsjc")+"='"+keyfieldvalue+"'";
		rsd.execute(sql);
		if(rsd.next()) {
			ent = Util.null2String(rsd.getString(mtMap.get("erpent")));
			site = Util.null2String(rsd.getString(mtMap.get("erpsite")));
			djh = Util.null2String(rsd.getString(mtMap.get("erpdjh")));
		}
		log.writeLog("SendFailFlowToErp", "处理：keyfieldvalue "+keyfieldvalue+" ent "+ent+" site "+site+" djh "+djh);
		String erpworkcode = mtMap.get("erpworkcode");
		if(!"".equals(djh)) {
			//插入建模中间表
			sql = "insert into uf_oaerp_dscl(keyfieldvalue,djh,ent,site,sfcl,clcs,code,erpworkcode,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid) values('"+keyfieldvalue+"','"+djh+"','"+ent+"','"+site+"','0',0,'','"+erpworkcode+"','"+sdf.format(new Date())+"','1','0','"+modeid+"')";
			rs.execute(sql);
			String billid = "";
			sql = "select max(id) as billid from uf_oaerp_dscl where keyfieldvalue='"+keyfieldvalue+"'";
			rs.executeSql(sql);
			if (rs.next()) {
				billid = Util.null2String(rs.getString("billid"));
			}
			if (!"".equals(billid)) {
				ModeRightInfo ModeRightInfo = new ModeRightInfo();
				ModeRightInfo.editModeDataShare(
						Integer.valueOf("1"),
						Util.getIntValue(modeid),
						Integer.valueOf(billid));
			}
			//推送erp
			toErp(ent,site,djh,erpworkcode,keyfieldvalue,billid);
			//创建提醒流程
			String title ="";
			sql = "select  workflowname from workflow_base where id="+wfid;
			rs.execute(sql);
			if(rs.next()) {
				title = "流程（"+Util.null2String(rs.getString("workflowname"))+"）创建失败提醒";
			}
			log.writeLog("SendFailFlowToErp", "推送提醒流程");
			createRemindWorkflow(title,1,"1","单据号:"+djh+" ENT:"+ent+" SITE:"+site);
			
			
		}
	}
	/**
	 * 创建提醒流程
	 * @param title
	 * @param creater
	 * @param receiver
	 * @param bz
	 */
	public void createRemindWorkflow(String title,int creater,String receiver,String bz) {
		BaseBean log = new BaseBean();
		SysRemindWorkflow remindwf=new SysRemindWorkflow();
		 try {
			 remindwf.make(title, 0, 0, 0, 0, creater, receiver, bz,0);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				log.writeLog("推送提醒流程失败");
				log.writeLog(e);
			}
	}
	/**
	 *  推送erp 
	 * @param ent
	 * @param site
	 * @param djh
	 * @param erpworkcode
	 * @param keyfieldvalue
	 * @return
	 */
	public void toErp(String ent,String site,String djh,String erpworkcode,String keyfieldvalue,String billid){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		ErpUtil eu = new ErpUtil();
		String sql = "";
		String param = eu.getSendToErpParam(ent, site, djh, erpworkcode, "", "N","");
		try {
			log.writeLog("SendFailFlowToErp", "param:"+param);
			String result = sendToErp(param);
			log.writeLog("SendFailFlowToErp", "result:"+result);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder
					.parse(new InputSource(new StringReader(result)));
			NodeList list = doc.getElementsByTagName("param");
			String resultxml =list.item(0).getTextContent();
			doc = builder
					.parse(new InputSource(new StringReader(resultxml)));
		
			list = doc.getElementsByTagName("Status");
			String code = list.item(0).getAttributes().getNamedItem("code").getTextContent();
			String description = list.item(0).getAttributes().getNamedItem("description").getTextContent();
			sql = "update uf_oaerp_dscl set sfcl='1',clcs=clcs+1,code='"+code+"',description='"+description+"' where id="+billid;
			rs.execute(sql);
		} catch (Exception e) {
			log.writeLog("SendFailFlowToErp", e);
			sql = "update uf_oaerp_dscl set sfcl='1',clcs=clcs+1,code='',description='调用接口失败' where id="+billid;
			rs.execute(sql);
		}
	}
	public String  sendToErp(String param) throws Exception {
		TIPTOPServiceGateWayStub tpg = new TIPTOPServiceGateWayStub();
		InvokeSrv is = new TIPTOPServiceGateWayStub.InvokeSrv();
		is.setRequest(param);
		InvokeSrvResponse  isr =tpg.invokeSrv(is);
		String resultxml = isr.getResponse().toString();
		
		return resultxml;
		
	}
	
	/**
	 * 获取数据源名称
	 * @param mainid
	 * @return
	 */
	public String getDataSource(String mainid) {
		RecordSet rs = new RecordSet();
		String datasourceid = "";
		String sql = "select datasourceid from outerdatawfset where id="+mainid;
		rs.execute(sql);
		if(rs.next()) {
			datasourceid = Util.null2String(rs.getString("datasourceid"));
		}
		return datasourceid;
	}
	
	public String getModeId(String tableName){
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"+tableName+"'";
		rs.executeSql(sql);
		if(rs.next()){
			formid = Util.null2String(rs.getString("id"));
		}
		sql="select id from modeinfo where  formid="+formid;
		rs.executeSql(sql);
		if(rs.next()){
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}

}
