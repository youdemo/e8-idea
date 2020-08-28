package jh.erp.workflow;

import jh.erp.service.TIPTOPServiceGateWayStub;
import jh.erp.service.TIPTOPServiceGateWayStub.InvokeSrv;
import jh.erp.service.TIPTOPServiceGateWayStub.InvokeSrvResponse;
import jh.erp.util.ErpUtil;
import jh.erp.util.TransUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: 料号申请审批成功后推送状态给erp
* @version: 
* @author: jianyong.tang
* @date: 20200729
 */
public class SendStatusToErpForLhsq implements Action{

	@Override
	public String execute(RequestInfo info) {
		TransUtil tu = new TransUtil();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = tu.getTableName(workflowID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ErpUtil eu = new ErpUtil();
        String remark = eu.removeHtmlTag(info.getRequestManager().getRemark()).replaceAll("<[^<>]*/>","");
        String  lastname = info.getRequestManager().getUser().getLastname();
        int nodeid = info.getRequestManager().getNodeid();
		Map<String, String> mtmap = eu.getzjbpzMap(workflowID);
		String pmdtucent = "";//ENT
		String pmdtucsite = "";//site
		String pmdtuc006 = "";//单据编号
		String operator = Util.null2String(info.getRequestManager().getUserId());
		String nextNodeid =  Util.null2String(info.getRequestManager().getNextNodeid());
		String operatorCode = tu.getWorkCode(operator);
		String sql = "select * from "+tableName+" where requestid="+requestid;
		Map<String,String> bdzdmap = new HashMap();
		rs.execute(sql);
		if(rs.next()) {
			pmdtucent = Util.null2String(rs.getString(mtmap.get("oaent")));
			pmdtucsite = Util.null2String(rs.getString(mtmap.get("oasite")));
			pmdtuc006 = Util.null2String(rs.getString(mtmap.get("oadjh")));
			bdzdmap.put("imbauc068",getWorkcode(Util.null2String(rs.getString("imbauc068"))));//采购人员
			bdzdmap.put("imbauc069",Util.null2String(rs.getString("imbauc069")));//供应商
			bdzdmap.put("imbauc070",Util.null2String(rs.getString("imbauc070")));//供应商简称
			bdzdmap.put("imbauc071",Util.null2String(rs.getString("imbauc071")));//供应商全称
			bdzdmap.put("imbauc072",getWorkcode(Util.null2String(rs.getString("imbauc072"))));//计划人员
			bdzdmap.put("imbauc073",getWorkcode(Util.null2String(rs.getString("imbauc073"))));//仓管员
			bdzdmap.put("imbauc074",Util.null2String(rs.getString("imbauc074")));//默认仓位
			bdzdmap.put("imbauc075",Util.null2String(rs.getString("imbauc075")));//默认储位
			bdzdmap.put("imbauc076",Util.null2String(rs.getString("imbauc076")));//MOQ
			bdzdmap.put("imbauc077",Util.null2String(rs.getString("imbauc077")));//L/T
			bdzdmap.put("imbauc078",Util.null2String(rs.getString("imbauc078")));//工作中心
			bdzdmap.put("imbauc079",getDepartmentcode(Util.null2String(rs.getString("imbauc079"))));//成本中心 部门
		}
		String nodename = "";
		sql = "select nodename from workflow_nodebase where id="+nodeid;
		rs.execute(sql);
		if(rs.next()){
            nodename = Util.null2String(rs.getString("nodename"));
        }
		String nextNodeName = "";
		String nextNodetype = "";
		sql = "select a.nodename,b.nodetype from workflow_nodebase a,workflow_flownode b where a.id=b.nodeid and a.id="+nextNodeid;
		rs.execute(sql);
		if(rs.next()){
			nextNodeName = Util.null2String(rs.getString("nodename"));
			nextNodetype = Util.null2String(rs.getString("nodetype"));
		}
		String option = "";
		if("3".equals(nextNodetype)){
			option = nextNodeName+"/n";
		}
		option = option + nodename+":"+lastname+","+remark+","+sdf.format(new Date())+"/n"+eu.getOptionString(requestid);
		String param = getSendToErpParam(pmdtucent, pmdtucsite, pmdtuc006, mtmap.get("erpworkcode"), operatorCode, "Y",option,bdzdmap);
		try {
			tu.writeLog("SendStatusToErp", "param:"+param);
			String result = sendToErp(param);
			tu.writeLog("SendStatusToErp", "result:"+result);
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
	/**
	 * 获取推送接口参数
	 * @param ent
	 * @param site
	 * @param djh 单据号
	 * @param erpcode  erp类型号
	 * @param operatorCode 审批人工号
	 * @param status 状态 Y 审批 R 退回 N 触发失败
	 * @return
	 */
	public String getSendToErpParam(String ent,String site,String djh,String erpcode,String operatorCode,String status,String opinion,Map<String,String> bdzdmap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String nowTime = sdf.format(new Date());
		String param = "<request type=\"sync\" key=\"76CFB4D975B57C166A5F93C79E19D933\"> \r\n" +
				"  <host prod=\"OA\" ver=\"1.0\" ip=\"10.10.20.10\" lang=\"zh_CN\" timezone=\"8\" timestamp=\""+sdf1.format(new Date())+"\" acct=\"tiptop\"/>  \r\n" +
				"  <service prod=\"T100\" name=\"oa.bill.confirm\" srvver=\"1.0\" id=\"00000\"/>  \r\n" +
				"  <datakey type=\"FOM\"> \r\n" +
				"    <key name=\"EntId\">"+ent+"</key> \r\n" +
				"    <key name=\"CompanyId\">"+site+"</key> \r\n" +
				"  </datakey>  \r\n" +
				"  <payload> \r\n" +
				"    <param key=\"data\" type=\"XML\"><![CDATA[\r\n" +
				"<Request>\r\n" +
				" <RequestContent>\r\n" +
				"  <Parameter>\r\n" +
				" <Record>\r\n" +
				" <Field name=\"docno\" value=\""+djh+"\"/> \r\n" +
				" <Field name=\"prog\" value=\""+erpcode+"\"/> \r\n" +
				" <Field name=\"stus\" value=\""+status+"\"/>  \r\n" +
				" <Field name=\"opinion\" value=\""+opinion.replaceAll("<br/>"," ").replaceAll("<br>"," ").replaceAll("</br>"," ").replaceAll("<BR/>"," ").replaceAll("<BR>"," ").replaceAll("</BR>"," ").replaceAll("&NBSP;"," ").replaceAll("&nbsp;"," ")+"\"/>\r\n" +
				" <Field name=\"confirmid\" value=\""+operatorCode+"\"/> \r\n" +
				" <Field name=\"confirmdate\" value=\""+nowTime+"\"/> \r\n"+
				" <Field name=\"imbauc068\" value=\""+bdzdmap.get("imbauc068")+"\"/> \r\n"+
				" <Field name=\"imbauc069\" value=\""+bdzdmap.get("imbauc069")+"\"/> \r\n"+
				" <Field name=\"imbauc070\" value=\""+bdzdmap.get("imbauc070")+"\"/> \r\n"+
				" <Field name=\"imbauc071\" value=\""+bdzdmap.get("imbauc071")+"\"/> \r\n"+
				" <Field name=\"imbauc072\" value=\""+bdzdmap.get("imbauc072")+"\"/> \r\n"+
				" <Field name=\"imbauc073\" value=\""+bdzdmap.get("imbauc073")+"\"/> \r\n"+
				" <Field name=\"imbauc074\" value=\""+bdzdmap.get("imbauc074")+"\"/> \r\n"+
				" <Field name=\"imbauc075\" value=\""+bdzdmap.get("imbauc075")+"\"/> \r\n"+
				" <Field name=\"imbauc076\" value=\""+bdzdmap.get("imbauc076")+"\"/> \r\n"+
				" <Field name=\"imbauc077\" value=\""+bdzdmap.get("imbauc077")+"\"/> \r\n"+
				" <Field name=\"imbauc078\" value=\""+bdzdmap.get("imbauc078")+"\"/> \r\n"+
				" <Field name=\"imbauc079\" value=\""+bdzdmap.get("imbauc079")+"\"/> \r\n"+
		        "    </Record>\r\n" +
				"    </Parameter>\r\n" +
				"     </RequestContent>\r\n" +
				"    </Request>]]> </param> \r\n" +
				"  </payload> \r\n" +
				"</request>\r\n";
		return param;
	}

	/**
	 * 获取工号
	 * @param ryid
	 * @return
	 */
	public String getWorkcode(String ryid){
		String workcode = "";
		if("".equals(ryid)){
			return workcode;
		}
		RecordSet rs = new RecordSet();
		rs.execute("select workcode from hrmresource where id="+ryid);
		if(rs.next()){
			workcode = Util.null2String(rs.getString("workcode"));
		}
		return workcode;
	}

	/**
	 * 获取部门编码
	 * @param dpid
	 * @return
	 */
	public String getDepartmentcode(String dpid){
		String departmentcode = "";
		if("".equals(dpid)){
			return departmentcode;
		}
		RecordSet rs = new RecordSet();
		rs.execute("select departmentcode from hrmdepartment where id="+dpid);
		if(rs.next()){
			departmentcode = Util.null2String(rs.getString("departmentcode"));
		}
		if(!"".equals(departmentcode) && "0".equals(departmentcode.substring(0,1))){
			departmentcode = "1"+departmentcode.substring(1);
		}
		return departmentcode;
	}

	public String  sendToErp(String param) throws Exception {
		TIPTOPServiceGateWayStub tpg = new TIPTOPServiceGateWayStub();
		InvokeSrv is = new InvokeSrv();
		is.setRequest(param);
		InvokeSrvResponse  isr =tpg.invokeSrv(is);
		String resultxml = isr.getResponse().toString();
		
		return resultxml;
		
	}
	
}
