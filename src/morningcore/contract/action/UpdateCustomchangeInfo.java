package morningcore.contract.action;

import morningcore.contract.bean.ChangeFieldBean;
import morningcore.contract.dao.ChangeFieldDao;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * createby jianyong.tang
 * createTime 2020/5/20 21:13
 * version v1
 * desc
 */
public class UpdateCustomchangeInfo implements Action {
    @Override
    public String execute(RequestInfo info) {
        BaseBean log = new BaseBean();
        log.writeLog("UpdateCustomchangeInfo start");
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        String sql_dt = "";
        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();
        String tableName = "";
        String mainid = "";
        ChangeFieldDao cfd = new ChangeFieldDao();
        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= " + workflowID
                + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        sql = "select * from "+tableName+" where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()) {
            mainid = Util.null2String(rs.getString("id"));
        }
        sql = "select sysno,bgzd,oldvalue,newvalue from "+tableName+"_dt1  where mainid="+mainid;
        //log.writeLog("sql:"+sql);
        rs.execute(sql);
        while(rs.next()) {
            String seqno = Util.null2String(rs.getString("sysno"));
            String bgzd = Util.null2String(rs.getString("bgzd"));
            String newvalue = "";
            sql_dt = "select fieldname,oldvalue,newvalue from uf_change_detail where seqno='" + seqno + "'";
            rs_dt.execute(sql_dt);
            if (rs_dt.next()) {
                newvalue = Util.null2String(rs_dt.getString("newvalue"));
            }
            ChangeFieldBean cfb = cfd.getChangeFieldBean(bgzd);
            String dbfieldname = cfb.getFieldname();
            sql_dt = "update "+tableName+" set "+dbfieldname+"='"+newvalue+"' where requestid="+requestid;
            log.writeLog("sql_dt:"+sql_dt);
            rs_dt.execute(sql_dt);
        }

        return SUCCESS;
    }
}
