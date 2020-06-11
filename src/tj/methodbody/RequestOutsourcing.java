package tj.methodbody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tj.util.AutoRequestService;
import tj.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-15 上午11:43:24
 * 请款单（外购）
 */
public class RequestOutsourcing {
	/**
	 * 触发流程
	 * @param jsonstr
	 * @param flag
	 * @return
	 */
	public String triggerProcess(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONObject jsar = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
		String workflowid = "22";//流程id------------------
		RecordSet rs = new  RecordSet();
		GetUtil gu = new GetUtil();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONObject(jsonstr);
		} catch (JSONException e) {
			log.writeLog("jsonstr转换错误----");
			e.printStackTrace();
		}
		try {
			creatercode = jsar.getString("CREATERCODE");
			String sql = "select id from hrmresource where workcode = '"+creatercode+"'" ;
			rs.executeSql(sql);
			if(rs.next()){
				createrid =Util.null2String(rs.getString("id"));
			}else{
				JSONObject jsn1 = null;
				try {
					jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"创建人不存在OA中\",\"requestid\":\"0\"}");
				} catch (JSONException e) {
					e.printStackTrace();
					
					log.writeLog("异常2----"+e.getMessage());
				}
				return jsn1.toString();
			}
		} catch (JSONException e1) {
			log.writeLog("异常2----"+e1.getMessage());
		}
		JSONObject jsn = null;
		try {				
			JSONObject head = jsar.getJSONObject("HEADER");
			if(!head.isNull("CJR")){
				String CJR = head.getString("CJR");
				String str = "select id,departmentid from hrmresource where workcode = '"+CJR+"'" ;
				rs.executeSql(str);
				if(rs.next()){
					CJR = Util.null2String(rs.getString("id"));
					head.put("CJR", CJR);
					jsar.put("HEADER", head);
				}else{
					JSONObject jsn1 = null;
					try {
						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("CJR")+"创建人不存在OA中\",\"requestid\":\"0\"}");
					} catch (JSONException e) {
						e.printStackTrace();
						log.writeLog("异常2----"+e.getMessage());
					}
					return jsn1.toString();
				}
			}
			if(!head.isNull("JE")){
				String JE = gu.getFour(head.getString("JE"));
				head.put("JE", JE);
				jsar.put("HEADER", head);
			}
			
		} catch (JSONException e) {
			log.writeLog("异常1----"+e.getMessage());
			e.printStackTrace();
		}
		try {
			result = as.createRequest(workflowid, jsar.toString(), createrid, "0");
			jsn = new JSONObject(result);
			log.writeLog("result----"+result);
			
		} catch (JSONException e) {
			log.writeLog("异常2----"+e.getMessage());
			JSONObject jsn1 = null;
			try {
				jsn1  = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"json异常\",\"requestid\":\"0\"}");
			} catch (JSONException ee) {
				ee.printStackTrace();
				log.writeLog("异常2----"+ee.getMessage());
			}
			return jsn1.toString();
		}
		return jsn.toString();
	}
}