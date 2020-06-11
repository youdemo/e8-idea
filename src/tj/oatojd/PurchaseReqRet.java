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
 * @version 创建时间：2019-6-16 下午1:25:53
 * 请购数据回传
 */
public class PurchaseReqRet implements Action{

	@Override
	public String execute(RequestInfo info) {
		GetUtil gu = new GetUtil();
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet res = new RecordSet();
		RecordSet rs = new RecordSet();
		JSONObject json = new JSONObject();
		JSONArray head = new JSONArray();
		JSONArray body = new JSONArray();
		String qgrq = "";//请购日期
		String jhrq = "";//交货日期
		String nwg = "";//内外购类型
		String sq = "";//经办人
		String xyrq = ""; 
		String mainid = "";
		String sql = "select * from "+tablename+" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			JSONObject headdt = new JSONObject();
			try {
				qgrq = Util.null2String(rs.getString("qgrq"));
				mainid = Util.null2String(rs.getString("id"));
				jhrq = Util.null2String(rs.getString("jhrq"));
				xyrq = Util.null2String(rs.getString("xyrq"));
				jhrq = Util.null2String(rs.getString("jhrq"));
				sq = Util.null2String(rs.getString("sq")); //FJiangbanID
				String sq1gh = gu.getFieldVal("hrmresource", "workcode", "id", sq);
				nwg = Util.null2String(rs.getString("bldw"));//办理单位
				headdt.put("FDate", qgrq);//制单日期格式  “yyyy-MM-dd” ---- 申请日期  
//				headdt.put("Fbillerid", gu.getFieldVal("hrmresource", "lastname", "id", Util.null2String(rs.getString("sq"))));//制单人      --经办人姓名
				headdt.put("FBillerId", gu.getK3FieldVal("A00_V_Emp", "FUserId", "FNUMBER", Util.null2String(rs.getString("gh"))));
				headdt.put("FJingbanID", gu.getK3FieldVal("A00_V_Emp", "FItemID", "FNUMBER", sq1gh));//经办人20190923
				headdt.put("FCaiGouBillNo", Util.null2String(rs.getString("qghm")));///请购单单号----请购号码
//				headdt.put("FDeptId", gu.getFieldVal("uf_bmfygx ", "bmbm", "bmmc",Util.null2String(rs.getString("qgdw"))));//部门内码----  对应库里取
				headdt.put("FDeptId", gu.getK3FieldVal("A00_V_Department", "FItemID", "FNUMBER", Util.null2String(rs.getString("bmbm"))));
//				headdt.put("FRequesterID", gu.getFieldVal("hrmresource", "workcode", "id", Util.null2String(rs.getString("sq"))));//申请人内码---经办
				headdt.put("FRequesterID", gu.getK3FieldVal("A00_V_Emp", "FItemID", "FNUMBER", Util.null2String(rs.getString("gh"))));//申请人内码---经办
				if("0".contentEquals(nwg)) {
					headdt.put("FIsWaiGou", "内购");
				}else {
					headdt.put("FIsWaiGou", "外购");
				}
				head.put(headdt);
			} catch (JSONException e) {
				log.writeLog("请购数据回传异常1--------------------"+e.getMessage());
			}
		}
		sql = "select * from "+tablename+"_dt1 where  mainid ='"+mainid+"' and wlbh is not null and wlbh <> ''  order by id asc";
		rs.executeSql(sql);
		int Hno = 1;
		int flagA = 1;
		
		while(rs.next()){
			JSONObject dt = new JSONObject();
			try {
				dt.put("FEntryID",Hno+"");//行号
				dt.put("FItemID",Util.null2String(rs.getString("wlbh")));//物料内码   --物料编号
				dt.put("FUnitId",gu.getK3FieldVal("A00_V_Item", "FUnitID", "FItemID", Util.null2String(rs.getString("wlbh")))) ;//单位内码----单位
				dt.put("FQty",Util.null2String(rs.getString("sl")));///数量--
				dt.put("FAPurchTime",xyrq);//建议采购日期---主表请购日期
				dt.put("FFetchtime",jhrq);//到货日期----主表交货日期
				
				String str = "select xj,dj4w,nm from "+tablename+"_dt2 where  mainid ='"+mainid+"' and gys is not null and gys <> ''  order by id asc";
				res.executeSql(str);
				int flagB = 1;
				while(res.next()) {//
					if(flagB == flagA) {
						String xj = Util.null2String(res.getString("xj"));
						String dj4w = Util.null2String(res.getString("dj4w"));
						String nm = Util.null2String(res.getString("nm"));
						dt.put("FAmount",xj);//小计
						dt.put("FPrice",dj4w);//单价		
						dt.put("FSupplyID",nm);//供应商内码	20190923	
					}
					flagB++;
				}
				body.put(dt);
				Hno++;
			} catch (JSONException e) {
				e.printStackTrace();
				log.writeLog("请购数据回传异常2--------------------"+e.getMessage());
			}
			flagA++;
		}
		try {
			json.put("head", head);
			json.put("body", body);
			json.put("typecode", "70");
		} catch (JSONException e) {
			e.printStackTrace();
			log.writeLog("请购数据回传异常3--------------------"+e.getMessage());
		}
		HttpRequest hr = new HttpRequest();
		//{"code":1,"mesage":"数据成功返回","Success":"OK"}
		log.writeLog("请购数据回传json--------------------"+json.toString());
		String result = hr.sendPost(HttpConstant.OATOHR_URL, json.toString());
		log.writeLog("请购数据回传result--------------------"+result);
		try {
			JSONObject  jsr = new JSONObject(result);
			if(!jsr.isNull("Success")){
				String suc = jsr.getString("Success");
				if(!"成功".equals(suc)){
					info.getRequestManager().setMessagecontent(jsr.getString("mesage"));    
					info.getRequestManager().setMessageid("信息提示");
					return FAILURE_AND_CONTINUE;
				}
			}
		} catch (JSONException e) {
			log.writeLog("请购数据回传异常4--------------------"+e.getMessage());
		}
		return SUCCESS;
	}
}
