package weixin.action;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;

/**
 * createby jianyong.tang
 * createTime 2020/5/28 10:58
 * version v1
 * desc 更新流程走向标识
 */
public class UpdateLcZxBsAction implements Action {
    @Override
    public String execute(RequestInfo info) {
        String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
        String requestid = info.getRequestid();
        RecordSet rs = new RecordSet();
        String tableName = "";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        String mainID = "";
        String Name = "";
        String fpcebs = "0"; //发票超额标识
        String zdbzbs = "1";//招待标准标识
        String bxlx = "";//报销类型 0 其他费用 1 招待费 2特殊情况
        String dept = "";
        String zdfbs = "33";//项目招待费
        String bmid = "1110";//PM-JADE Account
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
            dept = Util.null2String(rs.getString("Dept"));
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
        sql = "delete from uf__fybxjl_sp where rqid="+requestid;
        rs.execute(sql);
        if(count >0) {
            int zcs = 0;
            sql = "select " + count + "+nvl(sum(nvl(cs,0)),0) as zcs from uf__fybxjl_sp where nf='" + year + "' and ry='" + Name + "'";
            rs.execute(sql);
            if(rs.next()){
                zcs = rs.getInt("zcs");
            }
            if(zcs<=3){
                fpcebs = "1";
            }else{
                fpcebs = "2";
            }
            String billid = "";
            sql = "select * from uf__fybxjl_sp where rqid='"+requestid+"' and ry='"+Name+"'";
            rs.execute(sql);
            if(rs.next()){
                billid = Util.null2String(rs.getString("id"));
            }
            if ("".equals(billid)) {
                sql = "insert into uf__fybxjl_sp(nf,ry,rqid,cs) values('" + year + "','" + Name + "','" + requestid + "','" + count + "')";
                rs.execute(sql);
            } else {
                sql = "update uf__fybxjl_sp set cs='" + count + "' where id=" + billid;
                rs.execute(sql);
            }
        }


        count = 0;
        sql = "select count(1) as count from "+tableName+"_dt1 where bxxmnew="+zdfbs+" and (((TO_DATE(EndDate, 'YYYY-MM-DD') - TO_DATE(StartDate, 'YYYY-MM-DD')+1)*150*(nvl(wsrs,0)+nvl(khrs,0)))<CostRMB or (nvl(wsrs,0)/nvl(khrs,0.1)>2)) and mainid="+mainID;
        rs.execute(sql);
        if(rs.next()){
            count = rs.getInt("count");
        }
        if(count>0){
            zdbzbs = "0";
        }


        //
        if(bmid.equals(dept)){//PM-JADE Account
            double amount = 0;
            sql = "select nvl(sum(nvl(CostRMB,0)),0) as amount from "+tableName+"_dt1 where bxxmnew<>'"+zdfbs+"' and mainid="+mainID;
            rs.execute(sql);
            if(rs.next()){
                amount = rs.getDouble("amount");
            }
            if(amount>10000){
                bxlx = "2";
            }
        }
        if("".equals(bxlx)){
            count = 0;
            sql = "select count(1) as count from "+tableName+"_dt1 where bxxmnew='"+zdfbs+"' and mainid="+mainID;
            rs.execute(sql);
            if(rs.next()){
                count = rs.getInt("count");
            }
            if(count>0){
                bxlx = "1";
            }else{
                bxlx = "0";
            }
        }


        sql = "update "+tableName+" set zdbzbs='"+zdbzbs+"',fpcebs='"+fpcebs+"',bxlx='"+bxlx+"' where requestid="+requestid;
        rs.execute(sql);


        return SUCCESS;
    }
}
