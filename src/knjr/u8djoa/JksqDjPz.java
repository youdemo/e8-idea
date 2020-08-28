package knjr.u8djoa;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.concurrent.TimeUnit;


public class JksqDjPz implements Action {

    @Override
    public String execute(RequestInfo info) {
        String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
        String requestId = info.getRequestid();//获取当前流程的requestid
        RecordSet rs = new RecordSet();
        String tableName = "";
        String mainId = "";
        BaseBean log = new BaseBean();
        //获取流程主表表面
        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= " + workflowID
                + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        try {
            log.writeLog("=============JksqDjPz Start===================");
            sql = "select yggh,reqdept,zdrgh,creditsubj,debitsubj,nf,paydate,debitmt,creditmt,zyyt from "+tableName+" where requestid="+requestId;
            String url = ScPz.scpz(sql);
            log.writeLog("JksqDjPz url: "+url);
            if (url!=null) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    new BaseBean().writeLog(e);
                }
                HttpClientResult result = null;
                try {
                     result = HttpClientUtils.doGet(url);
                } catch (Exception e) {
                    new BaseBean().writeLog(e);
                }
                String content = result.getContent();
                new BaseBean().writeLog("JksqDjPz content: "+content);
                JSONObject parse = (JSONObject) JSONObject.parse(content);
                String errcode = (String) parse.get("errcode");
                if("0".equals(errcode)){
                    log.writeLog("==========JksqDjPz===U8接口调用成功===================");
                    return SUCCESS;
                }else {
                    info.getRequestManager().setMessage("errcode");
                    return FAILURE_AND_CONTINUE;
                }
            }else{
                log.writeLog("==========JksqDjPz===U8接口调用失败===================");
                return FAILURE_AND_CONTINUE;
            }
        } catch (Exception e) {
            log.writeLog(e);
            return FAILURE_AND_CONTINUE;
        }
    }
}
