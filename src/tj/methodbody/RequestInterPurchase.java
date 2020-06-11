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
 * @version 创建时间：2019-6-15 上午11:44:24
 * 请款单（内购）
 */
public class RequestInterPurchase {
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
		String workflowid = "21";//流程id------------------
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
			if(!head.isNull("BM")){
				String BM = head.getString("BM");
				String str = "select bmmc from uf_bmfygx  where bmbm = '"+BM+"'" ;
				rs.executeSql(str);
				if(rs.next()){
					BM = Util.null2String(rs.getString("bmmc"));
					head.put("BM", BM);
					
				}else{
					JSONObject jsn1 = null;
					try {
						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("BM")+"部门不存在OA中\",\"requestid\":\"0\"}");
					} catch (JSONException e) {
						log.writeLog("异常2----"+e.getMessage());
					}
					return jsn1.toString();
				}
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
			
			///

			if(!head.isNull("FKFS")){
				String FKFS = head.getString("FKFS");
				if("转账".equals(FKFS)){
					FKFS = "0";
				}else if("承兑".equals(FKFS)){
					FKFS = "1";
				}else if("现金".equals(FKFS)){
					FKFS = "2";
				}else if("冲暂支".equals(FKFS)){
					FKFS = "3";
				}
				head.put("FKFS", FKFS);
			}
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

