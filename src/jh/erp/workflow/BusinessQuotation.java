package jh.erp.workflow;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import jh.erp.service.TIPTOPServiceGateWayStub;
import jh.erp.service.TIPTOPServiceGateWayStub.InvokeSrv;
import jh.erp.service.TIPTOPServiceGateWayStub.InvokeSrvResponse;
import jh.erp.util.ErpUtil;
import jh.erp.util.GetUtil;
import jh.erp.util.TransUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2019年12月17日 下午4:49:00 
* @version 1.0  DP-SA03-业务报价单
*/
public class BusinessQuotation  implements Action{

	@Override
	public String execute(RequestInfo info) {
		TransUtil tu = new TransUtil();
		RecordSet rs = new RecordSet();
		GetUtil gu = new GetUtil();
		BaseBean log = new BaseBean();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = tu.getTableName(workflowID);
		ErpUtil eu = new ErpUtil();
		Map<String, String> mtmap = eu.getzjbpzMap(workflowID);
		String pmdtucent = "";//ENT
		String pmdtucsite = "";//site
		String sql = "select * from "+tableName+" where requestid="+requestid;
		String mid = "";//
		Map<String, String> map = new HashMap<String, String>();//主表信息
		rs.execute(sql);
		if(rs.next()) {
			pmdtucent = Util.null2String(rs.getString(mtmap.get("oaent")));
			pmdtucsite = Util.null2String(rs.getString(mtmap.get("oasite")));
			map.put("xmfdent", Util.null2String(rs.getString("xmfdent")));//企业编号
			map.put("xmfdsite", Util.null2String(rs.getString("xmfdsite")));//营运据点
			map.put("xmfddocno", Util.null2String(rs.getString("SN")));//报价单号
			map.put("xmfddocdt", gu.formatDate(Util.null2String(rs.getString("xmfddocdt"))));//报价日期
			map.put("xmfd001", Util.null2String(rs.getString("xmfd001")));//业务人员
			map.put("xmfd002", getDeptCode(Util.null2String(rs.getString("xmfd002"))));//业务部门
			map.put("xmfd003", Util.null2String(rs.getString("xmfd003")));//客户编号
			map.put("xmfd007", gu.formatDate(Util.null2String(rs.getString("xmfd007"))));//报价有效日期
			map.put("xmfd029", Util.null2String(rs.getString("beizhu")));//备注
			map.put("xmfdud002", requestid);//OA的requestID
		}
		
		//明细数据组装
		JSONArray arr = new JSONArray();
		sql = "select * from "+tableName+"_dt1 where mainid = '"+mid+"'"; 
		rs.execute(sql);
		while(rs.next()) {
			JSONObject json = new JSONObject();
			try {
				json.put("xmffseq", Util.null2String(rs.getString("id")));//项次   项次/序号  明细id
				json.put("xmff001", Util.null2String(rs.getString("xmff001")));//料件编号    料号   xmff001：主键即是编号，直接传
				json.put("xmff003", Util.null2String(rs.getString("xmff003")));//客户料号	料号	  xmff003：手填
				json.put("xmff004", Util.null2String(rs.getString("xmff004")));//销售单位	单位	 xmff004：料件编号带出
				json.put("xmff006", Util.null2String(rs.getString("xmff006")));//数量	数量	 xmff006：手填，浮点数
				json.put("xmff007", Util.null2String(rs.getString("xmff007")));//报价单价	单价	 xmff007：手填，浮点数
				json.put("xmff008", Util.null2String(rs.getString("xmff008")));//税前金额	 金额	 xmff008：手填，浮点数
				json.put("xmff009", Util.null2String(rs.getString("xmff009")));//含税金额     金额	 	xmff009：手填，浮点数
				json.put("xmff010", Util.null2String(rs.getString("xmff010")));//税额	 金额	 	xmff010：手填，浮点数
				json.put("xmff014", Util.null2String(rs.getString("xmff014")));//备注	 短备注 	xmff014：手填，文本
			} catch (JSONException e) {
				log.writeLog("jsonarr组装异常------"+e.getMessage());
				e.printStackTrace();
			}
			arr.put(json);
		}		
		String param = gu.getSendToErpParam(pmdtucent, pmdtucsite, map, arr);
		try {
			tu.writeLog("DP-SA03-业务报价单", "param:"+param);
			String result = sendToErp(param);
			tu.writeLog("DP-SA03-业务报价单", "result:"+result);
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
			if(!"0".equals(code)) {
				info.getRequestManager().setMessagecontent("ERP接口处理数据失败，错误信息："+description+" (请联系系统管理员)");        
				info.getRequestManager().setMessageid("ERROR:"); 
				return FAILURE_AND_CONTINUE;
			}
		} catch (Exception e) {
			tu.writeLog("SendStatusToErp", e);
			info.getRequestManager().setMessagecontent("ERP接口调用错误 请联系系统管理员");        
			info.getRequestManager().setMessageid("ERROR:"); 
			return FAILURE_AND_CONTINUE;
		}
		return SUCCESS;
	}
	public String  sendToErp(String param) throws Exception {
		TIPTOPServiceGateWayStub tpg = new TIPTOPServiceGateWayStub();
		InvokeSrv is = new InvokeSrv();
		is.setRequest(param);
		InvokeSrvResponse  isr =tpg.invokeSrv(is);
		String resultxml = isr.getResponse().toString();
		
		return resultxml;
		
	}
	public String getDeptCode(String dept) {
		String code = "";
		String str = "";
		if(dept.length()>1) {
			code = dept.substring(0,1);
			str = dept.substring(1,dept.length());
		}
		if("0".equals(code)) {
			return "1"+str;
		}
		return dept;
		
	}
	
}
