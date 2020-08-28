package hhgd.doc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class DeleteDoc implements Action{

    @Override
    public String execute(RequestInfo info) {
        String requestid = info.getRequestid();
        String workflowid = info.getWorkflowid();
        BaseBean log = new BaseBean();
        String ed = "";
        String sfgd = "";
        String tablename = info.getRequestManager().getBillTableName();
        RecordSet rs = new  RecordSet();
        RecordSet res = new  RecordSet();
        new BaseBean().writeLog(this.getClass().getName(),"DeleteDoc requestid:"+requestid);
        String sql = "select wd,sfgd from "+ tablename +" where requestid = '" + requestid + "' and sfgd=1 ";
        rs.executeSql(sql);
        if(rs.next()){
            ed = Util.null2String(rs.getString("wd"));
            sfgd = Util.null2String(rs.getString("sfgd"));

        }
        new BaseBean().writeLog(this.getClass().getName(),"ed:"+ed+" sfgd:"+sfgd);

        String  lastid = "";
        int con = 0;
        sql = "select count(id) as con  from docdetail where parentids = '" + ed + "'";
        rs.executeSql(sql);
        if(rs.next()){
            con = rs.getInt("con");

        }
        new BaseBean().writeLog(this.getClass().getName(),"con:"+con);

        if(con>1){
            String  st = "select max(id) as mid from docdetail where parentids = '" + ed + "' and id <> '"+ed+"' ";
            new BaseBean().writeLog(this.getClass().getName(),"st:"+st);

            res.executeSql(st);
            String mid = "";
            if(res.next()){
                mid = Util.null2String(res.getString("mid"));
                lastid = mid;
                st = "update docdetail set parentids = '" + mid + "' where parentids = '" + ed + "'";
                new BaseBean().writeLog(this.getClass().getName(),"mid:"+mid+" st:"+st);
                res.executeSql(st);
            }


            sql = "delete docdetail where id = '" + ed + "'";
            new BaseBean().writeLog(this.getClass().getName(),"sql:"+sql);

            rs.executeSql(sql);
            sql = "update docdetail set ishistory = 0 ,docstatus=2 where id = '"+lastid+"'"; 
            log.writeLog("update--------"+sql);
			rs.executeSql(sql);
        }
        new BaseBean().writeLog(this.getClass().getName(),"结束");

        return SUCCESS;
    }


}
