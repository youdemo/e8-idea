package txrz.util;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class LeaderUtil  implements Action
{
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        this.log.writeLog("进入直接上级LeaderUtil——————");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();
        String wfcreater = "";

        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        RecordSet rs1 = new RecordSet();

        String sql = "";
        String tableName = "";

        String[] leadergroup = new String[100];

        sql = " select creater from workflow_requestbase where requestid= " + requestid;
        rs.execute(sql);
        if (rs.next()) {
            wfcreater = Util.null2String(rs.getString("creater"));
        }

        sql = " Select tablename From Workflow_bill Where id in (Select formid From workflow_base Where id= " + workflowID + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!("".equals(tableName)))
        {
            sql = "select qkr from "+tableName+" where requestid="+requestid;
            rs.execute(sql);
            if(rs.next()){
                wfcreater = Util.null2String(rs.getString("qkr"));
            }

            sql = "select managerstr from hrmresource where id = " + wfcreater;
            this.log.writeLog("sql_leader=" + sql);
            rs.execute(sql);
            if (rs.next()) {
                String tem_managerstr = Util.null2String(rs.getString("managerstr"));
                tem_managerstr = tem_managerstr.substring(1, tem_managerstr.length() - 1);

                leadergroup = tem_managerstr.split(",");

                this.log.writeLog("当前人所有上级" + tem_managerstr);
                for (int i = 0; i < leadergroup.length; ++i) {
                    String tmpLeader = leadergroup[i];
                    sql = " select seclevel from HrmResource where id=" + tmpLeader;
                    this.log.writeLog("sql_tmp_" + tmpLeader + "=" + sql);
                    res.execute(sql);
                    if (res.next()) {
                        String seclevel = Util.null2String(res.getString("seclevel"));
                        if ("20".equals(seclevel)) {
                            sql = " update " + tableName + " set xzzz = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            this.log.writeLog("sql_" + tmpLeader + "=" + sql);
                        }

                        if ("80".equals(seclevel)) {
                            sql = " update " + tableName + " set dzzz = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            this.log.writeLog("sql_" + tmpLeader + "=" + sql);
                        }

                        if ("90".equals(seclevel)) {
                            sql = " update " + tableName + " set bjzg = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            this.log.writeLog("sql_" + tmpLeader + "=" + sql);
                        }
                        if ("95".equals(seclevel)) {
                            sql = " update " + tableName + " set cjzg = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            this.log.writeLog("sql_" + tmpLeader + "=" + sql);
                        }
                    }
                }
            }
        }
        else {
            this.log.writeLog("流程表单获取失败!");
            return FAILURE_AND_CONTINUE;
        }
        return SUCCESS;
    }
}