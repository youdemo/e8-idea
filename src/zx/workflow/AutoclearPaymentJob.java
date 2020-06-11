package zx.workflow;

import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowServiceImpl;
import zx.sap.BringMainAndDetailByMain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AutoclearPaymentJob extends BaseCronJob {
    public void execute() {
        BaseBean log = new BaseBean();
        log.writeLog("AutoclearPaymentJob","自动清账开始");
        dojob();
        log.writeLog("AutoclearPaymentJob","自动清账结束");
    }

    public void dojob(){
        //根据配置表获取需要处理的流程节点
        BaseBean log = new BaseBean();
        RecordSet rs = new RecordSet();
        String type = "";//	标识
        String wfid = "";//流程类型id
        String nodeid = "";//节点id
        String requestids = "";//需要处理的流程id
        String tableName = "";//表名
        String sql = "select * from uf_auto_clear_mt";
        rs.execute(sql);
        while(rs.next()){
            type = Util.null2String(rs.getString("type"));
            wfid = Util.null2String(rs.getString("wfid"));
            nodeid = Util.null2String(rs.getString("nodeid"));
            requestids = getRequestids(wfid,nodeid);
            if("".equals(requestids)){
                continue;
            }
            tableName = getTableName(wfid);
            if("YBWLFPYZ".equals(type)){
                log.writeLog("AutoclearPaymentJob","一般物料（原材料）发票验证 requestids:"+requestids);
                doYbwl(tableName,requestids);
            }else if("FYGZLSHQK".equals(type)){
                log.writeLog("AutoclearPaymentJob","费用固资类收货请款 requestids:"+requestids);
                doFygzlshqk(tableName,requestids);
            }
            else if("YFKSQONE".equals(type)){
                log.writeLog("AutoclearPaymentJob","预付款申请ONE requestids:"+requestids);
                doYFKSQOne(tableName,requestids);
            }
            else if("YFKSQTWO".equals(type)){
                log.writeLog("AutoclearPaymentJob","预付款申请TWO requestids:"+requestids);
                doYFKSQTwo(tableName,requestids);
            }else if("XYZSQONE".equals(type)){
                log.writeLog("AutoclearPaymentJob","信用证申请ONE requestids:"+requestids);
                doXYZSQOne(tableName,requestids);
            }
            else if("XYZSQTWO".equals(type)){
                log.writeLog("AutoclearPaymentJob","信用证申请TWO requestids:"+requestids);
                doXYZSQTwo(tableName,requestids);
            }

        }


    }


    /**
     * 处理一般物料（原材料）发票验证 数据
     * @param tablename
     * @param requestids
     * @return
     */
    public void doYbwl(String tablename,String requestids){
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String requestid = "";
        String gsdm = "";//公司代码
        String GYSZHH = "";//供应商帐户号
        String year = sf.format(new Date());//财年
        String kjpzhm = "";//会计凭证号
        String bcqktjdm = "";//本次请款条件代码
        String je = "";

        String MSG_TYPE = "";//消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        String MSG_TEXT = "";// 消息文本
        String AUGBL = "";//清算单据的单据号码
        String AUGCP = "";// 清算分录日期
        String sql = "select requestid,gsdm,GYSZHH,kjpzhm,bcqktjdm,cast(isnull(BCZFZJEHS*bcqkbl,0) as numeric(12,2)) as je from "+tablename+" where requestid in("+requestids+")";
        rs.execute(sql);
        while(rs.next()){
            requestid = Util.null2String(rs.getString("requestid"));
            gsdm = getSelectName(tablename,"gsdm",Util.null2String(rs.getString("gsdm")));
            GYSZHH = Util.null2String(rs.getString("GYSZHH"));
            kjpzhm = Util.null2String(rs.getString("kjpzhm"));
            bcqktjdm = Util.null2String(rs.getString("bcqktjdm"));
            je = Util.null2String(rs.getString("je"));
            log.writeLog("AutoclearPaymentJob","gsdm,GYSZHH,kjpzhm,year,bcqktjdm,je:"+gsdm+","+GYSZHH+","+kjpzhm+","+year+","+bcqktjdm+","+je);
            String result = getSapData(gsdm,GYSZHH,kjpzhm,year,bcqktjdm,je);
            log.writeLog("result:"+result);
            try {
                JSONObject json = new org.json.JSONObject(result);
                JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
                JSONObject jsonx = (JSONObject) jsonArr.get(0);
                MSG_TYPE = jsonx.getString("MSG_TYPE");
                MSG_TEXT = jsonx.getString("MSG_TEXT");
                AUGBL = jsonx.getString("AUGBL");
                AUGCP = jsonx.getString("AUGDT");
                sql_dt = "update "+tablename+" set MSG_TYPE='"+MSG_TYPE+"',MSG_TEXT='"+MSG_TEXT+"',AUGBL='"+AUGBL+"',AUGCP='"+AUGCP+"' where requestid="+requestid;
                log.writeLog("sql_dt:"+sql_dt);
                rs_dt.execute(sql_dt);
                if(!"".equals(AUGBL)&&!"".equals(AUGCP)){
                    String userid = "";
                    sql_dt="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
                    rs_dt.execute(sql_dt);
                    if(rs_dt.next()) {
                        userid = Util.null2String(rs_dt.getString("userid"));
                    }
                    if("".equals(userid)){
                        userid = "1";
                    }
                    autoSubmit(requestid,userid);
                }
            }catch(Exception e){

            }

        }


    }


    /**
     * 费用固资类收货请款
     * @param tablename
     * @param requestids
     * @return
     */
    public void doFygzlshqk(String tablename,String requestids){
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String requestid = "";
        String gsdm = "";//公司代码
        String gyszhh = "";//供应商帐户号
        String year = sf.format(new Date());//财年
        String kjpzhm = "";//会计凭证号
        String bcqktjdm = "";//本次请款条件代码
        String je = "";

        String MSG_TYPE = "";//消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        String MSG_TEXT = "";// 消息文本
        String AUGBL = "";//清算单据的单据号码
        String AUGCP = "";// 清算分录日期
        String sql = "select requestid,gsdm,gyszhh,kjpzhm,bcqktjdm,cast(isnull(bczfzjehs*isnull(bcqkbl,0),0) as numeric(12,2)) as je from "+tablename+" where requestid in("+requestids+")";
        rs.execute(sql);
        while(rs.next()){
            requestid = Util.null2String(rs.getString("requestid"));
            gsdm = getSelectName(tablename,"gsdm",Util.null2String(rs.getString("gsdm")));
            gyszhh = Util.null2String(rs.getString("gyszhh"));
            kjpzhm = Util.null2String(rs.getString("kjpzhm"));
            bcqktjdm = Util.null2String(rs.getString("bcqktjdm"));
            je = Util.null2String(rs.getString("je"));
            log.writeLog("AutoclearPaymentJob","gsdm,gyszhh,kjpzhm,year,bcqktjdm,je:"+gsdm+","+gyszhh+","+kjpzhm+","+year+","+bcqktjdm+","+je);
            String result = getSapData(gsdm,gyszhh,kjpzhm,year,bcqktjdm,je);
            log.writeLog("result:"+result);
            try {
                JSONObject json = new org.json.JSONObject(result);
                JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
                JSONObject jsonx = (JSONObject) jsonArr.get(0);
                MSG_TYPE = jsonx.getString("MSG_TYPE");
                MSG_TEXT = jsonx.getString("MSG_TEXT");
                AUGBL = jsonx.getString("AUGBL");
                AUGCP = jsonx.getString("AUGDT");
                sql_dt = "update "+tablename+" set MSG_TYPE='"+MSG_TYPE+"',MSG_TEXT='"+MSG_TEXT+"',AUGBL='"+AUGBL+"',AUGCP='"+AUGCP+"' where requestid="+requestid;
                log.writeLog("sql_dt:"+sql_dt);
                rs_dt.execute(sql_dt);
                if(!"".equals(AUGBL)&&!"".equals(AUGCP)){
                    String userid = "";
                    sql_dt="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
                    rs_dt.execute(sql_dt);
                    if(rs_dt.next()) {
                        userid = Util.null2String(rs_dt.getString("userid"));
                    }
                    if("".equals(userid)){
                        userid = "1";
                    }
                    log.writeLog("userid:"+userid);
                    autoSubmit(requestid,userid);
                }
            }catch(Exception e){

            }

        }


    }

    /**
     * 预付款申请One
     * @param tablename
     * @param requestids
     * @return
     */
    public void doYFKSQOne(String tablename,String requestids){
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String requestid = "";
        String gsdm = "";//公司代码
        String gysdm = "";//供应商代码
        String year = sf.format(new Date());//财年
        String FI_NO = "";//会计凭证号
        String cn = "";//财年
        String je = "";

        String MSG_TYPE = "";//消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        String MSG_TEXT = "";// 消息文本
        String AUGBL = "";//清算单据的单据号码
        String AUGCP = "";// 清算分录日期
        String sql = "select requestid,gsdm,gysdm,FI_NO,cn, isnull(bcqkhsje_fin,0.00) as je from "+tablename+" where requestid in("+requestids+")";
        rs.execute(sql);
        while(rs.next()){
            requestid = Util.null2String(rs.getString("requestid"));
            gsdm = getSelectName(tablename,"gsdm",Util.null2String(rs.getString("gsdm")));
            gysdm = Util.null2String(rs.getString("gysdm"));
            FI_NO = Util.null2String(rs.getString("FI_NO"));
            cn = Util.null2String(rs.getString("cn"));
            je = Util.null2String(rs.getString("je"));
            log.writeLog("AutoclearPaymentJob","gsdm,gysdm,FI_NO,cn,je:"+gsdm+","+gysdm+","+FI_NO+","+cn+","+je);
            String result = getSapData(gsdm,gysdm,FI_NO,cn,"","");
            log.writeLog("result:"+result);
            try {
                JSONObject json = new org.json.JSONObject(result);
                JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
                JSONObject jsonx = (JSONObject) jsonArr.get(0);
                MSG_TYPE = jsonx.getString("MSG_TYPE");
                MSG_TEXT = jsonx.getString("MSG_TEXT");
                AUGBL = jsonx.getString("AUGBL");
                AUGCP = jsonx.getString("AUGDT");
                sql_dt = "update "+tablename+" set qzfhzt='"+MSG_TYPE+"',qzfhxx='"+MSG_TEXT+"',fkqzpzh='"+AUGBL+"',fkqzrq='"+AUGCP+"' where requestid="+requestid;
                log.writeLog("sql_dt:"+sql_dt);
                rs_dt.execute(sql_dt);
                if(!"".equals(AUGBL)&&!"".equals(AUGCP)){
                    String userid = "";
                    sql_dt="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
                    rs_dt.execute(sql_dt);
                    if(rs_dt.next()) {
                        userid = Util.null2String(rs_dt.getString("userid"));
                    }
                    if("".equals(userid)){
                        userid = "1";
                    }
                    log.writeLog("userid:"+userid);
                    autoSubmit(requestid,userid);
                }
            }catch(Exception e){

            }

        }


    }

    /**
     * 预付款申请One
     * @param tablename
     * @param requestids
     * @return
     */
    public void doYFKSQTwo(String tablename,String requestids){
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String requestid = "";
        String gsdm = "";//公司代码
        String gysdm = "";//供应商代码
        String year = sf.format(new Date());//财年
        String fkqzpzh = "";//会计凭证号
        String cn = "";//财年

        String MSG_TYPE = "";//消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        String MSG_TEXT = "";// 消息文本
        String AUGBL = "";//清算单据的单据号码
        String AUGCP = "";// 清算分录日期
        String sql = "select requestid,gsdm,gysdm,fkqzpzh,cn, isnull(bcqkhsje_fin,0.00) as je from "+tablename+" where requestid in("+requestids+")";
        rs.execute(sql);
        while(rs.next()){
            requestid = Util.null2String(rs.getString("requestid"));
            gsdm = getSelectName(tablename,"gsdm",Util.null2String(rs.getString("gsdm")));
            gysdm = Util.null2String(rs.getString("gysdm"));
            fkqzpzh = Util.null2String(rs.getString("fkqzpzh"));
            cn = Util.null2String(rs.getString("cn"));
            log.writeLog("AutoclearPaymentJob","gsdm,gysdm,fkqzpzh,cn:"+gsdm+","+gysdm+","+fkqzpzh+","+cn);
            String result = getSapData(gsdm,gysdm,fkqzpzh,cn,"","");
            log.writeLog("result:"+result);
            try {
                JSONObject json = new org.json.JSONObject(result);
                JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
                JSONObject jsonx = (JSONObject) jsonArr.get(0);
                MSG_TYPE = jsonx.getString("MSG_TYPE");
                MSG_TEXT = jsonx.getString("MSG_TEXT");
                AUGBL = jsonx.getString("AUGBL");
                AUGCP = jsonx.getString("AUGDT");
                sql_dt = "update "+tablename+" set cxfhzt='"+MSG_TYPE+"',cxfhxx='"+MSG_TEXT+"',fpcxpzh='"+AUGBL+"',fpcxrq='"+AUGCP+"' where requestid="+requestid;
                log.writeLog("sql_dt:"+sql_dt);
                rs_dt.execute(sql_dt);
                if(!"".equals(AUGBL)&&!"".equals(AUGCP)){
                    String userid = "";
                    sql_dt="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
                    rs_dt.execute(sql_dt);
                    if(rs_dt.next()) {
                        userid = Util.null2String(rs_dt.getString("userid"));
                    }
                    if("".equals(userid)){
                        userid = "1";
                    }
                    log.writeLog("userid:"+userid);
                    autoSubmit(requestid,userid);
                }
            }catch(Exception e){

            }

        }


    }
    /**
     * 信用证申请One
     * @param tablename
     * @param requestids
     * @return
     */
    public void doXYZSQOne(String tablename,String requestids){
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String requestid = "";
        String gsdm = "";//公司代码
        String gysdm = "";//供应商代码
        String year = sf.format(new Date());//财年
        String FI_NO = "";//会计凭证号
        String cn = "";//财年
        String je = "";

        String MSG_TYPE = "";//消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        String MSG_TEXT = "";// 消息文本
        String AUGBL = "";//清算单据的单据号码
        String AUGCP = "";// 清算分录日期
        String sql = "select requestid,gsdm,VendorCode,FI_NO,CN, isnull(BCQKHSZE,0.00) as je from "+tablename+" where requestid in("+requestids+")";
        rs.execute(sql);
        while(rs.next()){
            requestid = Util.null2String(rs.getString("requestid"));
            gsdm = getSelectName(tablename,"gsdm",Util.null2String(rs.getString("gsdm")));
            gysdm = Util.null2String(rs.getString("VendorCode"));
            FI_NO = Util.null2String(rs.getString("FI_NO"));
            cn = Util.null2String(rs.getString("CN"));
            je = Util.null2String(rs.getString("je"));
            log.writeLog("AutoclearPaymentJob","gsdm,gysdm,FI_NO,cn,je:"+gsdm+","+gysdm+","+FI_NO+","+cn+","+je);
            String result = getSapData(gsdm,gysdm,FI_NO,cn,"",je);
            log.writeLog("result:"+result);
            try {
                JSONObject json = new org.json.JSONObject(result);
                JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
                JSONObject jsonx = (JSONObject) jsonArr.get(0);
                MSG_TYPE = jsonx.getString("MSG_TYPE");
                MSG_TEXT = jsonx.getString("MSG_TEXT");
                AUGBL = jsonx.getString("AUGBL");
                AUGCP = jsonx.getString("AUGDT");
                sql_dt = "update "+tablename+" set qzfhzt='"+MSG_TYPE+"',qzfhxx='"+MSG_TEXT+"',fkqzpzh='"+AUGBL+"',fkqzrq='"+AUGCP+"' where requestid="+requestid;
                log.writeLog("sql_dt:"+sql_dt);
                rs_dt.execute(sql_dt);
                if(!"".equals(AUGBL)&&!"".equals(AUGCP)){
                    String userid = "";
                    sql_dt="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
                    rs_dt.execute(sql_dt);
                    if(rs_dt.next()) {
                        userid = Util.null2String(rs_dt.getString("userid"));
                    }
                    if("".equals(userid)){
                        userid = "1";
                    }
                    log.writeLog("userid:"+userid);
                    autoSubmit(requestid,userid);
                }
            }catch(Exception e){

            }

        }


    }

    /**
     * 信用证申请TWO
     * @param tablename
     * @param requestids
     * @return
     */
    public void doXYZSQTwo(String tablename,String requestids){
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String requestid = "";
        String gsdm = "";//公司代码
        String gysdm = "";//供应商代码
        String year = sf.format(new Date());//财年
        String fkqzpzh = "";//会计凭证号
        String cn = "";//财年

        String MSG_TYPE = "";//消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        String MSG_TEXT = "";// 消息文本
        String AUGBL = "";//清算单据的单据号码
        String AUGCP = "";// 清算分录日期
        String sql = "select requestid,gsdm,VendorCode,fkqzpzh,cn, isnull(BCQKHSZE,0.00) as je from "+tablename+" where requestid in("+requestids+")";
        rs.execute(sql);
        while(rs.next()){
            requestid = Util.null2String(rs.getString("requestid"));
            gsdm = getSelectName(tablename,"gsdm",Util.null2String(rs.getString("gsdm")));
            gysdm = Util.null2String(rs.getString("VendorCode"));
            fkqzpzh = Util.null2String(rs.getString("fkqzpzh"));
            cn = Util.null2String(rs.getString("cn"));
            log.writeLog("AutoclearPaymentJob","gsdm,gysdm,fkqzpzh,cn:"+gsdm+","+gysdm+","+fkqzpzh+","+cn);
            String result = getSapData(gsdm,gysdm,fkqzpzh,cn,"","");
            log.writeLog("result:"+result);
            try {
                JSONObject json = new org.json.JSONObject(result);
                JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
                JSONObject jsonx = (JSONObject) jsonArr.get(0);
                MSG_TYPE = jsonx.getString("MSG_TYPE");
                MSG_TEXT = jsonx.getString("MSG_TEXT");
                AUGBL = jsonx.getString("AUGBL");
                AUGCP = jsonx.getString("AUGDT");
                sql_dt = "update "+tablename+" set cxfhzt='"+MSG_TYPE+"',cxfhxx='"+MSG_TEXT+"',fpcxpzh='"+AUGBL+"',fpcxrq='"+AUGCP+"' where requestid="+requestid;
                log.writeLog("sql_dt:"+sql_dt);
                rs_dt.execute(sql_dt);
                if(!"".equals(AUGBL)&&!"".equals(AUGCP)){
                    String userid = "";
                    sql_dt="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
                    rs_dt.execute(sql_dt);
                    if(rs_dt.next()) {
                        userid = Util.null2String(rs_dt.getString("userid"));
                    }
                    if("".equals(userid)){
                        userid = "1";
                    }
                    log.writeLog("userid:"+userid);
                    autoSubmit(requestid,userid);
                }
            }catch(Exception e){

            }

        }


    }
    /**
     * 自动提交
     * @param requestid
     * @param userid
     * @return
     */
    public String autoSubmit(String requestid, String userid) {
        WorkflowServiceImpl ws = new WorkflowServiceImpl();
        WorkflowRequestInfo wri = new WorkflowRequestInfo();
        String result = ws.submitWorkflowRequest(wri,
                Integer.valueOf(requestid), Integer.valueOf(userid), "submit",
                "自动提交");
        return result;
    }

    /**
     * 调用sap获取数据
     * @param BUKRS  公司代码
     * @param LIFNR  供应商或债权人的帐号
     * @param BELNR  会计凭证号码
     * @param GJAHR  财年
     * @param ZTERM  付款条件代码
     * @param WRBTR  凭证货币金额
     * @return
     */
    public String getSapData(String BUKRS, String LIFNR, String BELNR, String GJAHR,
                             String ZTERM,String WRBTR) {
        Map<String, String> oaDatas = new HashMap<String, String>();
        oaDatas.put("BUKRS", BUKRS);
        oaDatas.put("LIFNR", LIFNR);
        oaDatas.put("BELNR", BELNR);
        oaDatas.put("GJAHR", GJAHR);
        oaDatas.put("ZTERM", ZTERM);
        oaDatas.put("WRBTR", WRBTR);
        BringMainAndDetailByMain bmb = new BringMainAndDetailByMain("2");
        String result = bmb.getReturn(oaDatas, "7", "IS_INPUT", null, "");
        return result;
    }
    /**
     * 获取表名
     * @param workflowID
     * @return
     */
    public String getTableName(String workflowID){
        RecordSet rs = new RecordSet();
        String tableName = "";
        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= " + workflowID
                + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        return tableName;
    }

    /**
     * 获取处理的流程id
     * @param wfid
     * @param nodeid
     * @return
     */
    public String getRequestids(String wfid,String nodeid) {
        RecordSet rs = new RecordSet();
        String requestids = "";
        String flag = "";
        String sql = " select requestid from workflow_requestbase where workflowid=" + wfid + " and currentnodeid=" + nodeid;
        rs.execute(sql);
        while(rs.next()){
            requestids = requestids + flag + Util.null2String(rs.getString("requestid"));
            flag = ",";
        }
        return requestids;
    }

    /**
     * 根据选择框value 获取选择框name
     * @param tableName
     * @param filedname
     * @param selectvalue
     * @return
     */
    public String getSelectName(String tableName, String filedname,
                                 String selectvalue) {
        RecordSet rs = new RecordSet();
        String name = "";
        String sql = "select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"
                + tableName
                + "' and a.fieldname='"
                + filedname
                + "' and c.selectvalue='"
                + selectvalue
                + "'";
        //writeLog("sql:"+sql);
        rs.execute(sql);
        if (rs.next()) {
            name = Util.null2String(rs.getString("selectname"));
        }
        return name;
    }

}
