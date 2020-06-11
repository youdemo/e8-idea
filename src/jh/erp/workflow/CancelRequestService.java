package jh.erp.workflow;

import weaver.conn.RecordSet;
import weaver.file.FileUpload;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.workflow.request.RequestManager;

/**
 * 流程撤销接口
 * 通过传入requestid，进行流程干预到指定归档节点
 */
public class CancelRequestService {
    public String cancelRequest(String requestid){
        RecordSet rs = new RecordSet();
        BaseBean log = new BaseBean();
        String sql = "";
        String result = "";
        int count = 0;
        int workflowid = 0;
        String currentnodetype = "";
        String creater = "";
        String nodeid = "";
        log.writeLog("CancelRequestService","requestid:"+requestid);
        if("".equals(requestid)){
            log.writeLog("CancelRequestService","result:{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 传入的requestid不能为空\"}");
            return "{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 传入的requestid不能为空\"}";
        }
        sql = "select count(1) as count from workflow_requestbase where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            count = rs.getInt("count");
        }
        if(count <=0){
            log.writeLog("CancelRequestService","result:{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 传入的requestid不存在，请传入正确的requestid\"}");
            return "{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 传入的requestid不存在，请传入正确的requestid\"}";
        }
        sql = "select workflowid,currentnodetype,creater from workflow_requestbase where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            workflowid = rs.getInt("workflowid");
            currentnodetype = Util.null2String(rs.getString("currentnodetype"));
            creater = Util.null2String(rs.getString("creater"));
        }
        if("3".equals(currentnodetype)){
            log.writeLog("CancelRequestService","result:{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 该流程已归档\"}");
            return "{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 该流程已归档\"}";
        }
        sql = "select nodeid from uf_oaerp_cancel_mt where wfid='"+workflowid+"'";
        rs.execute(sql);
        if(rs.next()){
            nodeid = Util.null2String(rs.getString("nodeid"));
        }
        if("".equals(nodeid)){
            log.writeLog("CancelRequestService","result:{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 该流程撤销节点没有配置\"}");
            return "{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 该流程撤销节点没有配置\"}";
        }
        result = cancelRequest(Integer.valueOf(requestid),workflowid,Integer.valueOf(nodeid),creater);
        log.writeLog("CancelRequestService","result:"+result);
        return result;
    }

    /**
     * 流程干预
     * @param requestid
     * @param workflowid
     * @param nextnodeid
     * @param operatorid
     * @return
     */
    private String cancelRequest(int requestid,int workflowid,int nextnodeid,String operatorid){
        //BaseBean log = new BaseBean();
        RecordSet rs = new RecordSet();
        User user = User.getUser(1,0);
        FileUpload fu = null;
        String result = "";
        String workflowtype = "";
        int isbill = -1;
        int formid = -1;
        int nodeid = -1;
        String nodetype = "";
        String submitNodeId = "";
        String requestname = "";
        String requestlevel = "";
        int billid=0;
        String sql = "select workflowtype,isbill,formid from workflow_base where id="+workflowid;
        rs.execute(sql);
        if(rs.next()){
            workflowtype = Util.null2String(rs.getString("workflowtype"));
            isbill = rs.getInt("isbill");
            formid = rs.getInt("formid");
        }
        sql = "select currentnodeid,b.nodetype,a.requestname,a.requestlevel from workflow_requestbase a,workflow_flownode b where a.currentnodeid=b.nodeid and a.requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            nodetype = Util.null2String(rs.getString("nodetype"));
            nodeid = rs.getInt("currentnodeid");
            requestname = Util.null2String(rs.getString("requestname"));
            requestlevel = Util.null2String(rs.getString("requestlevel"));

        }
        sql = "select convert(varchar(20),a.nodeid)+'_'+convert(varchar(20),a.nodetype)+'_'+convert(varchar(20),b.nodeattribute) as submitNodeId from workflow_flownode a,workflow_nodebase b where a.nodeid=b.id and a.nodeid="+nextnodeid;
        rs.execute(sql);
        if(rs.next()){
            submitNodeId = Util.null2String(rs.getString("submitNodeId"));
        }
        sql = "select billid from workflow_form where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            billid = rs.getInt("billid");
        }
//        sql = "select MAX(id) as wfcurrenid from workflow_currentoperator where requestid="+requestid;
//        rs.execute(sql);
//        log.writeLog("testaaa");
//        log.writeLog("requestid:"+requestid+" workflowid:"+workflowid+" workflowtype:"+workflowtype);
//        log.writeLog("formid:"+formid+" isbill:"+isbill+" billid:"+billid+" nodeid:"+nodeid);
//        log.writeLog("nodetype:"+nodetype+" requestname:"+requestname+" requestlevel:"+requestlevel);
//        log.writeLog("submitNodeId:"+submitNodeId+" operatorid:"+operatorid+" user:"+user.getUID());
        RequestManager RequestManager = new RequestManager();
        RequestManager.setIsMultiDoc("") ;
        RequestManager.setSrc("intervenor") ;
        RequestManager.setIscreate("0") ;
        RequestManager.setRequestid(requestid) ;
        RequestManager.setWorkflowid(workflowid) ;
        RequestManager.setWorkflowtype(workflowtype) ;
        RequestManager.setIsremark(-1) ;
        RequestManager.setFormid(formid) ;
        RequestManager.setIsbill(isbill) ;
        RequestManager.setBillid(billid) ;
        RequestManager.setNodeid(nodeid) ;
        RequestManager.setNodetype(nodetype) ;
        RequestManager.setRequestname(requestname) ;
        RequestManager.setRequestlevel(requestlevel) ;
        RequestManager.setRemark("") ;
        //RequestManager.setRequest(fu) ;
        RequestManager.setSubmitNodeId(submitNodeId);//干预节点
        RequestManager.setIntervenorid(operatorid);//人员
        RequestManager.setSignType(0);//非会签
        RequestManager.setIsFromEditDocument("") ;
        RequestManager.setUser(user) ;
        RequestManager.setIsagentCreater(-1);
        RequestManager.setBeAgenter(0);
        RequestManager.setIsPending(-1);
        RequestManager.setRequestKey(0);
        RequestManager.setCanModify(false);
        RequestManager.setCoadsigntype("2");
        RequestManager.setEnableIntervenor(1);//是否启用节点及出口附加操作
        RequestManager.setRemarkLocation("");
        RequestManager.setIsFirstSubmit("");
        RequestManager.setMessageType("0") ;
        RequestManager.setChatsType("0") ;
        boolean savestatus = RequestManager.saveRequestInfo() ;
//        log.writeLog(RequestManager.getMessageType());
//        log.writeLog(RequestManager.getMessagecontent());
//        log.writeLog(RequestManager.getMessageid());
        boolean flowstatus = RequestManager.flowNextNode() ;
        //log.writeLog("flowstatus:"+flowstatus);
        boolean logstatus = RequestManager.saveRequestLog() ;
        //log.writeLog("logstatus:"+logstatus);
        if(savestatus&&flowstatus&&logstatus){
            result = "{\"MSG_TYPE\":\"S\",\"MSG_CONTENT\":\"撤销成功\"}";
        }else{
            result = "{\"MSG_TYPE\":\"E\",\"MSG_CONTENT\":\"撤销失败 "+RequestManager.getMessagecontent()+"\"}";
        }

        return result;
    }
}
