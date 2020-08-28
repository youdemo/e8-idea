package htwb.workflow.wh;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * createby jianyong.tang
 * createTime 2020/8/13 14:23
 * version v1
 * desc HTWB-招标&采购成果文件校审 生成文号
 */
public class UpdateWhForCg implements Action {
    @Override
    public String execute(RequestInfo info) {
        TransUtil tu = new TransUtil();
        RecordSet rs = new RecordSet();
        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();
        String tableName = tu.getTableName(workflowID);
        String htbh = "";//合同编号
        String ejeslx = "";//二校二审类型
        String wh = "";//文号
        String lxms = "";
        String sql = "select htbh,ejeslx,wh from "+tableName+" where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            htbh = Util.null2String(rs.getString("htbh"));
            ejeslx = Util.null2String(rs.getString("ejeslx"));
            wh = Util.null2String(rs.getString("wh"));
        }
        if("".equals(wh)){
            if(!"".equals(htbh)||!"".equals(ejeslx)){
                sql = "select selectname from workflow_billfield t1, workflow_bill t2,workflow_selectitem t3 where t1.billid=t2.id and t3.fieldid=t1.id  and t2.tablename='"+tableName+"' and t1.fieldname='ejeslx' and t3.selectvalue='"+ejeslx+"'";
                rs.execute(sql);
                if(rs.next()){
                    lxms = Util.null2String(rs.getString("selectname"));
                }
                wh = htbh+lxms+getFlowNumYear(htbh,4);
                sql = "update "+tableName+" set wh='"+wh+"' where requestid="+requestid;
                rs.execute(sql);
            }
        }

        return SUCCESS;
    }

    public  String getFlowNumYear(String htbh, int ls) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sf.format(new Date());
        RecordSet rs = new RecordSet();
        String modeid = getModeId("uf_wfwh_cg");
        int nextNo = 1;
        int count = 0;
        String sql = "select count(1) as count from uf_wfwh_cg where hth='" + htbh + "'";
        rs.execute(sql);
        if (rs.next()) {
            count = rs.getInt("count");
        }

        if (count > 0) {
            sql = "select lsh+1 as seqnum from uf_wfwh_cg where hth='" + htbh + "'";
            rs.execute(sql);
            if (rs.next()) {
                nextNo = rs.getInt("seqnum");
            }

            sql = "update uf_wfwh_cg set lsh=lsh+1 where hth='" + htbh + "'";
            rs.execute(sql);
        } else {
            sql="insert into uf_wfwh_cg (hth,lsh,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid)"
                    + " values('"+htbh+ "',1,'"+now+"','1','0','"+modeid+"')";
            rs.execute(sql);
            int billid = 0;
            sql = "select id from uf_wfwh_cg where hth='" + htbh + "'";
            rs.execute(sql);
            if(rs.next()) {
                billid = rs.getInt("id");
            }
            if(billid > 0) {
                ModeRightInfo ModeRightInfo = new ModeRightInfo();
                ModeRightInfo.editModeDataShare(1,
                        Util.getIntValue(modeid),
                        billid);
            }
        }


        return getStrNum(nextNo, ls);
    }

    public String getModeId(String tableName) {
        RecordSet rs = new RecordSet();
        String formid = "";
        String modeid = "";
        String sql = "select id from workflow_bill where tablename='"
                + tableName + "'";
        rs.execute(sql);
        if (rs.next()) {
            formid = Util.null2String(rs.getString("id"));
        }
        sql = "select id from modeinfo where  formid=" + formid;
        rs.execute(sql);
        if (rs.next()) {
            modeid = Util.null2String(rs.getString("id"));
        }
        return modeid;
    }

    public String getStrNum(int num,int len){
        String buff = String.valueOf(num);
        int max = len - buff.length();
        for(int index = 0; index < max;index++){
            buff = "0" + buff;
        }
        return buff;
    }
}
