package tj.oatojd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tj.util.GetUtil;
import tj.util.HttpConstant;
import tj.util.HttpRequest;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-16 下午2:50:24
 * 请款单内-->(预付款单)
 */
public class RequestFundsRet implements Action{

	@Override
	public String execute(RequestInfo info) {
		GetUtil gu = new GetUtil();
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		RecordSetDataSource res = new RecordSetDataSource("k3_test");
		JSONObject json = new JSONObject();
		JSONArray head = new JSONArray();
		JSONArray body = new JSONArray();
		String qgrq = "";//请购日期
		String jhrq = "";//交货日期
		String mainid = "";
		String sql = "select * from "+tablename+" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			JSONObject headdt = new JSONObject();
			try {
				String bmbm = gu.getFieldVal("uf_bmfygx ", "bmbm", "bmmc",Util.null2String(rs.getString("BM")));
				headdt.put("FcustomerId", Util.null2String(rs.getString("SKDW")));//供应商内码---无供应商字段？？？？？？
				headdt.put("FDeptId",gu.getK3FieldVal("A00_V_Department", "FItemID", "FNUMBER", bmbm) );//部门内码  ---
				headdt.put("FEmployeeNumber", gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("cjr"))));///业务员---？？？？？？？
				headdt.put("Famount", Util.null2String(rs.getString("jehzxx")));///金额---?????哪个金额  金额汇总小写
				headdt.put("FisSpecial", Util.null2String(rs.getString("sffgcd")));///是否付给成都/蚌埠字段---
				headdt.put("FCurrencyID", "1");///币别---
				headdt.put("FExchangeRate", "1");///汇率---
				head.put(headdt);
			} catch (JSONException e) {
				log.writeLog("请款单内-->(预付款单)异常1--------------------"+e.getMessage());
			}
		}
		try {
			json.put("head", head);
			json.put("body", body);
			json.put("typecode", "1000");
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("请款单内-->(预付款单)异常2--------------------"+e.getMessage());
		}
		HttpRequest hr = new HttpRequest();
		//{"code":1,"mesage":"数据成功返回","Success":"OK"}
		log.writeLog("请款单内-->(预付款单)json--------------------"+json.toString());
		String result = hr.sendPost(HttpConstant.OATOHR_URL, json.toString());
		log.writeLog("请款单内-->(预付款单)result--------------------"+result);
		try {
			JSONObject  jsr = new JSONObject(result);
			if(!jsr.isNull("Success")){
				String suc = jsr.getString("Success");
				if(!"成功".equals(suc)){
					info.getRequestManager().setMessagecontent(jsr.getString("mesage"));    
					info.getRequestManager().setMessageid("信息提示");
					return SUCCESS;
				}
			}
		} catch (JSONException e) {
			log.writeLog("请款单内-->(预付款单)异常3--------------------"+e.getMessage());
		}
		return SUCCESS;
	}
}
