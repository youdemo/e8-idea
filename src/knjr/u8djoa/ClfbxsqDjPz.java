package knjr.u8djoa;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.concurrent.TimeUnit;

public class ClfbxsqDjPz implements Action {
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
            log.writeLog("=============ClfbxsqDjPz Start===================");
            sql = " select a.yggh,a.sqbm as reqdept,a.zdrgh,b.creditsubjcode,b.debitsubjcode,a.nf,a.paydate,b.debitmt,b.creditmt,b.zy as zyyt from "+tableName+" a , "+tableName+"_dt2 b where a.requestid="+requestId+" and a.id=b.mainid";
            String url = ScPz.scpz(sql);
            new BaseBean().writeLog("ClfbxsqDjPz url: "+url);
            if (url != null) {
                TimeUnit.SECONDS.sleep(3);
                HttpClientResult result = HttpClientUtils.doGet(url);
                String content = result.getContent();
                new BaseBean().writeLog("ClfbxsqDjPz content: "+content);
                JSONObject parse = (JSONObject) JSONObject.parse(content);
                String errcode = (String) parse.get("errcode");
                if("0".equals(errcode)){
                    log.writeLog("==========ClfbxsqDjPz===U8接口调用成功===================");
                    return SUCCESS;
                }else{
                    return FAILURE_AND_CONTINUE;
                }
            }else{
                log.writeLog("==========ClfbxsqDjPz===U8接口调用失败===url null================");
                return FAILURE_AND_CONTINUE;
            }
        } catch (Exception e) {
            log.writeLog(e);
            return FAILURE_AND_CONTINUE;
        }
    }
}
