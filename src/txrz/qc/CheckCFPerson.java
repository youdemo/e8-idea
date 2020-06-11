package txrz.qc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.AllManagers;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CheckCFPerson implements Action {

    @Override
    public String execute(RequestInfo info) {
        BaseBean log = new BaseBean();
        log.writeLog("CheckCFPerson", "开始拆分人员");
        String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
        String requestid = info.getRequestid();
        RecordSet rs = new RecordSet();
        AllManagers al = new AllManagers();
        String tableName = "";
        String sfcfjh = "1";
        String sfxyzjl = "";//4.是否需要总经理签核
        String sfxydszqh = "";//5.是否需要董事长签核
        String sfxyjkqh = "";//6.是否需要金控签核
        String cjzg = "";//初级主管
        String jh = "";//敬会租赁
        String cfr1 = "";//拆分人1
        String cfr2 = "";
        String flag1 = "";
        String flag2 = "";
        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= " + workflowID
                + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        sql = "select * from " + tableName + " where requestid=" + requestid;
        rs.execute(sql);
        if (rs.next()) {
            sfxyzjl = Util.null2String(rs.getString("sfxyzjl"));
            sfxydszqh = Util.null2String(rs.getString("sfxydszqh"));
            sfxyjkqh = Util.null2String(rs.getString("sfxyjkqh"));
            cjzg = Util.null2String(rs.getString("cjzg"));
            jh = Util.null2String(rs.getString("jh"));
        }
        if ("0".equals(sfxyzjl) || "0".equals(sfxydszqh) || "0".equals(sfxyjkqh)) {
            if (!"".equals(jh) && !"".equals(cjzg)) {
                String cjzg1 = "," + cjzg + ",";
                String jhry[] = jh.split(",");
                for (String ryid : jhry) {
                    if (ryid.equals(cjzg)) {
                        cfr2 = cfr2 + flag2 + ryid;
                        flag2 = ",";
                        continue;
                    }
                    if ("".equals(ryid)) {
                        continue;
                    }
                    log.writeLog("CheckCFPerson", "人员:" + ryid);
                    String managerstr = al.getAllManagerstr("" + ryid);
                    log.writeLog("managerstr", "人员managerstr:" + managerstr);
                    managerstr = "," + managerstr + ",";
                    if (managerstr.indexOf(cjzg1) >= 0) {
                        cfr1 = cfr1 + flag1 + ryid;
                        flag1 = ",";
                        sfcfjh = "0";
                    } else {
                        cfr2 = cfr2 + flag2 + ryid;
                        flag2 = ",";
                    }
                }
            }
        }
        sql = "update "+tableName+" set cfr1='"+cfr1+"',cfr2='"+cfr2+"',sfcfjh='"+sfcfjh+"' where requestid="+requestid;
        log.writeLog("managerstr", "sql:" + sql);
        rs.execute(sql);
        return SUCCESS;
    }

}
