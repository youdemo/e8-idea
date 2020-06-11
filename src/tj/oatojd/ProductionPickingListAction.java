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
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 生产领料单接口Action
 *
 * @author : adore
 * @version : v1.0
 * @since : 2019-08-30 13:07:44
 */
public class ProductionPickingListAction extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo info) {
        GetUtil gu = new GetUtil();
        String requestid = info.getRequestid();
        this.writeLog("生产领料单接口Action开始：" + requestid);
        String tablename = info.getRequestManager().getBillTableName();

        RecordSet rs = new RecordSet();
        JSONObject json = new JSONObject();
        JSONArray head = new JSONArray();
        JSONArray body = new JSONArray();
        String sql;
        String mainid = "";
        String jingb = ""; // 经办
        String bm = ""; // 部门
        if (!"".equals(tablename)) {
            sql = "select * from " + tablename + " where requestid = '" + requestid + "'";
            writeLog("流程主数据：" + sql);
            rs.execute(sql);
            if (rs.next()) {
                JSONObject headdt = new JSONObject();
                try {
                    mainid = Util.null2String(rs.getString("id"));
                    jingb = Util.null2String(rs.getString("jingb"));
                    bm = Util.null2String(rs.getString("bm"));

                    String bmbm = gu.getFieldVal("uf_bmfygx", "bmbm", "bmmc", bm);
                    headdt.put("FDeptId", gu.getK3FieldVal("A00_V_Department", "FItemID", "FNUMBER", bmbm)); // 部门内码
                    ResourceComInfo rc = new ResourceComInfo();
                    String workCode = Util.null2String(rc.getWorkcode(jingb));
                    headdt.put("FRequesterID", gu.getK3FieldVal("A00_V_Emp", "FItemID", "FNUMBER", workCode)); // 领料申请人内码
                    headdt.put("FBillerId", gu.getK3FieldVal("A00_V_Emp", "FUserId", "FNUMBER", workCode)); // 制单人
                    head.put(headdt);
                    this.writeLog("head:" + head.toString());
                } catch (Exception e) {
                    this.writeLog("生产领料单数据回传异常1--------------------" + e.getMessage());
                }
            }
            sql = "select * from " + tablename + "_dt1 where  mainid ='" + mainid + "' and wlbh is not null and wlbh <> ''  order by id ";
            this.writeLog("明细数据:" + sql);
            rs.execute(sql);
            int Hno = 1;
            while (rs.next()) {
                JSONObject dt = new JSONObject();
                try {
                    String wlbh = Util.null2String(rs.getString("wlbh"));
                    String ckbm = Util.null2String(rs.getString("ckbm"));
                    dt.put("FEntryID", Hno + ""); // 行号
                    dt.put("FItemID", wlbh); // 物料内码   --物料编号
                    dt.put("FUnitId", gu.getK3FieldVal("A00_V_Item", "FUnitID", "FItemID", wlbh)); // 单位内码----单位
                    dt.put("FQty", Util.null2String(rs.getString("sfsl"))); // 数量--
                    dt.put("FStockID", ckbm); // 仓库
                    body.put(dt);
                    Hno++;
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.writeLog("生产领料单数据回传异常2--------------------" + e.getMessage());
                }
            }
            try {
                json.put("head", head);
                json.put("body", body);
                json.put("typecode", "24");
            } catch (JSONException e) {
                e.printStackTrace();
                this.writeLog("生产领料单数据回传异常3--------------------" + e.getMessage());
            }
            HttpRequest hr = new HttpRequest();
            //{"code":1,"mesage":"数据成功返回","Success":"OK"}
            this.writeLog("生产领料单数据回传json--------------------:" + json.toString());
            String result = hr.sendPost(HttpConstant.OATOHR_URL, json.toString());
            this.writeLog("生产领料单数据回传result--------------------:" + result);
            try {
                JSONObject jsr = new JSONObject(result);
                if (!jsr.isNull("Success")) {
                    String suc = jsr.getString("Success");
                    if (!"成功".equals(suc)) {
                        info.getRequestManager().setMessagecontent(jsr.getString("mesage"));
                        info.getRequestManager().setMessageid("信息提示");
                        this.writeLog("生产领料单接口Action结束1：" + requestid);
                        return Action.SUCCESS;
                    }
                }
            } catch (JSONException e) {
                this.writeLog("请购数据回传异常4--------------------" + e.getMessage());
            }
        } else {
            this.writeLog("表名获取失败：" + requestid);
        }
        this.writeLog("生产领料单接口Action结束2：" + requestid);
        return Action.SUCCESS;
    }
}
