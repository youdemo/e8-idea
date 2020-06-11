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
 * @version 创建时间：2019-6-15 下午2:09:50
 * 收料单（外购）
 */
public class ReceivingOutsourcing {
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
		String workflowid = "24";//流程id------------------
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
//					jsar.put("HEADER", head);
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
			if(!head.isNull("JCRQ")){
				String JCRQ = head.getString("JCRQ");
				if(JCRQ.length()>10){
					JCRQ = JCRQ.substring(0, 10);
				}
				head.put("JCRQ", JCRQ);
//				jsar.put("HEADER", head);				
			}
			if(!head.isNull("QGDW")){
				String QGDW = head.getString("QGDW");
				String QGDWNAME = gu.getK3FieldVal("A00_V_Department", "FName", "FNUMBER", QGDW);
				if(QGDWNAME.length()>0){
					head.put("QGDW", QGDWNAME);
				}else{
					JSONObject jsn1 = null;
					try {
						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("QGDW")+"请购单位不存在A00_V_Department中\",\"requestid\":\"0\"}");
					} catch (JSONException e) {
						log.writeLog("异常2----"+e.getMessage());
					}
					return jsn1.toString();
				}
			}
			if(!head.isNull("CURRENCY")){
				String CURRENCY = head.getString("CURRENCY");
				String CURRENCYNAME = gu.getK3FieldVal("A00_V_Currency", "FName", "FCurrencyID", CURRENCY);
				if(CURRENCYNAME.length()>0){
					head.put("CURRENCY", CURRENCYNAME);
				}else{
					JSONObject jsn1 = null;
					try {
						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("CURRENCY")+"币别不存在A00_V_Currency中\",\"requestid\":\"0\"}");
					} catch (JSONException e) {
						log.writeLog("异常2----"+e.getMessage());
					}
					return jsn1.toString();
				}
			}
			if(!head.isNull("qgdjbr")){
				String qgdjbr = head.getString("qgdjbr");
				String str = "select id,departmentid from hrmresource where workcode = '"+qgdjbr+"'" ;
				rs.executeSql(str);
				if(rs.next()){
					qgdjbr = Util.null2String(rs.getString("id"));
					head.put("qgdjbr", qgdjbr);
//					jsar.put("HEADER", head);
				}else{
					JSONObject jsn1 = null;
					try {
						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("qgdjbr")+"经办人不存在OA中\",\"requestid\":\"0\"}");
					} catch (JSONException e) {
						e.printStackTrace();
						log.writeLog("异常2----"+e.getMessage());
					}
					return jsn1.toString();
				}
			}
			
			//FJiangbanID
//			if(!head.isNull("qgdjbr")){//20190923
//				String qgdjbr = head.getString("qgdjbr");
//				String gh = gu.getK3FieldVal("A00_V_Emp", "FNUMBER", "FUserId", qgdjbr);
//				String jbr = gu.getFieldVal("hrmresource", "id", "workcode", gh);
//				if(jbr.length()>0) {
//					head.put("qgdjbr", jbr);
//				}else {
//					JSONObject jsn1 = null;
//					try {
//						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("qgdjbr")+"经办人不存在A00_V_Emp中\",\"requestid\":\"0\"}");
//					} catch (JSONException e) {
//						log.writeLog("异常2----"+e.getMessage());
//					}
//					return jsn1.toString();
//				}
//			}
			jsar.put("HEADER", head);
			
		} catch (JSONException e) {
			log.writeLog("异常1----"+e.getMessage());
			e.printStackTrace();
		}
		//明细 
		try {
			JSONObject detail = null;
			JSONObject details = jsar.getJSONObject("DETAILS");
			JSONArray arr = null;
			JSONArray arrnew = null;
			if(!details.isNull("DT1")){
				try {
					arr = details.getJSONArray("DT1");
					arrnew = new JSONArray();
				} catch (Exception e) {
					log.writeLog("解析明细错误---"+ jsar.toString());
				}
				for (int m = 0; m < arr.length(); m++) {
					JSONObject dt = arr.getJSONObject(m);
					if(!dt.isNull("DJ")){
						String DJ = dt.getString("DJ");
						dt.put("DJ", gu.getFour(DJ));
					}
					if(!dt.isNull("JE")){
						String JE = dt.getString("JE");
						dt.put("JE", gu.getFour(JE));
					}
					  
					if(!dt.isNull("JEUSD")){
						String JEUSD = dt.getString("JEUSD");
						dt.put("JEUSD", gu.getFour(JEUSD));
					}
					if(!dt.isNull("JERMB")){
						String JERMB = dt.getString("JERMB");
						dt.put("JERMB", gu.getFour(JERMB));
					}
					arrnew.put(dt);	
				}	
				detail = details.put("DT1", arrnew);	
				jsar.put("DETAILS", detail);
			}
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	///	
		
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
