package tj.oatojd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tj.util.GetUtil;
import tj.util.HttpConstant;
import tj.util.HttpRequest;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-16 下午3:02:27  sh
 * 4.	检验单-->(反写收料(内)通知单)
业务描述: ERP中创建收料通知单,在OA中进行检验审批,将结果返回给对应的收料通知单,进行入库
 */
public class InspectionSlipRet  implements Action{

	@Override
	public String execute(RequestInfo info) {
		GetUtil gu = new GetUtil();
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
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
				headdt.put("FBillNo", Util.null2String(rs.getString("SLDHM")));//采购申请单单号
				headdt.put("FCaiGouBillNo", Util.null2String(rs.getString("QGHM")));//请购单号码
//				headdt.put("FSupplyID", Util.null2String(rs.getString("")));//供应商
//				headdt.put("FPOStyle", Util.null2String(rs.getString("")));//采购方式
//				headdt.put("FAreaPS", Util.null2String(rs.getString("")));//采购范围
//				headdt.put("FCurrencyID", Util.null2String(rs.getString("")));//币别
//				headdt.put("FExchangeRate", Util.null2String(rs.getString("")));//汇率
//				headdt.put("FBizType", Util.null2String(rs.getString("")));//业务类型
				mainid = rs.getString("id");				
//				//---------------------------------------
//				qgrq = Util.null2String(rs.getString("qgrq"));
//				mainid = Util.null2String(rs.getString("id"));
//				jhrq = Util.null2String(rs.getString("jhrq"));
//				headdt.put("FDate", Util.null2String(rs.getString("sqrq")));//制单日期格式  “yyyy-MM-dd” ---- 申请日期
//				headdt.put("Fbillerid", gu.getFieldVal("hrmresource", "lastname", "id", Util.null2String(rs.getString("sq"))));//制单人      --经办人姓名
//				headdt.put("FBillNo", Util.null2String(rs.getString("qghm")));///请购单单号----请购号码
//				headdt.put("FDeptId", gu.getFieldVal("uf_bmfygx ", "bmbm", "bmmc",Util.null2String(rs.getString("qgdw"))));//部门内码----  对应库里取
//				headdt.put("FRequesterID", gu.getFieldVal("hrmresource", "workcode", "id", Util.null2String(rs.getString("sq"))));//申请人内码---经办
				head.put(headdt);
			} catch (JSONException e) {
				log.writeLog("反写收料(内)通知单异常1--------------------"+e.getMessage());
			}
		}
		sql = "select * from "+tablename+"_dt1 where  mainid ='"+mainid+"' order by id ";
		int Hno= 1;
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject dt = new JSONObject();
			try {
				
				dt.put("FEntryID",Hno+"");//
				dt.put("FItemID",gu.getK3FieldVal("A00_V_Item", "FItemID", "FNumber", Util.null2String(rs.getString("WLBM"))));//物料内码   --物料编号
				dt.put("FHegeQty",Util.null2String(rs.getString("SL")));///数量-- 	合格数量


//				dt.put("FItemID",Util.null2String(rs.getString("wlbh")));//物料内码   --物料编号
//				dt.put("FUnitId",Util.null2String(rs.getString("dw")));//单位内码----单位
//				dt.put("FQty",Util.null2String(rs.getString("sl")));///数量--
//				dt.put("FAPurchTime",qgrq);//建议采购日期---主表请购日期
//				dt.put("FFetchtime",jhrq);//到货日期----主表交货日期
				Hno++;
				body.put(dt);
			} catch (JSONException e) {
				e.printStackTrace();
				log.writeLog("反写收料(内)通知单异常2--------------------"+e.getMessage());
			}
		}
		try {
			json.put("head", head);
			json.put("body", body);
			json.put("typecode", "72");
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("反写收料(内)通知单异常3--------------------"+e.getMessage());
		}
		HttpRequest hr = new HttpRequest();
		//{"code":1,"mesage":"数据成功返回","Success":"OK"}
		log.writeLog("反写收料(内)通知单json--------------------"+json.toString());
		String result = hr.sendPost(HttpConstant.OATOHR_URL, json.toString());
		log.writeLog("v反写收料(内)通知单result--------------------"+result);
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
			log.writeLog("反写收料(内)通知单异常4--------------------"+e.getMessage());
		}
		return SUCCESS;
	}
}