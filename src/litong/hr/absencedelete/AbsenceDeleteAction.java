package litong.hr.absencedelete;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;

public class AbsenceDeleteAction implements Action {

    /***
     *  ( p_serno      in  varchar2,     bdbh
     *     p_id_no_sz   in  varchar2,    --員工代號   sqrgh
     *     p_datetime_b in  date,    --請假日期時間(起)格式西元YYYY/MM/DD HH24:MI  xjksrq,xjkssj
     *     p_datetime_e in  date,    --請假日期時間(迄)格式西元YYYY/MM/DD HH24:MI xjjsrq,xjjssj
     *     p_reason     in  varchar2,    --假別(假扣代號設定內設定之假別代號)
     *     p_segment_no   in varchar2,   --公司代號
     *     p_error out varchar2,       --錯誤原因   zt
     *     p_error_type  in  varchar2 default 'MSG'   --訊息類別 'MSG'多語訊息  'CODE'代碼 預設為多語訊息  ####
     *     )
     *     bdbh,sqrgh,xjksrq,xjkssj,xjjsrq,xjjssj,bcgxjsj,sqyy,zt,cczt,xjlx
     */
    @Override
    public String execute(RequestInfo info) {
        BaseBean log = new BaseBean();
        log.writeLog("AbsenceDeleteAction.......");
        String requestid = info.getRequestid();
        String workflowid = info.getWorkflowid();
        String tableName = "";
        String p_serno = "";
        String p_id_no_sz = "";
        String p_begindate = "";
        String p_enddate = "";
        String p_begintime = "";
        String p_endtime = "";
        String p_reason = "";
        String p_segment_no = "";
        String p_error_type = "MSG";
        RecordSet rs = new RecordSet();
        String sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
                + workflowid + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        sql = "select * from " + tableName + " where requestid = " + requestid;
        rs.execute(sql);
        if(rs.next()){
            p_serno = Util.null2String(rs.getString("glqjlc"));
            p_id_no_sz = Util.null2String(rs.getString("sqrgh"));
            p_begindate = Util.null2String(rs.getString("xjksrq"));
            p_enddate = Util.null2String(rs.getString("xjjsrq"));
            p_begintime = Util.null2String(rs.getString("xjkssj"));
            p_endtime = Util.null2String(rs.getString("xjjssj"));
            p_reason = Util.null2String(rs.getString("jbdm"));
            p_segment_no = Util.null2String(rs.getString("gsdm"));
        }
//        log.writeLog("p_serno(表单编号)-->" + p_serno + ",p_id_no_sz(人员编号)-->" + p_id_no_sz + ",p_begindate(开始日期)-->"
//                + p_begindate + ",p_enddate(结束日期)-->" + p_enddate + ",p_begintime(开始时间)-->" + p_begintime + p_reason
//                + ",p_segment_no(分部代码)-->" + p_segment_no);
        Map<String,String> map = new HashMap<String, String>();
        map.put("p_serno",requestid);
        map.put("p_id_no_sz",p_id_no_sz);
        map.put("p_datetime_b",p_begindate + " " + p_begintime);
        map.put("p_datetime_e",p_enddate + " " + p_endtime);
        map.put("p_reason",p_reason);
        map.put("p_segment_no",p_segment_no);
        map.put("p_error_type",p_error_type);
        String czzt = new AbsenceDelProc().callProcedure(map);
        sql = "update " + tableName + " set czzt = '" + czzt + "' where requestid = " + requestid;
        rs.execute(sql);
        log.writeLog("sql-->" + sql);
        return SUCCESS;
    }
}
