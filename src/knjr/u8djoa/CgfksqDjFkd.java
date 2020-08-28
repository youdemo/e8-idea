package knjr.u8djoa;

import com.alibaba.fastjson.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 *采购付款申请测试---集成到付款单
 */
public class CgfksqDjFkd implements Action {
    @Override
    public String execute(RequestInfo info) {
        String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
        String requestId = info.getRequestid();//获取当前流程的requestid
        RecordSet rs = new RecordSet();
        String tableName = "";
        String mainId = "";
        //获取流程主表表面
        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= " + workflowID
                + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        sql = " select a.flowno,a.reqdate,b.cCusVen,a.cSSCode,a.bbje,a.ybje,a.Jskmcode,a.Bmcode,a.Lrr,a.Ywycode,a.zy from "+ tableName +" a , "+ tableName +"_dt1 b where a.requestid="+requestId+" and a.id=b.mainid";

        String url = ScFkd.scfkd(sql);
        new BaseBean().writeLog("CgfksqDjFkd url: "+url);
        if (url!=null) {
            HttpClientResult result = null;
            try {
                result = HttpClientUtils.doGet(url);
            } catch (Exception e) {
                new BaseBean().writeLog(e);
            }
            String content = result.getContent();
            new BaseBean().writeLog("CgfksqDjFkd content: "+content);
            JSONObject parse = (JSONObject) JSONObject.parse(content);
            String errcode = (String) parse.get("errcode");
            if("0".equals(errcode)){
                return SUCCESS;
            }else{
                return FAILURE_AND_CONTINUE;
            }
        }else{
            return FAILURE_AND_CONTINUE;
        }
    }
}
