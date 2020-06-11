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
import jh.erp.util.TransUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * @Description: 审批退回后推送状态给erp
* @version: 
* @author: jianyong.tang
* @date: 2019年11月5日 下午5:48:18
 */
public class SendStatusToErpForTH implements Action{

	@Override
	public String execute(RequestInfo info) {
		TransUtil tu = new TransUtil();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = tu.getTableName(workflowID);
		ErpUtil eu = new ErpUtil();
		Map<String, String> mtmap = eu.getzjbpzMap(workflowID);
		String pmdtucent = "";//ENT
		String pmdtucsite = "";//site
		String pmdtuc006 = "";//单据编号
		String operator = Util.null2String(info.getRequestManager().getUserId());
		String operatorCode = tu.getWorkCode(operator);
		String sql = "select * from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			pmdtucent = Util.null2String(rs.getString(mtmap.get("oaent")));
			pmdtucsite = Util.null2String(rs.getString(mtmap.get("oasite")));
			pmdtuc006 = Util.null2String(rs.getString(mtmap.get("oadjh")));
		}
		String param = eu.getSendToErpParam(pmdtucent, pmdtucsite, pmdtuc006, mtmap.get("erpworkcode"), operatorCode, "R","");
		try {
			tu.writeLog("SendStatusToErpForTH", "param:"+param);
			String result = sendToErp(param);
			tu.writeLog("SendStatusToErpForTH", "result:"+result);
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
			tu.writeLog("SendStatusToErpForTH", e);
			info.getRequestManager().setMessagecontent("ERP接口调用错误 请联系系统管理员");        
			info.getRequestManager().setMessageid("ERROR:"); 
			return FAILURE_AND_CONTINUE;
		}
		return SUCCESS;
	}
	public String  sendToErp(String param) throws Exception {
		TIPTOPServiceGateWayStub tpg = new TIPTOPServiceGateWayStub();
		InvokeSrv is = new TIPTOPServiceGateWayStub.InvokeSrv();
		is.setRequest(param);
		InvokeSrvResponse  isr =tpg.invokeSrv(is);
		String resultxml = isr.getResponse().toString();
		
		return resultxml;
		
	}
}
