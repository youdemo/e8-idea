package jh.erp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2019年12月17日 下午4:50:28 
* @version 1.0 
*/
public class GetUtil {
	/**
	 * 获取推送接口参数
	 * @param ent
	 * @param site
	 * @param map  主表 数据
	 * @param jsonarr  明细数据
	 * @return  拼接的xml 报文数据
	 */
	public String getSendToErpParam(String ent,String site,Map<String,String> map,JSONArray jsonarr) {
		BaseBean log = new BaseBean();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StringBuffer sb = new StringBuffer();
		sb.append("<request type=\"sync\" key=\"76CFB4D975B57C166A5F93C79E19D933\"> \r\n");
		sb.append("  <host prod=\"OA\" ver=\"1.0\" ip=\"10.10.20.10\" lang=\"zh_CN\" timezone=\"8\" timestamp=\""+sdf1.format(new Date())+"\" acct=\"tiptop\"/>  \r\n");
		sb.append("  <service prod=\"T100\" name=\"oa.baojiadan.create\" srvver=\"1.0\" id=\"00000\"/>  \r\n");
		sb.append("  <datakey type=\"FOM\"> \r\n" );
		sb.append("    <key name=\"EntId\">"+ent+"</key> \r\n");
		sb.append("    <key name=\"CompanyId\">"+site+"</key> \r\n");
		sb.append("  </datakey>  \r\n");
		sb.append("  <payload> \r\n");
		sb.append("    <param key=\"data\" type=\"XML\"><![CDATA[\r\n");
		sb.append("<Request>\r\n");
		sb.append(" <RequestContent>\r\n");
		sb.append("    <Document> \r\n");
		sb.append("    		<RecordSet id=\"1\"> \r\n");
		sb.append("				<Master name=\"xmfd_t\" node_id=\"1\"> \r\n");
		sb.append("					<Record> \r\n");
		sb.append("					<Field name=\"xmfdent\" value=\""+map.get("xmfdent")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfdsite\" value=\""+map.get("xmfdsite")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfddocno\" value=\""+map.get("xmfddocno")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfddocdt\" value=\""+map.get("xmfddocdt")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfd001\" value=\""+map.get("xmfd001")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfd002\" value=\""+map.get("xmfd002")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfd003\" value=\""+map.get("xmfd003")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfd007\" value=\""+map.get("xmfd007")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfd029\" value=\""+map.get("xmfd029")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfdud002\" value=\""+map.get("xmfdud002")+"\"/> /r/n");
		sb.append("					<Field name=\"xmfd034\" value=\"\"/> /r/n");
		sb.append("					<Detail name=\"xmff_t\"> \r\n");
		//循环
		for(int i = 0;i<jsonarr.length();i++) {
			try {
				JSONObject json = jsonarr.getJSONObject(i);
				sb.append("						<Record> \r\n");
				sb.append("<Field name=\"xmffseq\" value=\""+json.getString("xmffseq")+"\"/> \r\n");
				sb.append("<Field name=\"xmff001\" value=\""+json.getString("xmff001")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff003\" value=\""+json.getString("xmff003")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff004\" value=\""+json.getString("xmff004")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff006\" value=\""+json.getString("xmff006")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff007\" value=\""+json.getString("xmff007")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff008\" value=\""+json.getString("xmff008")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff009\" value=\""+json.getString("xmff009")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff010\" value=\""+json.getString("xmff010")+"\"/> \r\n"); 
				sb.append("<Field name=\"xmff014\" value=\""+json.getString("xmff014")+"\"/> \r\n"); 
				sb.append("						</Record>\r\n");
			} catch (JSONException e) {
				e.printStackTrace();
				log.writeLog("jsonarr异常------"+e.getMessage());
			}
		 }
		//
		sb.append("					</Detail> \r\n");
		sb.append("				</Record> \r\n");
		sb.append("			</Master> \r\n");
		sb.append("		</RecordSet> \r\n");
		sb.append("	</Document> \r\n");
		sb.append(" </RequestContent> \r\n");
		sb.append(" </Request>]]> </param> \r\n");
		sb.append("</payload> \r\n");
		sb.append("</request> \r\n");
		return sb.toString();
	}
	
	public String formatDate(String dat) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		String result = "";
		try {
			result = sdf1.format(sdf.parse(dat));			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
}
