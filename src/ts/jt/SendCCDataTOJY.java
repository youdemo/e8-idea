package ts.jt;

import ts.util.TransUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class SendCCDataTOJY implements Action {


    @Override
    public String execute(RequestInfo info) {
        TransUtil tu = new TransUtil();
        RecordSet rs = new RecordSet();
        RecordSetDataSource rsd = new RecordSetDataSource("HR");
        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();
        String tableName = tu.getTableName(workflowID);
        String gh = "";
        String ksrq = "";//开始日期
        String kssj = "";//开始时间
        String jsrq = "";//结束日期
        String jssj = "";//结束时间
        String chuchaixiaoshishu = "";//出差小时数
        String sql = "select * from "+tableName+" where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            gh = Util.null2String(rs.getString("gh"));
            ksrq = Util.null2String(rs.getString("ksrq"));
            kssj = Util.null2String(rs.getString("kssj"));
            jsrq = Util.null2String(rs.getString("jsrq"));
            jssj = Util.null2String(rs.getString("jssj"));
            chuchaixiaoshishu = Util.null2String(rs.getString("chuchaixiaoshishu"));
            if("".equals(chuchaixiaoshishu)){
                chuchaixiaoshishu = null;
            }

        }
        String term = "";
        String createdate = "";
        String createtime = "";
        sql = "select createdate,createtime from workflow_requestbase where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            createdate = Util.null2String(rs.getString("createdate"));
            createtime = Util.null2String(rs.getString("createtime"));
        }
        term = createdate+" "+createtime.substring(0,5);
        sql = "insert into aregleave_register(eid,Badge,Name,compid,DepID,jobid,azid,TWID,BeginTime,EndTime,Amount,Unit,OAseqid,Term) " +
                "select eid,badge,name,compid,depid,jobid,ezid,43,'"+ksrq+" "+kssj+"','"+jsrq+" "+jssj+"',"+chuchaixiaoshishu+",2,'"+requestid+"','"+term+"' from eemployee where badge='"+gh+"'";
        tu.writeLog("SendCCDataTOJY","sql:"+sql);
        Boolean result = rsd.execute(sql);
        if(!result){
            info.getRequestManager().setMessagecontent("数据传输嘉杨系统失败，请联系管理员");
            info.getRequestManager().setMessageid("ERROR:");
            return FAILURE_AND_CONTINUE;
        }
        return SUCCESS;
    }
}
