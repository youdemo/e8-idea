package weixin.action;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;

/**
 * createby jianyong.tang
 * createTime 2020/5/25 22:14
 * version v1
 * desc
 */
public class AddCountToBxjl implements Action {
    @Override
    public String execute(RequestInfo info) {
        String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
        String requestid = info.getRequestid();
        RecordSet rs = new RecordSet();
        String tableName = "";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        String mainID = "";
        String Name = "";
        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= " + workflowID
                + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        sql = "select * from "+tableName+" where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            mainID = Util.null2String(rs.getString("id"));
            Name = Util.null2String(rs.getString("Name"));
        }
        String year = "";
        sql = "select createdate from workflow_requestbase where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            year = Util.null2String(rs.getString("createdate")).substring(0,4);
        }
        int count = 0;
        sql = "select count(1) as count  from "+tableName+"_dt1 where mainid="+mainID+" and CostRMB>1000 and fkpzsc is null";
        rs.execute(sql);
        if(rs.next()){
            count = rs.getInt("count");
        }
        String billid = "";
        sql = "select * from uf__fybxjl_sp where rqid='"+requestid+"' and ry='"+Name+"'";
        rs.execute(sql);
        if(rs.next()){
            billid = Util.null2String(rs.getString("id"));
        }
        if("".equals(billid)){
            sql = "insert into uf__fybxjl_sp(nf,ry,rqid,cs) values('"+year+"','"+Name+"','"+requestid+"','"+count+"')";
            rs.execute(sql);
        }else{
            sql = "update uf__fybxjl_sp set cs='"+count+"' where id="+billid;
            rs.execute(sql);
        }
        return SUCCESS;
    }
}
