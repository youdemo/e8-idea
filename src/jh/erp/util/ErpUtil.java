package jh.erp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class ErpUtil {
	/**
	 * 获取中间表字段配置
	 * @param wfid
	 * @return
	 */
	public Map<String, String> getzjbpzMap(String wfid) {
		Map<String, String> map = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		String sql = "select * from uf_wfid_erpwork where wfid='"+wfid+"'";
		rs.execute(sql);
		if(rs.next()) {
			map.put("erptb", Util.null2String(rs.getString("erptb")));
			map.put("erpent", Util.null2String(rs.getString("erpent")));
			map.put("erpsite", Util.null2String(rs.getString("erpsite")));
			map.put("erpdjh", Util.null2String(rs.getString("erpdjh")));
			map.put("erpsjc", Util.null2String(rs.getString("erpsjc")));
			map.put("erpworkcode", Util.null2String(rs.getString("erpworkcode")));
			map.put("ms", Util.null2String(rs.getString("ms")));
			map.put("oaent", Util.null2String(rs.getString("oaent")));
			map.put("oasite", Util.null2String(rs.getString("oasite")));
			map.put("oadjh", Util.null2String(rs.getString("oadjh")));
			map.put("oafjdz", Util.null2String(rs.getString("oafjdz")));
			map.put("oafj", Util.null2String(rs.getString("oafj")));
		}
		return map;
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
	public String getSendToErpParam(String ent,String site,String djh,String erpcode,String operatorCode,String status,String opinion) {
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
				" <Field name=\"confirmdate\" value=\""+nowTime+"\"/> \r\n" +
				"    </Record>\r\n" + 
				"    </Parameter>\r\n" + 
				"     </RequestContent>\r\n" + 
				"    </Request>]]> </param> \r\n" + 
				"  </payload> \r\n" + 
				"</request>\r\n";
		return param;
	}

	public String getOptionString(String requestid){
		StringBuffer result = new StringBuffer();
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String nodename="";
		String lastname="";
		String time="";
		String remark="";
		if("".equals(requestid)){
			return "";
		}
		String sql_remark = "";
		sql_remark="select a.requestid,d.nodename,  a.operator,b.lastname,b.workcode,c.jobtitlename,a.operatedate+' '+a.operatetime as time, a.logtype, remark "+
					"from workflow_requestlog a,hrmresource b,hrmjobtitles c,workflow_nodebase d where a.operator=b.id and b.jobtitle=c.id "+
					" and a.nodeid=d.id and a.requestid in("+requestid+") and a.logtype not in ('e') "+
					"  order by a.operatedate desc ,a.operatetime desc";
		log.writeLog("testsql:"+sql_remark);
		rs.execute(sql_remark);
		int count=1;
		while(rs.next()){
			nodename = Util.null2String(rs.getString("nodename"));
			lastname = Util.null2String(rs.getString("lastname"));
			time = Util.null2String(rs.getString("time"));
			remark = removeHtmlTag(Util.null2String(rs.getString("remark"))).replaceAll("<[^<>]*/>","");
			if(count>1){
				result.append("/n");
			}
			result.append(nodename+":"+lastname+","+remark+","+time);
			count++;
		}
		result.append("/n");
		//log.writeLog("test:"+array.toString());
		return result.toString();

	}
	/**
	 * 签字意见html格式化
	 * @param content 签字意见remark字段文本
	 * @return
	 */
	public String removeHtmlTag(String content) {
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
		Matcher m = p.matcher(content);
		if (m.find()) {
			content = content
					.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
			content = removeHtmlTag(content);
		}
		return content;
	}
}
