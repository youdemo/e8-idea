package gvo.e9sj;

/*
 * Created on 2006-05-18
 * Copyright (c) 2001-2006 泛微软件
 * 泛微协同商务系统，版权所有。
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import weaver.conn.RecordSet;
import weaver.cpt.capital.CapitalComInfo;
import weaver.crm.Maint.CustomerInfoComInfo;
import weaver.docs.docs.DocComInfo;
import weaver.docs.docs.DocImageManager;
import weaver.docs.senddoc.DocReceiveUnitComInfo;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.job.JobTitlesComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.browser.Browser;
import weaver.interfaces.workflow.browser.BrowserBean;
import weaver.proj.Maint.ProjectInfoComInfo;
import weaver.rtx.RTXConfig;
import weaver.system.RequestDefaultComInfo;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.field.BrowserComInfo;
import weaver.workflow.monitor.Monitor;
import weaver.workflow.monitor.MonitorDTO;
import weaver.workflow.request.MailAndMessage;
import weaver.workflow.request.RequestCheckUser;
import weaver.workflow.request.ResourceConditionManager;
import weaver.workflow.request.WFForwardManager;
import weaver.workflow.request.WFLinkInfo;
import weaver.workflow.workflow.WfForceDrawBack;
import weaver.workflow.workflow.WfForceOver;
import weaver.workflow.workflow.WfFunctionManageUtil;
import weaver.workflow.workflow.WorkflowComInfo;
import weaver.workflow.workflow.WorkflowRequestComInfo;
import weaver.workflow.request.todo.RequestUtil;
import com.weaver.cssRenderHandler.JsonUtils;
/**
 * 流程列表页数据库值和显示名的转化
 * @author xwj
 * @version10.
 */
public class WorkFlowTransMethodE8 extends BaseBean {
    private ResourceComInfo rc = null;
    private CustomerInfoComInfo cci = null;
    private WorkflowComInfo wf = null;
    private WorkflowComInfo WorkflowComInfo=null;
    private DepartmentComInfo DepartmentComInfo1=null;
    private JobTitlesComInfo JobTitlesComInfo1=null;
    private ProjectInfoComInfo ProjectInfoComInfo1=null;
    private  DocComInfo DocComInfo1=null;
    private  BrowserComInfo   BrowserComInfo=null;
    private DocImageManager DocImageManager=null;
    private WorkflowRequestComInfo WorkflowRequestComInfo1=null;
    private CapitalComInfo CapitalComInfo1=null;
    private RequestDefaultComInfo RequestDefaultComInfo=null;
    private ResourceConditionManager rcm=null;
    private DocReceiveUnitComInfo duc=null;
    private RequestUtil ru = null ;
    public int count = 0;
    public WorkFlowTransMethodE8() {
        try {
            cci = new CustomerInfoComInfo();
            rc = new ResourceComInfo();
            wf=new WorkflowComInfo();
            RequestDefaultComInfo=new RequestDefaultComInfo();
            DocComInfo1=new  DocComInfo();
            ProjectInfoComInfo1=new ProjectInfoComInfo();
            BrowserComInfo =new  BrowserComInfo();
            DepartmentComInfo1=new DepartmentComInfo();
            JobTitlesComInfo1=new JobTitlesComInfo();
            DocImageManager=new DocImageManager();
            WorkflowRequestComInfo1=new WorkflowRequestComInfo();
            CapitalComInfo1=new CapitalComInfo();
            rcm=new ResourceConditionManager();
            duc=new DocReceiveUnitComInfo();
            ru = new RequestUtil();
        } catch (Exception ex) {
            writeLog(ex);
        }
    }

    /**
     * 判断当前请求节点设置了必须新增明细后，明细中有没有数据
     * @param workflowid
     * @param requestid
     * @return 返回true-表示明细有数据，或者没有设置必须新增明细；
     * @return 返回false-表示明细没有数据
     */
    private boolean haveDetailMustAdd(String workflowid,int requestid) {
        boolean flag = true;
        String sql = "";
        int isbill = 0;
        int formid = 0;
        int nodeid = 0;
        int groupid= 0;
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        sql = "select formid,isbill from workflow_base where id=" + workflowid;
        rs.executeSql(sql);
        if (rs.next()){
            formid = rs.getInt(1);
            isbill = rs.getInt(2);
        }
        sql = "select currentnodeid from workflow_requestbase where requestid=" + requestid;
        rs.executeSql(sql);
        if (rs.next()) {
            nodeid = rs.getInt(1);
        }
        //检查当前节点 是否设置了 必须新增明细
        sql = "select groupid from workflow_NodeFormGroup where isneed=1 and nodeid=" + nodeid;
        rs.executeSql(sql);
        while (rs.next()){
            groupid = rs.getInt(1);
            //判断是新表单还是老表单，明细表不一样
            if(formid > 0 && isbill == 0){//老表单
                //检查明细是否有数据
                sql = "select id from workflow_formdetail where requestid=" + requestid + " and groupid=" + groupid;
                rs1.executeSql(sql);
                if (rs1.next()) {

                }else{
                    return false;
                }
            }
            if(formid < 0 && isbill == 1){//新表单
                int dt = groupid + 1;
                int mainT = -formid;
                //检查明细是否有数据
                sql = "select id from formtable_main_" + mainT + "_dt"+ dt +" where mainid=(select id from formtable_main_" + mainT + " where requestid=" + requestid + ")";
                rs1.executeSql(sql);
                if (rs1.next()) {

                }else{
                    return false;
                }
            }
        }
        return flag;
    }

    /**
     * 重写，加上当前节点id
     * 判断当前请求节点设置了必须新增明细后，明细中有没有数据
     * @param workflowid
     * @param requestid
     * @param nodeid
     * @return 返回true-表示明细有数据，或者没有设置必须新增明细；
     * @return 返回false-表示明细没有数据
     */
    private boolean haveDetailMustAdd(String workflowid,int requestid,int nodeid) {
        boolean flag = true;
        String sql = "";
        String checksql = "";
        int isbill = 0;
        int formid = 0;
        int groupid= 0;
        int checks = 0;
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        sql = "select formid,isbill from workflow_base where id=" + workflowid;
        rs.executeSql(sql);
        if (rs.next()){
            formid = rs.getInt(1);
            isbill = rs.getInt(2);
        }
        //修改，主要是针对客户复制了明细表，然后直接删除了明细字段
        if(formid > 0 && isbill == 0){ //老表单
            checksql = "SELECT count(fieldid) FROM workflow_formfield WHERE formid="+formid+" and isdetail is not null";
            rs.executeSql(checksql);
            if (rs.next()){
                checks = rs.getInt(1);
            }
            if (checks>0) {
                //检查当前节点 是否设置了 必须新增明细
                sql = "select groupid from workflow_NodeFormGroup where isneed=1 and nodeid=" + nodeid;
                rs1.executeSql(sql);
                while (rs1.next()){
                    groupid = rs1.getInt(1);
                    //判断是新表单还是老表单，明细表不一样
                    if(formid > 0 && isbill == 0){//老表单
                        //检查明细是否有数据
                        sql = "select id from workflow_formdetail where requestid=" + requestid + " and groupid=" + groupid;
                        rs2.executeSql(sql);
                        if (rs2.next()) {

                        }else{
                            return false;
                        }
                    }
                    if(formid < 0 && isbill == 1){//新表单
                        int dt = groupid + 1;
                        int mainT = -formid;
                        //检查明细是否有数据
                        sql = "select id from formtable_main_" + mainT + "_dt"+ dt +" where mainid=(select id from formtable_main_" + mainT + " where requestid=" + requestid + ")";
                        rs2.executeSql(sql);
                        if (rs2.next()) {

                        }else{
                            return false;
                        }
                    }
                }
            }else{

            }
        }
        if(formid < 0 && isbill == 1){//新表单
            checksql = "select COUNT(id) from workflow_billfield where billid="+formid+"  AND viewtype =1";
            rs.executeSql(checksql);
            if (rs.next()){
                checks = rs.getInt(1);
            }
            if (checks>0) {
                //检查当前节点 是否设置了 必须新增明细
                sql = "select groupid from workflow_NodeFormGroup where isneed=1 and nodeid=" + nodeid;
                rs1.executeSql(sql);
                while (rs1.next()){
                    groupid = rs1.getInt(1);
                    //判断是新表单还是老表单，明细表不一样
                    if(formid > 0 && isbill == 0){//老表单
                        //检查明细是否有数据
                        sql = "select id from workflow_formdetail where requestid=" + requestid + " and groupid=" + groupid;
                        rs2.executeSql(sql);
                        if (rs2.next()) {

                        }else{
                            return false;
                        }
                    }
                    if(formid < 0 && isbill == 1){//新表单
                        int dt = groupid + 1;
                        int mainT = -formid;
                        //检查明细是否有数据
                        sql = "select id from formtable_main_" + mainT + "_dt"+ dt +" where mainid=(select id from formtable_main_" + mainT + " where requestid=" + requestid + ")";
                        rs2.executeSql(sql);
                        if (rs2.next()) {

                        }else{
                            return false;
                        }
                    }
                }
            }else{

            }
        }
        return flag;
    }

    /**
     * 是否在列表显示选择筐 待审批显示复选框
     * @return true/false
     */
    public String getOpUserResultCheckBox(String request){
        String[] tempStr = Util.splitString(request, "+");
        String requestid = Util.null2String(tempStr[0]);
        String userid = Util.null2String(tempStr[1]);
        RecordSet rs=new RecordSet();
        rs.executeSql("select distinct userid from workflow_currentoperator where (isremark in ('0','1') or (isremark='4' and viewtype=0))  and requestid ="+requestid+" and userid="+userid);
        return ""+rs.next();
    }
    /**
     * 把流程创建人由ID转为显示中文名
     * @param id 创建人ID
     * @param type 内部/外部
     * @return 显示中文名
     */
    public String getWFSearchResultName(String id, String type) {
        String returnStr = "";
        if ("1".equals(type)) { //外部
            returnStr =
                    "<a href=\"javaScript:openFullWindowHaveBar(\'/CRM/data/ViewCustomer.jsp?CustomerID="
                            + id
                            + "\')\">"
                            + cci.getCustomerInfoname(id)
                            + "</a>";
        } else { //内部
            returnStr =
                    "<a href=\"javaScript:openhrm("
                            + id
                            + ");\" onclick='pointerXY(event);'>"
                            + rc.getResourcename(id)
                            + "</a>";
        }
        return returnStr;
    }

    /**
     * 把流程名称和流程ID作为流程名称一起显示
     * @param requestname 流程名称
     * @param para1
     * @return 显示名
     */
    public String getWFSearchResultFlowName(String requestname, String para1) {
        String returnStr = "";
        String[] tempStr = Util.splitString(para1, "+");
        String requestid = Util.null2String(tempStr[0]);
        int isview=Util.getIntValue(tempStr[1],0);

        int workflowid = Util.getIntValue(Util.null2String(tempStr[2]),0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[3]),7);
        int isbill=0;
        int formid=0;
        RecordSet rs = new RecordSet();
        ////根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
        if (rs.next())
        {
            formid=rs.getInt(1);
            isbill=rs.getInt(2);

        }
        MailAndMessage mailTitle=new MailAndMessage();
        String titles=mailTitle.getTitle(Util.getIntValue(requestid,-1),workflowid,formid,userlang,isbill);
        if (!titles.equals("")){
            requestname=requestname+"<B>（"+titles+"）</B>";
        }
        returnStr=requestname+"("+requestid+")";
        if(isview==1){
            returnStr="<a href=\"javaScript:openFullWindowHaveBar(\'/workflow/request/ViewRequest.jsp?requestid="+ requestid+ "&ismonitor=1\')\">"+ returnStr+ "</a>";
        }
        return returnStr;
    }
    /**
     * 把流程紧急程度由代码转为显示中文名
     * @param urgencyType 流程紧急程度
     * @return 显示中文名
     */
    public String getWFSearchResultUrgencyDegree(String urgencyType,String userLanguage) {
        String returnStr = "";
        if ("0".equals(urgencyType)) {
            returnStr = SystemEnv.getHtmlLabelName(225,Integer.parseInt(userLanguage));
        } else if ("1".equals(urgencyType)) {
            returnStr = SystemEnv.getHtmlLabelName(15533,Integer.parseInt(userLanguage));
        } else if ("2".equals(urgencyType)) {
            returnStr = SystemEnv.getHtmlLabelName(2087,Integer.parseInt(userLanguage));
        } else {

        }
        return returnStr;
    }
    /**
     * 格式化显示创建时间
     * @param day 日期
     * @param time 时间
     * @return 日期+ " " + 时间
     */
    public String getWFSearchResultCreateTime(String day, String time) {
        return day + " " + time;
    }
    /**
     * 是否在列表显示选择筐
     * @param workflowid 流程ID
     * @return true/false
     */
    public String getWFSearchResultCheckBox(String workflowid) {
        String[] tempStr = Util.splitString(workflowid, "+");
        String wfid = Util.null2String(tempStr[0]);
        String isremark = Util.null2String(tempStr[1]);
        int requestid = Util.getIntValue(tempStr[2]);
        int nodeid = Util.getIntValue(tempStr[3]);
        int userid = Util.getIntValue(tempStr[4]);
        int Forwardid = 0;
        int takisremark = -1;
        boolean canSubmit = true;
        RecordSet RecordSet = new RecordSet();
        // System.out.println("---266--sql-"+"select
        // isremark,isreminded,preisremark,id,groupdetailid,nodeid from
        // workflow_currentoperator where requestid="+requestid+" and
        // userid="+userid+" and nodeid="+nodeid+" order by isremark,id");
        if ("1".equals(isremark)) {
            RecordSet
                    .executeSql("select takisremark,isremark,isreminded,preisremark,id,groupdetailid,nodeid from workflow_currentoperator where requestid="
                            + requestid
                            + " and userid="
                            + userid
                            + " and nodeid=" + nodeid + " order by isremark,id");
            while (RecordSet.next()) {
                Forwardid = Util.getIntValue(RecordSet.getString("id"));
                takisremark = Util.getIntValue(RecordSet
                        .getString("takisremark"));
            }

            String wfSQL = "select * from workflow_Forward where requestid="
                    + requestid + " and BeForwardid=" + Forwardid;
            RecordSet.executeSql(wfSQL);
            if (RecordSet.next()) {
                String IsFromWFRemark_T = Util.null2String(RecordSet
                        .getString("IsFromWFRemark")); // 待办提交后被转发人是否可提交意见
                String IsSubmitedOpinion = Util.null2String(RecordSet
                        .getString("IsSubmitedOpinion")); // 待办提交后被转发人是否可提交意见
                String IsBeForwardTodo = Util.null2String(RecordSet
                        .getString("IsBeForwardTodo")); // 待办可转发
                String IsBeForwardSubmitAlready = Util.null2String(RecordSet
                        .getString("IsBeForwardSubmitAlready")); // 允许已办被转发人可提交意见
                String IsBeForwardAlready = Util.null2String(RecordSet
                        .getString("IsBeForwardAlready")); // 已办被转发人可转发
                String IsBeForwardSubmitNotaries = Util.null2String(RecordSet
                        .getString("IsBeForwardSubmitNotaries")); // 允许办结被转发人可提交意见
                String IsBeForward = Util.null2String(RecordSet
                        .getString("IsBeForward")); // 办结被转发人是否可转发
                if (takisremark != 2) {
                    if (("0".equals(IsFromWFRemark_T) && "1"
                            .equals(IsSubmitedOpinion))
                            || ("1".equals(IsFromWFRemark_T) && "1"
                            .equals(IsBeForwardSubmitAlready))
                            || ("2".equals(IsFromWFRemark_T) && "1"
                            .equals(IsBeForwardSubmitNotaries))) {
                        canSubmit = true;
                    } else {
                        canSubmit = false;
                    }
                }
                if(takisremark == 2){
                    canSubmit = true;
                }
            }
        }
        String flag = "false";
        if (isremark.equals("0")) {
            if ("1".equals(getWFMultiSubmit(wfid,nodeid,isremark,takisremark)))
                flag = "true";
            if (flag.equals("true")) {
                if (flag.equals("true") && haveMustInput(requestid,nodeid)) {
                    flag = "false";
                } else {
                    if (haveDetailMustAdd(wfid, requestid,nodeid)) {
                        flag = "true";
                    } else {
                        flag = "false";
                    }
                }
            }
        } else if (isremark.equals("9")) {
            if ("1".equals(getWFMultiSubmit(wfid,nodeid,isremark,takisremark))) {
                flag = "true";
            }
        } else if (isremark.equals("1")) {
            if (canSubmit && "1".equals(getWFMultiSubmit(wfid,nodeid,isremark,takisremark))) {

                flag = "true";
            } else {

                flag = "false";
            }
        }

        return flag;
    }


    /**
     * 是否在列表显示选择筐,批量标记为已读
     * @param otherInfo 流程ID
     * @return true/false
     */
    public String getWFSearchRstCkBoxForMsg(String otherInfo) {
        String isread = "false";
        boolean isprocessed;
        String[] otherInfos = Util.splitString(otherInfo, "+");
        String viewtype = Util.null2String(otherInfos[0]);
        String isremark_tmp = Util.null2String(otherInfos[1]);
        String isprocessed_tmp = Util.null2String(otherInfos[2]);
        if((isremark_tmp.equals("0") && !isprocessed_tmp.equals("1")) || isremark_tmp.equals("5")){
            isprocessed=true;
        }else{
            isprocessed=false;
        }
        if (viewtype.equals("0")) {
            //if(isprocessed){
            isread = "true";
            //}
        }
        return isread;
    }

    /**
     * 流程操作按钮判断
     * add by Dracula @2014-1-15
     * @param otherInfo
     * @return
     *
     */
    public List<String> getWFSearchResultOperation(String requestid,String otherInfo,String userInfo)
    {
        String showMarkReadBtn = "false";	//显示标记为已读
        String showRansmitBtn = "false";	//显示转发按钮
        //String showtakingOpBtn = "false";	//显示转办按钮
        //String showHandlForBtn = "false";	//显示意见征询按钮
        String showPrintBtn = "false";		//显示打印按钮
        String showNewflowBtn = "false";	//显示新建流程按钮
        String showNewMessageBtn = "false";	//显示新建短信按钮
        String showFormLogBtn = "false";	//显示表单日志按钮

        boolean isread = false;
        boolean canransmit = false;
        //boolean cantakingOp = false;
        //boolean canHandlFor = false;
        boolean canprint = true;		//打印是无论如何都会出现，不管有没有打印模板
        boolean cannewflow = false;
        boolean cannewmessage = false;
        boolean canseelog = false;
        List<String> result = new ArrayList<String>();
        String currentType = getCurrentType(requestid);

        String[] otherInfos = Util.splitString(otherInfo, "+");
        String viewtype = Util.null2String(otherInfos[0]);
        String isremark_tmp = Util.null2String(otherInfos[1]);
        String isprocessed_tmp = Util.null2String(otherInfos[2]);
        String nodeid_tmp = Util.null2String(otherInfos[3]);
        String workflowid_tmp = Util.null2String(otherInfos[4]);

        String[] userInfos = Util.splitString(userInfo, "_");
        String userid_tmp = Util.null2String(userInfos[0]);
        String usertype_tmp = Util.null2String(userInfos[1]);

        String haswfrm = "";//是否使用新建流程按钮
        String hassmsrm = "";//是否使用新建短信按钮
        //判断是否已读===========
        boolean isprocessed;
        if((isremark_tmp.equals("0") && !isprocessed_tmp.equals("1")) || isremark_tmp.equals("5")){
            isprocessed=true;
        }else{
            isprocessed=false;
        }
        //未超时的 & 未超时
        if (viewtype.equals("0")) {
            //if(isprocessed){
//        		BDNew//新的
            isread = true;
            //}
        }
        //其他按钮逻辑查询
        String isremarkForRM = "";
        int preisremark = -1;
        RecordSet RecordSet = new RecordSet();
        RecordSet _rs = new RecordSet();
        int wfcurtid = 0;
        int wfcurnodeid = 0;
        int takisremark= -1;
        int handleforwardid = -1;
        String wfcurnodetype = "";
        _rs.executeSql("select handleforwardid,takisremark,isremark,isreminded,preisremark,id,groupdetailid,nodeid,(CASE WHEN isremark=9 THEN '7.5' ELSE isremark END) orderisremark from workflow_currentoperator where requestid="+requestid+" and userid in ("+userid_tmp+") and usertype="+usertype_tmp+" order by orderisremark,id ");
        while(_rs.next()) {
            String isremark = Util.null2String(_rs.getString("isremark")) ;
            wfcurtid = Util.getIntValue(Util.null2String(_rs.getString("id")));
            isremarkForRM = isremark;
            preisremark=Util.getIntValue(_rs.getString("preisremark"),0) ;
            takisremark=Util.getIntValue(_rs.getString("takisremark"),0) ;
            handleforwardid=Util.getIntValue(_rs.getString("handleforwardid"),-1) ;
            int tmpnodeid=Util.getIntValue(_rs.getString("nodeid"));

            if(isremark.equals("1") || isremark.equals("9")) {
                wfcurnodeid=tmpnodeid;
                WFLinkInfo wfLinkInfo = new WFLinkInfo();
                wfcurnodetype=wfLinkInfo.getNodeType(wfcurnodeid);
                break;
            }

            /*
            if( isremark.equals("1")||isremark.equals("5") || isremark.equals("7")|| isremark.equals("9") ||(isremark.equals("0")  && !currentType.equals("3")) ) {
                  wfcurnodeid=tmpnodeid;
                  WFLinkInfo wfLinkInfo = new WFLinkInfo();
                  wfcurnodetype=wfLinkInfo.getNodeType(wfcurnodeid);  
                  break;
            }
            if(isremark.equals("8")){
                break;
            }
            */
        }
        _rs.executeSql("select * from workflow_requestbase where requestid="+requestid);
        while(_rs.next()) {
            wfcurnodetype = Util.null2String(_rs.getString("currentnodetype")) ;

        }

        WFForwardManager wfmange = new WFForwardManager();
        wfmange.init();
        wfmange.setWorkflowid(Util.getIntValue(workflowid_tmp, 0));
        wfmange.setNodeid(Util.getIntValue(nodeid_tmp));
        wfmange.setRequestid(Util.getIntValue(requestid, 0));
        wfmange.setIsremark(isremark_tmp);
        wfmange.setBeForwardid(wfcurtid);
        wfmange.getWFNodeInfo();
        //转发=============
        String IsPendingForward=Util.null2String(wfmange.getIsPendingForward());//允许代办事宜转发
        String IsBeForwardTodo=Util.null2String(wfmange.getIsBeForwardTodo());
        String IsBeForwardSubmitAlready=Util.null2String(wfmange.getIsBeForwardSubmitAlready());
        String IsBeForwardSubmitNotaries=Util.null2String(wfmange.getIsBeForwardSubmitNotaries());
        String IsFromWFRemark_T=Util.null2String(wfmange.getIsFromWFRemark());
        String IsBeForwardAlready=Util.null2String(wfmange.getIsBeForwardAlready());  // 已办转发
        String IsAlreadyForward=Util.null2String(wfmange.getIsAlreadyForward());  // 已办转发
        String IsSubmitForward=Util.null2String(wfmange.getIsSubmitForward());  // 归档转发
        String IsTakingOpinions=Util.null2String(wfmange.getIsTakingOpinions());
        String IsHandleForward=Util.null2String(wfmange.getIsHandleForward());
        String IsBeForward=Util.null2String(wfmange.getIsBeForward());


        boolean canForwd = false;
        if(isremark_tmp.equals("1") || isremark_tmp.equals("9"))
        {
            if(("0".equals(IsFromWFRemark_T) && "1".equals(IsBeForwardTodo)) || "1".equals(IsFromWFRemark_T) && "1".equals(IsBeForwardAlready) || ("2".equals(IsFromWFRemark_T) && "1".equals(IsBeForward)))
                canForwd = true;
            if((isremark_tmp.equals("1")&& canForwd)||(isremark_tmp.equals("9")&&IsPendingForward.equals("1"))){
                canransmit = true;
            }
        }
        if(IsPendingForward.equals("1") && (!isremark_tmp.equals("2") && !isremark_tmp.equals("4")) && takisremark!=-2){
            canransmit = true;
        }

        if(IsAlreadyForward.equals("1") && takisremark==-2 && isremark_tmp.equals("0")){
            canransmit = true;

        }
        if(!"3".equals(wfcurnodetype) && IsAlreadyForward.equals("1") && isremark_tmp.equals("2") && (preisremark==0 || preisremark==8 || preisremark==9 || (preisremark==1 && takisremark==2))){
            canransmit = true;
        }
        if("3".equals(wfcurnodetype) && IsSubmitForward.equals("1") && (isremark_tmp.equals("2") ||  isremark_tmp.equals("4")) && (preisremark==0 || preisremark==8 || preisremark==9 || (preisremark==1 && takisremark==2))){
            canransmit = true;
        }

	/*	  if(IsTakingOpinions.equals("1") && isremark_tmp.equals("0")){
        	cantakingOp = true;
    	}
		  if(IsHandleForward.equals("1")  && isremark_tmp.equals("0")){
        	canHandlFor = true;
    	}
*/
        if(handleforwardid<0 &&  takisremark!=2 &&preisremark==1 && isremark_tmp.equals("2") && otherInfos.length>5){//是否是被转发 已办
            int Forwardid = 0;

            RecordSet.executeSql("select isremark,isreminded,preisremark,id,groupdetailid,nodeid from workflow_currentoperator where requestid="+requestid+" and userid="+userid_tmp+" and usertype="+usertype_tmp+" order by isremark,id");
            while(RecordSet.next())	{
                Forwardid=Util.getIntValue(RecordSet.getString("id"));
            }
            String wfSQL="select * from workflow_Forward where requestid="+requestid+" and BeForwardid="+Forwardid;
            RecordSet.executeSql(wfSQL);
            if(RecordSet.next())	{
                IsFromWFRemark_T=Util.null2String(RecordSet.getString("IsFromWFRemark"));     //待办提交后被转发人是否可提交意见
//IsSubmitedOpinion=Util.null2String(RecordSet.getString("IsSubmitedOpinion"));     //待办提交后被转发人是否可提交意见
                IsBeForwardTodo=Util.null2String(RecordSet.getString("IsBeForwardTodo"));    //待办可转发

//IsBeForwardSubmitAlready =Util.null2String(RecordSet.getString("IsBeForwardSubmitAlready"));    //允许已办被转发人可提交意见

                IsBeForwardAlready =Util.null2String(RecordSet.getString("IsBeForwardAlready"));		//已办被转发人可转发

//IsBeForwardSubmitNotaries =Util.null2String(RecordSet.getString("IsBeForwardSubmitNotaries"));    //允许办结被转发人可提交意见

                IsBeForward=Util.null2String(RecordSet.getString("IsBeForward"));            //办结被转发人是否可转发

            }
            if(("0".equals(IsFromWFRemark_T) && "1".equals(IsBeForwardTodo)) || "1".equals(IsFromWFRemark_T) && "1".equals(IsBeForwardAlready) || ("2".equals(IsFromWFRemark_T) && "1".equals(IsBeForward)))
                canForwd = true;
            if((preisremark==1 && canForwd)){
                canransmit = true;
            }
        }
        /*
         * 转发功能补充，前面逻辑不改动，增加查看已办，办结事宜时，分页控件是否显示转发按钮
         */
        if(preisremark!=1 && !isremark_tmp.equals("1") && !isremark_tmp.equals("2") &&otherInfos.length>5){
            String scope=Util.null2String(otherInfos[5]);//页面是查看待办，已办，还是办结
            String IsAreadyForward=Util.null2String(wfmange.getIsAlreadyForward());//允许已办事宜转发
            //String IsSubmitForward=Util.null2String(wfmange.getIsSubmitForward());//允许办结事宜转发
		/*	if(scope.equals("doing")&&IsTakingOpinions.equals("1")){
				cantakingOp = true;
			}else{
				cantakingOp = false;
			}
			if(scope.equals("doing")&&IsHandleForward.equals("1")){
				canHandlFor = true;
			}else{
				canHandlFor = false;
			}*/
            if((scope.equals("done")&&IsAreadyForward.equals("1") && !"3".equals(wfcurnodetype))
                    ||IsSubmitForward.equals("1") && "3".equals(wfcurnodetype)
                    ||(scope.equals("doing")&&IsPendingForward.equals("1")) || canForwd){
                canransmit=true;
            }else{
                canransmit=false;
            }
        }




        if(handleforwardid<0 &&  takisremark!=2 && isremark_tmp.equals("1") && otherInfos.length>5){//是否是被转发
            //IsBeForwardTodo  //待办被转发人可转发
            //IsBeForwardAlready //已办被转发人可转发
            //IsBeForward //是否允许已办及办结事宜转发
			/*if(scope.equals("doing")&&IsTakingOpinions.equals("1")){
				cantakingOp = true;
			}else{
				cantakingOp = false;
			}
			if(scope.equals("doing")&&IsHandleForward.equals("1")){
				canHandlFor = true;
			}else{
				canHandlFor = false;
			}*/
            if(canForwd){
                canransmit=true;
            }else{
                canransmit=false;
            }
        }
        //==============
        //新建流程、新建短信==========
        String sqlselectName = "select * from workflow_nodecustomrcmenu where wfid="+workflowid_tmp+" and nodeid="+nodeid_tmp;
        if(isremark_tmp.equals("0")){
            RecordSet.executeSql("select nodeid from workflow_currentoperator c where c.requestid="+requestid+" and c.userid in("+userid_tmp+") and c.usertype="+usertype_tmp+" and c.isremark='"+isremark_tmp+"' ");
            String tmpnodeid="";
            if(RecordSet.next()){
                tmpnodeid = Util.null2String(RecordSet.getString("nodeid"));
            }
            sqlselectName = "select * from workflow_nodecustomrcmenu where wfid="+workflowid_tmp+" and nodeid="+tmpnodeid;

            if(!"".equals(tmpnodeid))
            {
                RecordSet.executeSql(sqlselectName);
                if(RecordSet.next()){
                    haswfrm = Util.null2String(RecordSet.getString("haswfrm"));
                    hassmsrm = Util.null2String(RecordSet.getString("hassmsrm"));
                }
            }
        }

        //是否拥有新建流程功能
        if("1".equals(haswfrm)){
            RequestCheckUser rcu = new RequestCheckUser();
            rcu.setUserid(Util.getIntValue(userid_tmp, 0));
            rcu.setWorkflowid(Util.getIntValue(workflowid_tmp));
            rcu.setLogintype(usertype_tmp);
            try{
                rcu.checkUser();
                int  t_hasright=rcu.getHasright();
                if(t_hasright == 1){
                    cannewflow = true;
                }
            }catch(Exception e)
            {
                cannewflow =false;
            }
        }
        //是否拥有发短信按钮
        RTXConfig rtxconfig = new RTXConfig();
        String temV = rtxconfig.getPorp(rtxconfig.CUR_SMS_SERVER_IS_VALID);
        boolean valid = false;
        if (temV != null && temV.equalsIgnoreCase("true")) {
            valid = true;
        } else {
            valid = false;
        }
        User user = getUser(Util.getIntValue(userid_tmp, 0));
        if(valid == true && "1".equals(hassmsrm) && HrmUserVarify.checkUserRight("CreateSMS:View", user)){
            cannewmessage = true;
        }

        //表单日志
        String isModifyLog = "";
        RecordSet.executeSql("select t1.ismodifylog,t2.currentstatus from workflow_base t1, workflow_requestbase t2 where t1.id=t2.workflowid and t2.requestid="+requestid);
        if(RecordSet.next()) {
            isModifyLog = RecordSet.getString("isModifyLog");
        }
        if("1".equals(isModifyLog))
        {
            canseelog = true;
        }
        if(ru.getOfsSetting().getIsuse()==1&&Util.getIntValue(requestid)<0){
            canransmit = false ;
            canprint = false ;
            cannewflow = false ;
            cannewmessage = false ;
            canseelog = false ;
        }
        //System.out.println("--534-canHandlFor---"+canHandlFor);
        //System.out.println("--534-cantakingOp---"+cantakingOp);
        if(isread)	//标记为已读
            showMarkReadBtn = "true";
        if(canransmit)	//转发
            showRansmitBtn = "true";
        if(canprint)	//打印
            showPrintBtn = "true";
        if(cannewflow)//新建流程
            showNewflowBtn = "false";
        if(cannewmessage)//新建短信
            showNewMessageBtn = "false";
        if(canseelog)	//表单日志
            showFormLogBtn = "true";

        if(Util.getIntValue(requestid)<0){
            showMarkReadBtn = "false";
        }
        result.add(showMarkReadBtn);
        result.add(showRansmitBtn);
        result.add(showPrintBtn);
        result.add(showNewflowBtn);
        result.add(showNewMessageBtn);
        result.add(showFormLogBtn);
        return result;
    }
    /**
     * 代理流程收回菜单判断
     * add by lsj @2014-1-22
     * @return
     *
     */
    public List<String> getWFAgentBackOperation(String agentid,String outparaminfo)
    {
        List<String>  res=new ArrayList<String>();
        String[] params=outparaminfo.split("_");
        String userid=params[0];
        String agentinfo=params[1];
        if("0".equals(agentinfo))
        {
            res.add("true");

        }else if("1".equals(agentinfo))
        {
            User user=getUser(Util.getIntValue(userid, 0));
            boolean hasRights=HrmUserVarify.checkUserRight("WorkflowAgent:All", user);
            res.add(hasRights+"");
        }
        return res;
    }

    /**
     * 得到当前节点的类型
     * @return 当前节点的类型
     */
    public String getCurrentType(String requestid){
        String returnStr="";
        RecordSet rs = new RecordSet();
        rs.executeSql("SELECT * FROM workflow_Requestbase WHERE requestid  = '" + requestid + "'");
        if(rs.next()){
            returnStr =  rs.getString("currentnodetype");
        }
        return returnStr;
    }

    /**
     * 代理流程收回check判断是否显示
     * add by lsj @2014-1-22
     * @return
     *
     */
    public String getWFAgentBackOperationCheckBox(String outparaminfo)
    {
        String rs="false";
        String[] params=outparaminfo.split("_");
        String userid=params[0];
        String agentinfo=params[1];
        if("0".equals(agentinfo))
        {
            rs="true";

        }else if("1".equals(agentinfo))
        {
            User user=getUser(Integer.parseInt(userid));
            boolean hasRights=HrmUserVarify.checkUserRight("WorkflowAgent:All", user);
            rs=hasRights+"";
        }
        return rs;
    }
    /**
     * 督办标记已读
     * @param requestname 流程标题
     * @param para2 流程ID+，+显示类型+，+用户语言
     * @return    "true" or "false"
     */
    public List<String> getWfUrgerNewOperation(String requestname, String para2) {

        List<String> rsdata=new ArrayList<String>();
        String[] tempStr = Util.splitString(para2, "+");
        int requestid = Util.getIntValue(Util.null2String(tempStr[0]));
        int userid = Util.getIntValue(Util.null2String(tempStr[2]),0);
        int usertype=Util.getIntValue(Util.null2String(tempStr[3]),0);
        RecordSet rs = new RecordSet();
        String  isnew = "false";

        String newsql = "select b.lastoperatedate,b.lastoperatetime,b.creater,b.lastoperator, b.lastoperatortype from workflow_requestbase b where b.requestid = "
                + requestid;
        String requestdate = "";
        String viewdate = "";
        rs.execute(newsql);
        if (rs.next())
        {
            if(userid!=rs.getInt(4)||usertype!=rs.getInt(5)){
                if("".equals(Util.null2String(rs.getString(1))) || "".equals(Util.null2String(rs.getString(2)))) {
                    if(rs.getInt(3) != userid) {
                        newsql = "select w.viewdate from workflow_requestviewlog w where w.viewer="
                                +userid + " and id="+requestid;
                        rs.execute(newsql);
                        if(!rs.next()) {
                            isnew = "true";
                        }
                    }
                }else {
                    requestdate = rs.getString(1) + rs.getString(2);
                    newsql = "select max(w.viewdate) as viewdate,max(w.viewtime) as viewtime from workflow_requestviewlog w where w.viewer="
                            +userid + " and id="+requestid +" group by id";
                    rs.execute(newsql);
                    if(rs.next()) {
                        viewdate = rs.getString(1) + rs.getString(2);
                        if(viewdate.compareTo(requestdate)<0) {
                            isnew = "true";
                        }
                    }else {
                        isnew = "true";
                    }
                }
            }
        }
        rsdata.add(isnew);
        return rsdata;
    }
    /**
     * 督办标记已读(checkbox)
     * @param para2 流程ID+，+显示类型+，+用户语言
     * @return    "true" or "false"
     */
    public String getWfUrgerNewOperationCheckBox(String para2) {

        String[] tempStr = Util.splitString(para2, "+");
        int requestid = Util.getIntValue(Util.null2String(tempStr[0]));
        int userid = Util.getIntValue(Util.null2String(tempStr[2]),0);
        int usertype=Util.getIntValue(Util.null2String(tempStr[3]),0);
        String  isnew = "false";

        String newsql = "select b.lastoperatedate,b.lastoperatetime,b.creater,b.lastoperator, b.lastoperatortype from workflow_requestbase b where b.requestid = "
                + requestid;
        String requestdate = "";
        String viewdate = "";
        RecordSet rs = new RecordSet();
        rs.execute(newsql);
        if (rs.next())
        {
            if(userid!=rs.getInt(4)||usertype!=rs.getInt(5)){
                if("".equals(Util.null2String(rs.getString(1))) || "".equals(Util.null2String(rs.getString(2)))) {
                    if(rs.getInt(3) != userid) {
                        newsql = "select w.viewdate from workflow_requestviewlog w where w.viewer="
                                +userid + " and id="+requestid;
                        rs.execute(newsql);
                        if(!rs.next()) {
                            isnew = "true";
                        }
                    }
                }else {
                    requestdate = rs.getString(1) + rs.getString(2);
                    newsql = "select max(w.viewdate) as viewdate,max(w.viewtime) as viewtime from workflow_requestviewlog w where w.viewer="
                            +userid + " and id="+requestid +" group by id";
                    rs.execute(newsql);
                    if(rs.next()) {
                        viewdate = rs.getString(1) + rs.getString(2);
                        if(viewdate.compareTo(requestdate)<0) {
                            isnew = "true";
                        }
                    }else {
                        isnew = "true";
                    }
                }
            }
        }
        return isnew;
    }




    private User getUser(int userid) {
        User user=new User();
        try {
            ResourceComInfo rc = new ResourceComInfo();
            DepartmentComInfo dc = new DepartmentComInfo() ;

            user.setUid(userid);
            user.setLoginid(rc.getLoginID("" + userid));
            user.setFirstname(rc.getFirstname("" + userid));
            user.setLastname(rc.getLastname("" + userid));
            user.setLogintype("1");
            // user.setAliasname(rc.getAssistantID(""+userid));
            // user.setTitle(rs.getString("title"));
            // user.setTitlelocation(rc.getLocationid(""+userid));
            user.setSex(rc.getSexs("" + userid));
            user.setLanguage(7);
            // user.setTelephone(rc);
            // user.setMobile(rc.getm);
            // user.setMobilecall(rs.getString("mobilecall"));
            user.setEmail(rc.getEmail("" + userid));
            // user.setCountryid();
            user.setLocationid(rc.getLocationid("" + userid));
            user.setResourcetype(rc.getResourcetype("" + userid));
            // user.setStartdate(rc.gets);
            // user.setEnddate(rc.gete);
            // user.setContractdate(rc.getc);
            user.setJobtitle(rc.getJobTitle("" + userid));
            // user.setJobgroup(rs.getString("jobgroup"));
            // user.setJobactivity(rs.getString("jobactivity"));
            user.setJoblevel(rc.getJoblevel("" + userid));
            user.setSeclevel(rc.getSeclevel("" + userid));
            user.setUserDepartment(Util.getIntValue(rc.getDepartmentID("" + userid), 0));
            user.setUserSubCompany1(Util.getIntValue(dc.getSubcompanyid1(user.getUserDepartment() + ""), 0));
            // user.setUserSubCompany2(Util.getIntValue(rs.getString("subcompanyid2"),0));
            // user.setUserSubCompany3(Util.getIntValue(rs.getString("subcompanyid3"),0));
            // user.setUserSubCompany4(Util.getIntValue(rs.getString("subcompanyid4"),0));
            user.setManagerid(rc.getManagerID("" + userid));
            user.setAssistantid(rc.getAssistantID("" + userid));
            // user.setPurchaselimit(rc.getPropValue(""+userid));
            // user.setCurrencyid(rc.getc);
            // user.setLastlogindate(rc.get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 判断当前请求当前节点是否有必填项没有填写
     * @param requestid
     * @return
     */
    private boolean haveMustInput(int requestid) {
        boolean flag = false;
        String sql = "select currentnodeid,workflowid from workflow_requestbase where requestid=" + requestid;
        ////System.out.println("查询当前流程节点:"+sql);
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        rs.executeSql(sql);
        if (rs.next()) {
            int nodeid = rs.getInt(1);
            int workflowid = rs.getInt(2);
            sql = "select ismode,showdes,printdes,toexcel from workflow_flownode where workflowid=" + workflowid + " and nodeid=" + nodeid;
            //System.out.println("查询当前流程模式:"+sql);
            rs.executeSql(sql);
            if (rs.next()) {
                String ismode = Util.null2String(rs.getString("ismode"));
                int showdes = Util.getIntValue(Util.null2String(rs.getString("showdes")), 0);
                int modeid = 0;
                int isbill = 0;
                int formid = 0;
                String sqlwhere = "";
                String viewtablename = "workflow_nodeform";
                sql = "select formid,isbill from workflow_base where id=" + workflowid;
                //System.out.println("查询当前流程formid:"+sql);
                rs.executeSql(sql);
                if (rs.next()) {
                    formid = rs.getInt(1);
                    isbill = rs.getInt(2);
                }
                if (ismode.equals("1") && showdes != 1) {
                    sql = "select id from workflow_nodemode where isprint='0' and workflowid=" + workflowid + " and nodeid=" + nodeid;
                    rs.executeSql(sql);
                    if (rs.next()) {
                        modeid = rs.getInt("id");
                    } else {
                        sql = "select id from workflow_formmode where isprint='0' and formid=" + formid + " and isbill=" + isbill;
                        rs.executeSql(sql);
                        if (rs.next()) {
                            modeid = rs.getInt("id");
                        }
                    }
                }
                //模板模式
                if (modeid > 0) {
                    viewtablename = "workflow_modeview";
                }
                //System.out.println("formid:"+formid+",isbill="+isbill);
                //表单
                if (isbill == 0) {
                    //主字段
                    sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_formdict ff where nf.fieldid=ff.id and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 ";
                    rs.executeSql(sql);
                    while (rs.next()) {
                        String fielddbtype = Util.null2String(rs.getString(2));
                        if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                            if (sqlwhere.equals(""))
                                sqlwhere = rs.getString(1) + " is null";
                            else
                                sqlwhere += " or " + rs.getString(1) + " is null";
                        } else {
                            if(rs.getDBType().equals("sqlserver")){
                                String _t = " convert(varchar,"+rs.getString(1)+") ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                else
                                    sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                            }else{
                                if (sqlwhere.equals("")){
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere = rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0";
                                    }else{
                                        sqlwhere = rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }else{
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere += " or (" +rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0)";
                                    }else{
                                        sqlwhere += " or " + rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }
                            }
                        }
                    }
                    if (!sqlwhere.equals("")) {
                        sql = "select requestid from workflow_form where requestid=" + requestid + " and (" + sqlwhere + ")";
                        rs.executeSql(sql);
                        if (rs.next()) {
                            return true;
                        }
                    }
                    //明细字段
                    sqlwhere = "";
                    sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_formdictdetail ff where nf.fieldid=ff.id and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 ";
                    //System.out.println("查询明细字段"+sql);
                    rs.executeSql(sql);
                    while (rs.next()) {
                        String fielddbtype = Util.null2String(rs.getString(2));
                        if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                            if (sqlwhere.equals(""))
                                sqlwhere = rs.getString(1) + " is null";
                            else
                                sqlwhere += " or " + rs.getString(1) + " is null";
                        } else {
                            if(rs.getDBType().equals("sqlserver")){
                                String _t = " convert(varchar,"+rs.getString(1)+") ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                else
                                    sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                            }else{
                                if (sqlwhere.equals("")){
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere = rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0";
                                    }else{
                                        sqlwhere = rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }else{
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere += " or (" +rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0)";
                                    }else{
                                        sqlwhere += " or " + rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }
                            }
                        }
                    }
                    if (!sqlwhere.equals("")) {
                        sql = "select requestid from workflow_formdetail where requestid=" + requestid + " and (" + sqlwhere + ")";
                        //System.out.println("284查询是否可以进行批量提交"+sql);
                        rs.executeSql(sql);
                        if (rs.next()) {
                            return true;
                        }
                    }
                } else if (isbill == 1) {   //单据
                    //主字段
                    String maintablename = "";
                    String detailkey = "";
                    sql = "select tablename,detailkeyfield from workflow_bill where id=" + formid;
                    //System.out.println("295:"+sql);
                    rs.executeSql(sql);
                    if (rs.next()) {
                        maintablename = rs.getString(1);
                        detailkey = Util.null2String(rs.getString(2));
                    }
                    if (detailkey.equals("")) detailkey = "mainid";
                    sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_billfield ff where (ff.viewtype is null or ff.viewtype=0) and nf.fieldid=ff.id and ff.billid=" + formid + " and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 ";
                    //System.out.println("303:"+sql);
                    rs.executeSql(sql);
                    while (rs.next()) {
                        String fielddbtype = Util.null2String(rs.getString(2));
                        if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                            if (sqlwhere.equals(""))
                                sqlwhere = rs.getString(1) + " is null";
                            else
                                sqlwhere += " or " + rs.getString(1) + " is null";
                        } else {
                            if(rs.getDBType().equals("sqlserver")){
                                String _t = " convert(varchar,"+rs.getString(1)+") ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                else
                                    sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                            }else{
                                if (sqlwhere.equals("")){
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere = rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0";
                                    }else{
                                        sqlwhere = rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }else{
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere += " or (" +rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0)";
                                    }else{
                                        sqlwhere += " or " + rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }
                            }
                        }
                    }
                    if (!sqlwhere.equals("")) {
                        sql = "select requestid from " + maintablename + " where requestid=" + requestid + " and (" + sqlwhere + ")";
                        rs.executeSql(sql);
                        if (rs.next()) {
                            return true;
                        }
                    }
                    //明细字段
                    sql = "select tablename from workflow_billdetailtable where billid=" + formid;
                    //System.out.println("336:"+sql);
                    rs1.executeSql(sql);
                    while (rs1.next()) {
                        String detailtablename = rs1.getString(1);
                        sqlwhere = "";
                        sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_billfield ff where ff.viewtype=1 and nf.fieldid=ff.id and ff.billid=" + formid + " and ff.detailtable='" + detailtablename + "' and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 order by ff.detailtable ";
                        //System.out.println("342:"+sql);
                        rs.executeSql(sql);
                        while (rs.next()) {
                            String fielddbtype = Util.null2String(rs.getString(2));
                            if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                                if (sqlwhere.equals(""))
                                    sqlwhere = detailtablename + "." + rs.getString(1) + " is null";
                                else
                                    sqlwhere += " or " + detailtablename + "." + rs.getString(1) + " is null";
                            } else {
                                if(rs.getDBType().equals("sqlserver")){
                                    String _t = " convert(varchar,"+detailtablename+"."+rs.getString(1)+") ";
                                    if (sqlwhere.equals(""))
                                        sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                    else
                                        sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                                }else{
                                    String _t = " "+detailtablename+"."+rs.getString(1)+" ";
                                    if (sqlwhere.equals("")){
                                        if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                            sqlwhere = _t + " is null or dbms_lob.getlength("+_t+") = 0";
                                        }else{
                                            sqlwhere = _t + " is null or " + _t+ "='' or " +_t + "=' '";
                                        }
                                    }else{
                                        if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                            sqlwhere += " or (" +_t + " is null or dbms_lob.getlength("+_t+") = 0)";
                                        }else{
                                            sqlwhere += " or " +_t + " is null or " + _t+ "='' or " +_t + "=' '";
                                        }
                                    }
                                }
                            }
                        }
                        if (!sqlwhere.equals("")) {
                            sql = "select " + maintablename + ".requestid from " + maintablename + "," + detailtablename + " where " + maintablename + ".id=" + detailtablename + "." + detailkey + " and " + maintablename + ".requestid=" + requestid + " and (" + sqlwhere + ")";
                            //System.out.println("366:"+sql);
                            rs.executeSql(sql);
                            if (rs.next()) {
                                return true;
                            }
                        }
                    }
                    if (rs1.getCounts() < 1) {
                        sql = "select detailtablename from workflow_bill where id=" + formid;
                        //////System.out.println("375:"+sql);
                        rs.executeSql(sql);
                        if (rs.next()) {
                            String detailtablename = rs.getString(1);
                            sqlwhere = "";
                            sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf , workflow_billfield ff where ff.viewtype=1 and nf.fieldid=ff.id and ff.billid=" + formid + " and ff.detailtable='" + detailtablename + "' and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 order by ff.detailtable ";
                            //System.out.println("380:"+sql);
                            rs.executeSql(sql);
                            while (rs.next()) {
                                String fielddbtype = Util.null2String(rs.getString(2));
                                if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                                    if (sqlwhere.equals(""))
                                        sqlwhere = detailtablename+"."+rs.getString(1) + " is null";
                                    else
                                        sqlwhere += " or " + detailtablename+"."+rs.getString(1) + " is null";
                                } else {
                                    if(rs.getDBType().equals("sqlserver")){
                                        String _t = " convert(varchar,"+detailtablename+"."+rs.getString(1)+") ";
                                        if (sqlwhere.equals(""))
                                            sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                        else
                                            sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                                    }else{
                                        String _t = " "+detailtablename+"."+rs.getString(1)+" ";
                                        if (sqlwhere.equals("")){
                                            if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                                sqlwhere = _t + " is null or dbms_lob.getlength("+_t+") = 0";
                                            }else{
                                                sqlwhere = _t + " is null or " + _t+ "='' or " +_t + "=' '";
                                            }
                                        }else{
                                            if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                                sqlwhere += " or (" +_t + " is null or dbms_lob.getlength("+_t+") = 0)";
                                            }else{
                                                sqlwhere += " or " +_t + " is null or " + _t+ "='' or " +_t + "=' '";
                                            }
                                        }
                                    }
                                }
                            }
                            if (!sqlwhere.equals("")) {
                                sql = "select " + maintablename + ".requestid from " + maintablename + "," + detailtablename + " where " + maintablename + ".id=" + detailtablename + "." + detailkey + " and " + maintablename + ".requestid=" + requestid + " and (" + sqlwhere + ")";
                                //System.out.println("400查询是否可以进行批量提交"+sql);
                                rs.executeSql(sql);
                                if (rs.next()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 重写，加上当前节点id
     * 判断当前请求当前节点是否有必填项没有填写
     * @param requestid
     * @param nodeid
     * @return
     */
    private boolean haveMustInput(int requestid,int nodeid) {
        boolean flag = false;
        String sql = "select currentnodeid,workflowid from workflow_requestbase where requestid=" + requestid;
        ////System.out.println("查询当前流程节点:"+sql);
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        rs.executeSql(sql);
        if (rs.next()) {
            //int nodeid = rs.getInt(1);
            int workflowid = rs.getInt(2);
            sql = "select ismode,showdes,printdes,toexcel from workflow_flownode where workflowid=" + workflowid + " and nodeid=" + nodeid;
            //System.out.println("查询当前流程模式:"+sql);
            rs.executeSql(sql);
            if (rs.next()) {
                String ismode = Util.null2String(rs.getString("ismode"));
                int showdes = Util.getIntValue(Util.null2String(rs.getString("showdes")), 0);
                int modeid = 0;
                int isbill = 0;
                int formid = 0;
                String sqlwhere = "";
                String viewtablename = "workflow_nodeform";
                sql = "select formid,isbill from workflow_base where id=" + workflowid;
                //System.out.println("查询当前流程formid:"+sql);
                rs.executeSql(sql);
                if (rs.next()) {
                    formid = rs.getInt(1);
                    isbill = rs.getInt(2);
                }
                if (ismode.equals("1") && showdes != 1) {
                    sql = "select id from workflow_nodemode where isprint='0' and workflowid=" + workflowid + " and nodeid=" + nodeid;
                    rs.executeSql(sql);
                    if (rs.next()) {
                        modeid = rs.getInt("id");
                    } else {
                        sql = "select id from workflow_formmode where isprint='0' and formid=" + formid + " and isbill=" + isbill;
                        rs.executeSql(sql);
                        if (rs.next()) {
                            modeid = rs.getInt("id");
                        }
                    }
                }
                //模板模式
                if (modeid > 0) {
                    viewtablename = "workflow_modeview";
                }
                //System.out.println("formid:"+formid+",isbill="+isbill);
                //表单
                if (isbill == 0) {
                    //主字段
                    sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_formdict ff where nf.fieldid=ff.id and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 ";
                    rs.executeSql(sql);
                    while (rs.next()) {
                        String fielddbtype = Util.null2String(rs.getString(2));
                        if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                            if (sqlwhere.equals(""))
                                sqlwhere = rs.getString(1) + " is null";
                            else
                                sqlwhere += " or " + rs.getString(1) + " is null";
                        } else {
                            if(rs.getDBType().equals("sqlserver")){
                                String _t = " convert(varchar,"+rs.getString(1)+") ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                else
                                    sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                            }else{
                                if (sqlwhere.equals("")){
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere = rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0";
                                    }else{
                                        sqlwhere = rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }else{
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere += " or (" +rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0)";
                                    }else{
                                        sqlwhere += " or " + rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }
                            }
                        }
                    }
                    if (!sqlwhere.equals("")) {
                        sql = "select requestid from workflow_form where requestid=" + requestid + " and (" + sqlwhere + ")";
                        rs.executeSql(sql);
                        if (rs.next()) {
                            return true;
                        }
                    }
                    //明细字段
                    sqlwhere = "";
                    sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_formdictdetail ff where nf.fieldid=ff.id and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 ";
                    //System.out.println("查询明细字段"+sql);
                    rs.executeSql(sql);
                    while (rs.next()) {
                        String fielddbtype = Util.null2String(rs.getString(2));
                        if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                            if (sqlwhere.equals(""))
                                sqlwhere = rs.getString(1) + " is null";
                            else
                                sqlwhere += " or " + rs.getString(1) + " is null";
                        } else {
                            if(rs.getDBType().equals("sqlserver")){
                                String _t = " convert(varchar,"+rs.getString(1)+") ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                else
                                    sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                            }else{
                                if (sqlwhere.equals("")){
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere = rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0";
                                    }else{
                                        sqlwhere = rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }else{
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere += " or (" +rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0)";
                                    }else{
                                        sqlwhere += " or " + rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }
                            }
                        }
                    }
                    if (!sqlwhere.equals("")) {
                        sql = "select requestid from workflow_formdetail where requestid=" + requestid + " and (" + sqlwhere + ")";
                        //System.out.println("284查询是否可以进行批量提交"+sql);
                        rs.executeSql(sql);
                        if (rs.next()) {
                            return true;
                        }
                    }
                } else if (isbill == 1) {   //单据
                    //主字段
                    String maintablename = "";
                    String detailkey = "";
                    sql = "select tablename,detailkeyfield from workflow_bill where id=" + formid;
                    //System.out.println("295:"+sql);
                    rs.executeSql(sql);
                    if (rs.next()) {
                        maintablename = rs.getString(1);
                        detailkey = Util.null2String(rs.getString(2));
                    }
                    if (detailkey.equals("")) detailkey = "mainid";
                    sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_billfield ff where (ff.viewtype is null or ff.viewtype=0) and nf.fieldid=ff.id and ff.billid=" + formid + " and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 ";
                    //System.out.println("303:"+sql);
                    rs.executeSql(sql);
                    while (rs.next()) {
                        String fielddbtype = Util.null2String(rs.getString(2));
                        if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                            if (sqlwhere.equals(""))
                                sqlwhere = rs.getString(1) + " is null";
                            else
                                sqlwhere += " or " + rs.getString(1) + " is null";
                        } else {
                            if(rs.getDBType().equals("sqlserver")){
                                String _t = " convert(varchar,"+rs.getString(1)+") ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                else
                                    sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                            }else{
                                if (sqlwhere.equals("")){
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere = rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0";
                                    }else{
                                        sqlwhere = rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }else{
                                    if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                        sqlwhere += " or (" +rs.getString(1) + " is null or dbms_lob.getlength("+rs.getString(1)+") = 0)";
                                    }else{
                                        sqlwhere += " or " + rs.getString(1) + " is null or " + rs.getString(1) + "='' or " + rs.getString(1) + "=' '";
                                    }
                                }
                            }
                        }
                    }
                    if (!sqlwhere.equals("")) {
                        sql = "select requestid from " + maintablename + " where requestid=" + requestid + " and (" + sqlwhere + ")";
                        rs.executeSql(sql);
                        if (rs.next()) {
                            return true;
                        }
                    }
                    //明细字段
                    sql = "select tablename from workflow_billdetailtable where billid=" + formid;
                    //System.out.println("336:"+sql);
                    rs1.executeSql(sql);
                    while (rs1.next()) {
                        String detailtablename = rs1.getString(1);
                        sqlwhere = "";
                        sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf ,workflow_billfield ff where ff.viewtype=1 and nf.fieldid=ff.id and ff.billid=" + formid + " and ff.detailtable='" + detailtablename + "' and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 order by ff.detailtable ";
                        //System.out.println("342:"+sql);
                        rs.executeSql(sql);
                        while (rs.next()) {
                            String fielddbtype = Util.null2String(rs.getString(2));
                            if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                                String _t = " "+detailtablename+"."+rs.getString(1)+" ";
                                if (sqlwhere.equals(""))
                                    sqlwhere = _t + " is null";
                                else
                                    sqlwhere += " or " + _t + " is null";
                            } else {
                                if(rs.getDBType().equals("sqlserver")){
                                    String _t = " convert(varchar,"+detailtablename+"."+rs.getString(1)+") ";
                                    if (sqlwhere.equals(""))
                                        sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                    else
                                        sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                                }else{
                                    String _t = " "+detailtablename+"."+rs.getString(1)+" ";
                                    if (sqlwhere.equals("")){
                                        if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                            sqlwhere = _t + " is null or dbms_lob.getlength("+_t+") = 0";
                                        }else{
                                            sqlwhere = _t + " is null or " + _t+ "='' or " +_t + "=' '";
                                        }
                                    }else{
                                        if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                            sqlwhere += " or (" +_t + " is null or dbms_lob.getlength("+_t+") = 0)";
                                        }else{
                                            sqlwhere += " or " +_t + " is null or " + _t+ "='' or " +_t + "=' '";
                                        }
                                    }
                                }
                            }
                        }
                        if (!sqlwhere.equals("")) {
                            sql = "select " + maintablename + ".requestid from " + maintablename + "," + detailtablename + " where " + maintablename + ".id=" + detailtablename + "." + detailkey + " and " + maintablename + ".requestid=" + requestid + " and (" + sqlwhere + ")";
                            //System.out.println("366:"+sql);
                            rs.executeSql(sql);
                            if (rs.next()) {
                                return true;
                            }
                        }
                    }
                    if (rs1.getCounts() < 1) {
                        sql = "select detailtablename from workflow_bill where id=" + formid;
                        //////System.out.println("375:"+sql);
                        rs.executeSql(sql);
                        if (rs.next()) {
                            String detailtablename = rs.getString(1);
                            sqlwhere = "";
                            sql = "select ff.fieldname,ff.fielddbtype from " + viewtablename + " nf , workflow_billfield ff where ff.viewtype=1 and nf.fieldid=ff.id and ff.billid=" + formid + " and ff.detailtable='" + detailtablename + "' and nf.nodeid=" + nodeid + " and nf.ismandatory=1 and nf.fieldid>0 order by ff.detailtable ";
                            //System.out.println("380:"+sql);
                            rs.executeSql(sql);
                            while (rs.next()) {
                                String fielddbtype = Util.null2String(rs.getString(2));
                                if (fielddbtype.toLowerCase().indexOf("int") > -1 || fielddbtype.toLowerCase().indexOf("float") > -1 || fielddbtype.toLowerCase().indexOf("number") > -1) {
                                    String _t = " "+detailtablename+"."+rs.getString(1)+" ";
                                    if (sqlwhere.equals(""))
                                        sqlwhere = _t + " is null";
                                    else
                                        sqlwhere += " or " + _t + " is null";
                                } else {
                                    if(rs.getDBType().equals("sqlserver")){
                                        String _t = " convert(varchar,"+detailtablename+"."+rs.getString(1)+") ";
                                        if (sqlwhere.equals(""))
                                            sqlwhere = _t + " is null or " + _t + "='' or " +_t + "=' '";
                                        else
                                            sqlwhere += " or " + _t + " is null or " + _t + "='' or " + _t + "=' '";
                                    }else{
                                        String _t = " "+detailtablename+"."+rs.getString(1)+" ";
                                        if (sqlwhere.equals("")){
                                            if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                                sqlwhere = _t + " is null or dbms_lob.getlength("+_t+") = 0";
                                            }else{
                                                sqlwhere = _t + " is null or " + _t+ "='' or " +_t + "=' '";
                                            }
                                        }else{
                                            if (fielddbtype.toLowerCase().indexOf("clob") > -1){
                                                sqlwhere += " or (" +_t + " is null or dbms_lob.getlength("+_t+") = 0)";
                                            }else{
                                                sqlwhere += " or " +_t + " is null or " + _t+ "='' or " +_t + "=' '";
                                            }
                                        }
                                    }
                                }
                            }
                            if (!sqlwhere.equals("")) {
                                sql = "select " + maintablename + ".requestid from " + maintablename + "," + detailtablename + " where " + maintablename + ".id=" + detailtablename + "." + detailkey + " and " + maintablename + ".requestid=" + requestid + " and (" + sqlwhere + ")";
                                //System.out.println("400查询是否可以进行批量提交"+sql);
                                rs.executeSql(sql);
                                if (rs.next()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 是否可以进行批量提交处理(EXT)
     * @param workflowid 流程ID
     * @param isremark
     * @return true/false
     */
    public String getCanMultiSubmitExt(String workflowid,String isremark,String requestid){
        String flag="false";
        if(isremark.equals("0")){
            if("1".equals(wf.getMultiSubmit(workflowid))) flag="true";
            if(flag.equals("true")&&haveMustInput(Util.getIntValue(requestid))){
                flag="false";
            }
        }
        return flag;
    }

    /**
     * 流程标题显示样式
     * @param requestname 流程标题
     * @param requestid 请求id
     * @return   流程标题显示样式
     */
    public String getWfOnlyNewLink(String requestname,String requestid){
        return "<a href=javaScript:showModalDialog(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"\','','dialogTop:0;dialogWidth:'+(window.screen.availWidth)+'px;DialogHeight='+(window.screen.availHeight)+'px')>"+requestname+"</a>";
    }
    /**
     * 流程标题显示样式。只查看，不跳转到编辑页面
     * @param requestname 流程标题
     * @param requestid 请求id
     * @return   流程标题显示样式
     */
    public String getWfOnlyViewLink(String requestname,String requestid){
        return "<a href=javaScript:showModalDialog(\'/workflow/request/ViewRequest.jsp?isonlyview=1&requestid=" + requestid+"\','','dialogTop:0;dialogWidth:'+(window.screen.availWidth)+'px;DialogHeight='+(window.screen.availHeight)+'px')>"+requestname+"</a>";
    }
    /**
     * 流程标题显示样式（是否加图片等）
     * @param requestname 流程标题
     * @param para2 流程ID+，+显示类型+，+是否超时
     * @return   流程标题显示样式
     */
    public String getWfNewLink(String requestname, String para2) {
        String returnStr="";

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid=Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime=Util.getIntValue(tempStr[3],0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        int isbill=0;
        int formid=0;
        RecordSet rs = new RecordSet();
        ////根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
        if (rs.next())
        {
            formid=rs.getInt(1);
            isbill=rs.getInt(2);

        }
        MailAndMessage mailTitle=new MailAndMessage();
        String titles=mailTitle.getTitle(Util.getIntValue(requestid,-1),Util.getIntValue(workflowid,-1),formid,userlang,isbill);
        if (!titles.equals(""))
            requestname=requestname+"<B>（"+titles+"）</B>";

        boolean isprocessed=false;
        rs.executeSql("select isprocessed from workflow_currentoperator where ((isremark='0' and (isprocessed='2' or isprocessed='3'))  or isremark='5') and requestid = " + requestid);
        if(rs.next()){
            isprocessed=true;
        }
        if (viewtype.equals("0")) {
            //新流程,粗体链接加图片
            if(isprocessed){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+");doReadIt("+requestid+",\"\",this);>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDOut_wev8.gif\' align=absbottom></span>";
            }else{
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+");doReadIt("+requestid+",\"\",this);>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDNew_wev8.gif\' align=absbottom></span>";
            }
        } else if(viewtype.equals("-1")) {
            //旧流程,有新的提交信息未查看,普通链接加图片
            if(isprocessed){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+");doReadIt("+requestid+",\"\",this);>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDOut_wev8.gif\' align=absbottom></span>";
            }else{
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+");doReadIt("+requestid+",\"\",this);>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDNew2_wev8.gif\' align=absbottom></spna>";
            }
        }else{
            //旧流程,普通链接
            if(isprocessed){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+");doReadIt("+requestid+",\"\",this);>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src='/images/BDOut_wev8.gif' align=absbottom></span>";
            }else{
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+")>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'></span>";
            }
        }
        return returnStr;
    }

    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     * @param requestname 流程标题
     * @param para2 请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid + 操作人类性isremark+用户id userID+ 代理人/被代理人ID + 代理/被代理类型
     * @return   流程标题显示样式
     */
    public String getWfNewLinkWithTitle(String requestname, String para2) {
        String returnStr = "";

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime = Util.getIntValue(tempStr[3], 0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]), 7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String agentorbyagentid = "";
        String agenttype = "";
        String workflowtype = "";
        // sq 增加ofs_todo_data表id
        int ofsid = 0;
        if (tempStr.length >= 10) {
            agenttype = Util.null2String(tempStr[9]);
        }

        String paramstr = "";
        if (tempStr.length >= 13&&Util.getIntValue(requestid)<0) {
            String _userid = Util.null2String(tempStr[11]);
            String myrequest = Util.null2String(tempStr[12]);
            String creater = Util.null2String(tempStr[13]);
            String syscode = Util.null2String(tempStr[14]);
            workflowtype = Util.null2String(tempStr[15]);
            ofsid = Util.getIntValue(tempStr[16]);
        }
        //System.out.println("ofsid========" + ofsid + ",para2=" + para2);
        paramstr = "&_workflowid="+workflowid+"&_workflowtype="+workflowtype + "&ofsid="+ofsid;//方便异构系统使用

        String nodetitle = "";
        int isbill = 0;
        int formid = 0;
        RecordSet rs = new RecordSet();
        // //根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="
                + workflowid);
        if (rs.next()) {
            formid = rs.getInt(1);
            isbill = rs.getInt(2);

        }
        MailAndMessage mailTitle = new MailAndMessage();
        String titles = mailTitle.getTitle(Util.getIntValue(requestid, -1),
                Util.getIntValue(workflowid, -1), formid, userlang, isbill);
        if (!titles.equals(""))
            requestname = requestname + "<B>（" + titles + "）</B>";

        boolean isprocessed = false;
        boolean canContinue = false;
        rs
                .executeSql("select isprocessed, isremark, userid, nodeid from workflow_currentoperator where requestid = "
                        + requestid
                        + " and userid=" + userID
                        + " order by receivedate desc, receivetime desc");
        while (rs.next()) {
            String isremark_tmp = Util.null2String(rs.getString("isremark"));
            String isprocessed_tmp = Util.null2String(rs
                    .getString("isprocessed"));
            String userid_tmp = Util.null2String(rs.getString("userid"));
            if ((isremark_tmp.equals("0") && (isprocessed_tmp.equals("2") || isprocessed_tmp
                    .equals("3")))
                    || isremark_tmp.equals("5")) {
                isprocessed = true;
            }
            // 如果是被抄送或转发，判断是否正是某节点的操作人，取最后一次
            if (("8".equals(isremark) || "9".equals(isremark) || "1"
                    .equals(isremark))
                    && userID.equals(userid_tmp)
                    && "0".equals(isremark_tmp)
                    && canContinue == false) {
                int nodeid_tmp = Util.getIntValue(rs.getString("nodeid"), 0);
                if (nodeid_tmp != 0) {
                    isremark = isremark_tmp;
                    nodeid = "" + nodeid_tmp;
                    canContinue = true;
                }
            }
            if (isprocessed == true && canContinue == true) {
                break;
            }
        }
        // 改为按要求显示自定义流程标题的前缀信息
        if ("0".equals(isremark)) {
            rs
                    .executeSql("select nodetitle from workflow_flownode where workflowid="
                            + workflowid + " and nodeid=" + nodeid);
            if (rs.next()) {
                nodetitle = Util.null2String(rs.getString("nodetitle"));
            }
        }
        if (!"".equals(nodetitle) && !"null".equalsIgnoreCase(nodetitle)) {
            nodetitle = "（" + nodetitle + "）";
            requestname = nodetitle + requestname;
        }
        if (viewtype.equals("0")) {
            // 新流程,粗体链接加图片
            if (isprocessed) {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ");doReadIt("
                        + requestid
                        + ",\"\",this); >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                        + SystemEnv.getHtmlLabelName(19081, userlang)
                        + "'/></span>";
            } else if ("1".equals(agenttype)) {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ") >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                        + SystemEnv.getHtmlLabelName(19154, userlang)
                        + "'/></span>";
            } else {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ");doReadIt("
                        + requestid
                        + ",\"\",this); >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                        + SystemEnv.getHtmlLabelName(19154, userlang)
                        + "'/></span>";
            }
        } else if (viewtype.equals("-1")) {
            // 旧流程,有新的提交信息未查看,普通链接加图片
            if (isprocessed) {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ");doReadIt("
                        + requestid
                        + ",\"\",this); >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                        + SystemEnv.getHtmlLabelName(19081, userlang)
                        + "'/></span>";
            } else {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ");doReadIt("
                        + requestid
                        + ",\"\",this); >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                        + SystemEnv.getHtmlLabelName(20288, userlang)
                        + "'/></span>";
            }
        } else {
            // 旧流程,普通链接
            if (isprocessed) {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ");doReadIt("
                        + requestid
                        + ",\"\",this); >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                        + SystemEnv.getHtmlLabelName(19081, userlang)
                        + "'/></span>";
            } else {
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                        + requestid+paramstr
                        + "&isovertime="
                        + isovertime
                        + "\',"
                        + requestid
                        + ") >"
                        + requestname
                        + "</a><span id=\'wflist_"
                        + requestid
                        + "span\'></span>";
            }
        }
        return returnStr;
    }


    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     * @param requestname 流程标题
     * @param para2 请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid + 操作人类性isremark+用户id userID+ 代理人/被代理人ID + 代理/被代理类型
     * @return   流程标题显示样式
     * @throws Exception


     */

    public String getWfNewLinkWithTitle2(String requestname, String para2) throws Exception {
        return getWfNewLinkWithTitle2(requestname,para2,2);
    }


    public String getWfNewLinkWithTitle2(String requestname, String para2,int flag)
            throws Exception {
        DepartmentComInfo dc = new DepartmentComInfo();
        JobTitlesComInfo jc = new JobTitlesComInfo();
        String returnStr = "";

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime = Util.getIntValue(tempStr[3], 0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]), 7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String userid3 = Util.null2String(tempStr[11]);
        String username = rc.getResourcename(userid3);
        String ownDepid = rc.getDepartmentID(userid3);
        String depName = dc.getDepartmentname(ownDepid);
        String jobName = jc.getJobTitlesname(rc.getJobTitle(userid3));
        String agentorbyagentid = "";
        String agenttype = "";
        // sq 增加ofs_todo_data表id
        int ofsid = 0;
        if (tempStr.length >= 10) {
            agenttype = Util.null2String(tempStr[9]);
        }

        String workflowtype = "";
        if (tempStr.length >= 13&&Util.getIntValue(requestid)<0) {
            workflowtype = Util.null2String(tempStr[15]);
            ofsid = Util.getIntValue(tempStr[16]);
        }
        String paramstr = "&_workflowid="+workflowid+"&_workflowtype="+workflowtype + "&ofsid="+ ofsid ;//方便异构系统使用

        String nodetitle = "";
        int isbill = 0;
        int formid = 0;
        RecordSet rs = new RecordSet();
        // //根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="
                + workflowid);
        if (rs.next()) {
            formid = rs.getInt(1);
            isbill = rs.getInt(2);

        }
        MailAndMessage mailTitle = new MailAndMessage();
        String titles = mailTitle.getTitle(Util.getIntValue(requestid, -1),
                Util.getIntValue(workflowid, -1), formid, userlang, isbill);
        if (!titles.equals(""))
            requestname = requestname + "<B>（" + titles + "）</B>";

        boolean isprocessed = false;
        boolean canContinue = false;
        rs
                .executeSql("select isprocessed, isremark, userid, nodeid from workflow_currentoperator where requestid = "
                        + requestid
                        + " and userid=" + userID
                        + " order by receivedate desc, receivetime desc");
        while (rs.next()) {
            String isremark_tmp = Util.null2String(rs.getString("isremark"));
            String isprocessed_tmp = Util.null2String(rs
                    .getString("isprocessed"));
            String userid_tmp = Util.null2String(rs.getString("userid"));
            if ((isremark_tmp.equals("0") && (isprocessed_tmp.equals("2") || isprocessed_tmp
                    .equals("3")))
                    || isremark_tmp.equals("5")) {
                isprocessed = true;
            }
            // 如果是被抄送或转发，判断是否正是某节点的操作人，取最后一次
            if (("8".equals(isremark) || "9".equals(isremark) || "1"
                    .equals(isremark))
                    && userID.equals(userid_tmp)
                    && "0".equals(isremark_tmp)
                    && canContinue == false) {
                int nodeid_tmp = Util.getIntValue(rs.getString("nodeid"), 0);
                if (nodeid_tmp != 0) {
                    isremark = isremark_tmp;
                    nodeid = "" + nodeid_tmp;
                    canContinue = true;
                }
            }
            if (isprocessed == true && canContinue == true) {
                break;
            }
        }
        // 改为按要求显示自定义流程标题的前缀信息
        if ("0".equals(isremark)) {
            rs
                    .executeSql("select nodetitle from workflow_flownode where workflowid="
                            + workflowid + " and nodeid=" + nodeid);
            if (rs.next()) {
                nodetitle = Util.null2String(rs.getString("nodetitle"));
            }
        }
        if (!"".equals(nodetitle) && !"null".equalsIgnoreCase(nodetitle)) {
            nodetitle = "（" + nodetitle + "）";
            requestname = nodetitle + requestname;
        }

		String receiver = "1",sendaccount="";
        // sq 主次帐号显示小图标调整
		if(Util.getIntValue(requestid)<0){
			rs.executeQuery("select * from ofs_todo_data where id=?", ofsid);
			rs.next();
			receiver = Util.null2String(rs.getString("receiver"));
			sendaccount = Util.null2String(rs.getString("sendaccount"));
		}
        String wfpic = "mainwf_wev8";
		if(!userID.equals(userid3) && Util.getIntValue(requestid)>0){
			// OA 主次不一致
			wfpic = "subwf_wev8";
			
		}
		
        if (Util.getIntValue(requestid)<0) {	
			// 相等表示本条数据为主账号
			if(sendaccount.isEmpty() || receiver.equals(sendaccount)){
				wfpic = "mainwf_wev8";
				
			}else{
				wfpic = "subwf_wev8";
				RecordSet rss = new RecordSet();
				rss.executeQuery("select id,departmentid,jobtitle from hrmresource where loginid=?",sendaccount);
				if(rss.next()){
					depName = dc.getDepartmentname(rss.getString("departmentid"));
					jobName = jc.getJobTitlesname(rss.getString("jobtitle"));
				}
			}
        }
//writeLog("requestid="+requestid + ",ofsid=" + ofsid + ",wfpic=" + wfpic + ",sendaccount="+sendaccount+",receiver=" + receiver);

        if(flag==2) {
            if (viewtype.equals("0")) {
                // 新流程,粗体链接加图片
                if (isprocessed) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19081, userlang)
                            + "'/></span>";
                } else if ("1".equals(agenttype)) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ") >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19154, userlang)
                            + "'/></span>";
                } else {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19154, userlang)
                            + "'/></span>";
                }
            } else if (viewtype.equals("-1")) {
                // 旧流程,有新的提交信息未查看,普通链接加图片
                if (isprocessed) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19081, userlang)
                            + "'/></span>";
                } else {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(20288, userlang)
                            + "'/></span>";
                }
            } else {
                // 旧流程,普通链接
                if (isprocessed) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19081, userlang)
                            + "'/></span>";
                } else {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid + paramstr
                            + "&f_weaver_belongto_userid="
                            + userid3
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime
                            + "\',"
                            + requestid
                            + ") >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid + "span\'></span>";
                }
            }

        }
		//writeLog("returnStr="+ returnStr);
        if(flag==1){
            if (!userID.equals(userid3)) {
                rs
                        .executeSql("select * from workflow_currentoperator where userid="
                                + userid3
                                + " and workflowid="
                                + workflowid
                                + " and nodeid="
                                + nodeid
                                + " and requestid="
                                + requestid);

                if (rs.next()) {
                    if (viewtype.equals("0")) {
                        // 新流程,粗体链接加图片
                        if (isprocessed) {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this);  target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19081, userlang)
                                    + "'/></span>";
                        } else if ("1".equals(agenttype)) {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ")  target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19154, userlang)
                                    + "'/></span>";
                        } else {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this);  target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19154, userlang)
                                    + "'/></span>";
                        }
                    } else if (viewtype.equals("-1")) {
                        // 旧流程,有新的提交信息未查看,普通链接加图片
                        if (isprocessed) {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19081, userlang)
                                    + "'/></span>";
                        } else {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(20288, userlang)
                                    + "'/></span>";
                        }
                    } else {
                        // 旧流程,普通链接
                        if (isprocessed) {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19081, userlang)
                                    + "'/></span>";
                        } else {
                            returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid+paramstr
                                    + "&f_weaver_belongto_userid="
                                    + userid3
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ") target='_self'>"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid + "span\'></span>";
                        }
                    }

                }
            } else {

                // System.out.println("--usrid2-1615-"+usrid2);

                if (viewtype.equals("0")) {
                    // 新流程,粗体链接加图片
                    if (isprocessed) {
                        returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid+paramstr
                                + "&f_weaver_belongto_userid="
                                + userID
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); target='_self'>"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19081, userlang)
                                + "'/></span>";
                    } else {
                        returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid+paramstr
                                + "&f_weaver_belongto_userid="
                                + userID
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); target='_self'>"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19154, userlang)
                                + "'/></span>";
                    }
                } else if (viewtype.equals("-1")) {
                    // 旧流程,有新的提交信息未查看,普通链接加图片
                    if (isprocessed) {
                        returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid+paramstr
                                + "&f_weaver_belongto_userid="
                                + userID
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); target='_self' >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19081, userlang)
                                + "'/></span>";
                    } else {
                        returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid+paramstr
                                + "&f_weaver_belongto_userid="
                                + userID
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); target='_self'>"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(20288, userlang)
                                + "'/></span>";
                    }
                } else {
                    // 旧流程,普通链接
                    if (isprocessed) {

                        // System.out.println("--usrid2-1615-"+usrid2);

                        returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid+paramstr
                                + "&f_weaver_belongto_userid="
                                + userID
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); target='_self'>"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19081, userlang)
                                + "'/></span>";

                    } else {

                        // System.out.println("--usrid2-1615-"+usrid2);

                        returnStr = "<span><img src='/images/ecology8/"+wfpic+".png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid+paramstr
                                + "&f_weaver_belongto_userid="
                                + userID
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ") target='_self'>"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid + "span\'></span>";

                    }
                }
            }
        }
        //System.out.println("--returnStr-1956-" + returnStr);
        return returnStr;

    }


    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     * @param requestname 流程标题
     * @param para2 请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid + 操作人类性isremark+用户id userID+ 代理人/被代理人ID + 代理/被代理类型
     * @return   流程标题显示样式
     */
    public String getWfShareLinkWithTitle(String requestname, String para2) {
        String returnStr="";


        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid=Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime=Util.getIntValue(tempStr[3],0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);

        String agentorbyagentid="";
        String agenttype = "";
        String currentid = "";

        if(tempStr.length>=12){
            agenttype= Util.null2String(tempStr[9]);
            currentid= Util.null2String(tempStr[11]);
        }

        String workflowtype = "";
        String paramstr = "";
        try {
            workflowtype = Util.null2String(tempStr[13]);
        }catch(Exception e){

        }
        String creater = "";
        int ofsid = 0;
        try {
            String _userid = Util.null2String(tempStr[12]);
            creater = Util.null2String(tempStr[14]);
            ofsid = Util.getIntValue(tempStr[15]);
            /*
            if(!_userid.equals(userID)&&Util.getIntValue(requestid,0)<0){
	            viewtype = "1";
            }
            */
        }catch(Exception e){

        }
        //System.out.println("WorkFlowTransMethod.getWfShareLinkWithTitle2  ofsid=" + ofsid + ",para2="+para2);
        paramstr = "&_workflowid="+workflowid+"&_workflowtype="+workflowtype + "&ofsid=" + ofsid ;//方便异构系统使用

        String nodetitle = "";
        int isbill=0;
        int formid=0;
        RecordSet rs = new RecordSet();
        ////根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
        if (rs.next())
        {
            formid=rs.getInt(1);
            isbill=rs.getInt(2);

        }
        MailAndMessage mailTitle=new MailAndMessage();
        String titles=mailTitle.getTitle(Util.getIntValue(requestid,-1),Util.getIntValue(workflowid,-1),formid,userlang,isbill);
        if (!titles.equals(""))
            requestname=requestname+"<B>（"+titles+"）</B>";

        boolean isprocessed=false;
        boolean canContinue = false;
        rs.executeSql("select isprocessed, isremark, userid, nodeid from workflow_currentoperator where requestid = " + requestid + " order by receivedate desc, receivetime desc");
        while(rs.next()){
            String isremark_tmp = Util.null2String(rs.getString("isremark"));
            String isprocessed_tmp = Util.null2String(rs.getString("isprocessed"));
            String userid_tmp = Util.null2String(rs.getString("userid"));
            if((isremark_tmp.equals("0") && (isprocessed_tmp.equals("2") || isprocessed_tmp.equals("3"))) || isremark_tmp.equals("5")){
                isprocessed=true;
            }
            //如果是被抄送或转发，判断是否正是某节点的操作人，取最后一次
            if(("8".equals(isremark)||"9".equals(isremark)||"1".equals(isremark)) && userID.equals(userid_tmp) && "0".equals(isremark_tmp) && canContinue == false){
                int nodeid_tmp = Util.getIntValue(rs.getString("nodeid"), 0);
                if(nodeid_tmp != 0){
                    isremark = isremark_tmp;
                    nodeid = "" + nodeid_tmp;
                    canContinue = true;
                }
            }
            if(isprocessed == true && canContinue == true){
                break;
            }
        }
        //改为按要求显示自定义流程标题的前缀信息
        if("0".equals(isremark)){
            rs.executeSql("select nodetitle from workflow_flownode where workflowid="+workflowid+" and nodeid="+nodeid);
            if(rs.next()){
                nodetitle = Util.null2String(rs.getString("nodetitle"));
            }
        }
        if(!"".equals(nodetitle) && !"null".equalsIgnoreCase(nodetitle)){
            nodetitle = "（"+nodetitle+"）";
            requestname = nodetitle + requestname;
        }

        String allrequestids = "";
        if(!"".equals(currentid) && !"".equals(currentid.trim())){
            rs.executeSql("select requestid from workflow_currentoperator where id in("+currentid+")");
            while(rs.next()){
                if("".equals(allrequestids)){
                    allrequestids = Util.null2String(rs.getString("requestid"));
                }else{
                    allrequestids += ","+Util.null2String(rs.getString("requestid"));
                }
            }
        }

        boolean checkwfshare = false;
        if(!"".equals(allrequestids)){
            String checkquestids = ","+allrequestids+",";
            if(checkquestids.indexOf(requestid) > -1){
                checkwfshare = true;
            }
        }

        if(checkwfshare){
            returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                    + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'></span>";
        }else{
            if (viewtype.equals("0")) {
                //新流程,粗体链接加图片
                if(isprocessed){
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+");doReadIt("+requestid+",\"\",this); >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='" + SystemEnv.getHtmlLabelName(19081, userlang) + "'/></span>";
                }else if("1".equals(agenttype)){
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='" + SystemEnv.getHtmlLabelName(19154, userlang) + "'/></span>";
                }else{
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+");doReadIt("+requestid+",\"\",this); >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='" + SystemEnv.getHtmlLabelName(19154, userlang) + "'/></span>";
                }
            } else if(viewtype.equals("-1")) {
                //旧流程,有新的提交信息未查看,普通链接加图片
                if(isprocessed){
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+");doReadIt("+requestid+",\"\",this); >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='" + SystemEnv.getHtmlLabelName(19081, userlang) + "'/></span>";
                }else{
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+");doReadIt("+requestid+",\"\",this); >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='" + SystemEnv.getHtmlLabelName(20288, userlang) + "'/></span>";
                }
            }else{
                //旧流程,普通链接
                if(isprocessed){
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+");doReadIt("+requestid+",\"\",this); >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='" + SystemEnv.getHtmlLabelName(19081, userlang) + "'/></span>";
                }else{
                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime+paramstr
                            + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'></span>";
                }
            }
        }
        return returnStr;
    }



    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     * @param requestname 流程标题
     * @param para2 请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid + 操作人类性isremark+用户id userID+ 代理人/被代理人ID + 代理/被代理类型
     * @return   流程标题显示样式
     * @throws Exception
     */
    public String getWfShareLinkWithTitle2(String requestname, String para2)
            throws Exception {
        //System.out.println("para2=" + para2);
        String returnStr = "";
        ResourceComInfo rc = new ResourceComInfo();
        DepartmentComInfo dc = new DepartmentComInfo();
        JobTitlesComInfo jc = new JobTitlesComInfo();
        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime = Util.getIntValue(tempStr[3], 0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]), 7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String agentorbyagentid = "";
        String agenttype = "";
        String currentid = "";
        String nowuserid = "";
        if (tempStr.length >= 12) {
            agenttype = Util.null2String(tempStr[9]);
            currentid = Util.null2String(tempStr[11]);
            nowuserid = Util.null2String(tempStr[12]);
        }
        String workflowtype = "";
        String paramstr = "";
        int ofsid = 0;
        try {
            workflowtype = Util.null2String(tempStr[13]);
            ofsid = Util.getIntValue(tempStr[15]);
        }catch(Exception e){

        }
        //System.out.println("WorkFlowTransMethod.getWfShareLinkWithTitle2  ofsid=" + ofsid + ",para2="+para2);
        paramstr = "&_workflowid="+workflowid+"&_workflowtype="+workflowtype + "&ofsid=" + ofsid ;//方便异构系统使用

        String username = rc.getResourcename(nowuserid);
        String ownDepid = rc.getDepartmentID(nowuserid);
        String depName = dc.getDepartmentname(ownDepid);
        String jobName = jc.getJobTitlesname(rc.getJobTitle(nowuserid));

        String nodetitle = "";
        int isbill = 0;
        int formid = 0;
        RecordSet rs = new RecordSet();
        // //根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="
                + workflowid);
        if (rs.next()) {
            formid = rs.getInt(1);
            isbill = rs.getInt(2);

        }
        MailAndMessage mailTitle = new MailAndMessage();
        String titles = mailTitle.getTitle(Util.getIntValue(requestid, -1),
                Util.getIntValue(workflowid, -1), formid, userlang, isbill);
        if (!titles.equals(""))
            requestname = requestname + "<B>（" + titles + "）</B>";

        boolean isprocessed = false;
        boolean canContinue = false;
        rs
                .executeSql("select isprocessed, isremark, userid, nodeid from workflow_currentoperator where requestid = "
                        + requestid
                        + " order by receivedate desc, receivetime desc");
        while (rs.next()) {
            String isremark_tmp = Util.null2String(rs.getString("isremark"));
            String isprocessed_tmp = Util.null2String(rs
                    .getString("isprocessed"));
            String userid_tmp = Util.null2String(rs.getString("userid"));
            if ((isremark_tmp.equals("0") && (isprocessed_tmp.equals("2") || isprocessed_tmp
                    .equals("3")))
                    || isremark_tmp.equals("5")) {
                isprocessed = true;
            }
            // 如果是被抄送或转发，判断是否正是某节点的操作人，取最后一次
            if (("8".equals(isremark) || "9".equals(isremark) || "1"
                    .equals(isremark))
                    && userID.equals(userid_tmp)
                    && "0".equals(isremark_tmp)
                    && canContinue == false) {
                int nodeid_tmp = Util.getIntValue(rs.getString("nodeid"), 0);
                if (nodeid_tmp != 0) {
                    isremark = isremark_tmp;
                    nodeid = "" + nodeid_tmp;
                    canContinue = true;
                }
            }
            if (isprocessed == true && canContinue == true) {
                break;
            }
        }
        // 改为按要求显示自定义流程标题的前缀信息
        if ("0".equals(isremark)) {
            rs
                    .executeSql("select nodetitle from workflow_flownode where workflowid="
                            + workflowid + " and nodeid=" + nodeid);
            if (rs.next()) {
                nodetitle = Util.null2String(rs.getString("nodetitle"));
            }
        }
        if (!"".equals(nodetitle) && !"null".equalsIgnoreCase(nodetitle)) {
            nodetitle = "（" + nodetitle + "）";
            requestname = nodetitle + requestname;
        }

        String allrequestids = "";
        currentid = currentid.trim();
        if (!"".equals(currentid)) {
            rs
                    .executeSql("select requestid from workflow_currentoperator where id in("
                            + currentid + ")");
            while (rs.next()) {
                if ("".equals(allrequestids)) {
                    allrequestids = Util.null2String(rs.getString("requestid"));
                } else {
                    allrequestids += ","
                            + Util.null2String(rs.getString("requestid"));
                }
            }
        }

        boolean checkwfshare = false;
        if (!"".equals(allrequestids)) {
            String checkquestids = "," + allrequestids + ",";
            if (checkquestids.indexOf(requestid) > -1) {
                checkwfshare = true;
            }
        }

        String receiver = "1",sendaccount="";
        // sq 主次帐号显示小图标调整
        if(Util.getIntValue(requestid)<0){
            rs.executeQuery("select * from ofs_todo_data where id=?", ofsid);
            rs.next();
            receiver = Util.null2String(rs.getString("receiver"));
            sendaccount = Util.null2String(rs.getString("sendaccount"));
        }
        String wfpic = "mainwf_wev8";
        if(!userID.equals(nowuserid) && Util.getIntValue(requestid)>0){
            // OA 主次不一致
            wfpic = "subwf_wev8";

        }

        if (Util.getIntValue(requestid)<0) {
            // 相等表示本条数据为主账号
            if(sendaccount.isEmpty() || receiver.equals(sendaccount)){
                wfpic = "mainwf_wev8";

            }else{
                wfpic = "subwf_wev8";
                RecordSet rss = new RecordSet();
                rss.executeQuery("select id,departmentid,jobtitle from hrmresource where loginid=?",sendaccount);
                if(rss.next()){
                    depName = dc.getDepartmentname(rss.getString("departmentid"));
                    jobName = jc.getJobTitlesname(rss.getString("jobtitle"));
                }
            }
        }


        if (checkwfshare) {
            returnStr = "<span></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                    + requestid
                    + "&isovertime="
                    + isovertime + paramstr
                    + "\',"
                    + requestid
                    + ") >"
                    + requestname
                    + "</a><span id=\'wflist_"
                    + requestid
                    + "span\'></span>";
        } else {
            if (viewtype.equals("0")) {
                // 新流程,粗体链接加图片
                if (isprocessed) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19081,
                            userlang) + "'/></span>";
                } else if ("1".equals(agenttype)) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ") >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19154,
                            userlang) + "'/></span>";
                } else {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19154,
                            userlang) + "'/></span>";
                }
            } else if (viewtype.equals("-1")) {
                // 旧流程,有新的提交信息未查看,普通链接加图片
                if (isprocessed) {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19081,
                            userlang) + "'/></span>";
                } else {
                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(20288,
                            userlang) + "'/></span>";
                }
            } else {
                // 旧流程,普通链接
                if (isprocessed) {

                    // System.out.println("--usrid2-1615-"+usrid2);

                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ");doReadIt("
                            + requestid
                            + ",\"\",this); >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid
                            + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                            + SystemEnv.getHtmlLabelName(19081,
                            userlang) + "'/></span>";

                } else {

                    // System.out.println("--usrid2-1615-"+usrid2);

                    returnStr = "<span><img src='/images/ecology8/" + wfpic + ".png' title='"
                            + depName
                            + "/"
                            + jobName
                            + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                            + requestid
                            + "&f_weaver_belongto_userid="
                            + userID
                            + "&f_weaver_belongto_usertype=0&isovertime="
                            + isovertime + paramstr
                            + "\',"
                            + requestid
                            + ") >"
                            + requestname
                            + "</a><span id=\'wflist_"
                            + requestid + "span\'></span>";

                }
            }
        }


        return returnStr;
    }


    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     *
     * @param requestname
     *            主次账号图标
     * @param para2
     *            请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid +
     *            操作人类性isremark+用户id userID+ 代理人/被代理人ID + 代理/被代理类型
     * @return 流程标题显示样式
     * @throws Exception
     */
    public String getWfMainSubPic(String requestname, String para2)
            throws Exception {
        ResourceComInfo rc = new ResourceComInfo();
        DepartmentComInfo dc = new DepartmentComInfo();
        JobTitlesComInfo jc = new JobTitlesComInfo();
        String returnStr = "";

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime = Util.getIntValue(tempStr[3], 0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]), 7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String usrid2 = Util.null2String(tempStr[11]);
        String[] arr2 = null;
        ArrayList<String> userlist = new ArrayList();
        int Belongtoid = 0;
        if (!"".equals(usrid2)) {
            arr2 = usrid2.split(",");
            for (int i = 0; i < arr2.length; i++) {
                Belongtoid = Util.getIntValue(arr2[i]);
                userlist.add(Belongtoid + "");
            }
        }

        for (int k = 0; k < userlist.size(); k++) {
            String userid2 = Util.null2String((String) userlist.get(k));
            String username = rc.getResourcename((String) userlist.get(k));
            String ownDepid = rc.getDepartmentID((String) userlist.get(k));
            String depName = dc.getDepartmentname(ownDepid);
            String jobName = jc.getJobTitlesname(rc
                    .getJobTitle((String) userlist.get(k)));
            // System.out.println("-----1393----workflowtm---jobName==="+jobName);
            String agentorbyagentid = "";
            String agenttype = "";

            if (tempStr.length >= 10) {
                agenttype = Util.null2String(tempStr[9]);
            }

            String nodetitle = "";
            int isbill = 0;
            int formid = 0;
            RecordSet rs = new RecordSet();
            // //根据后台设置在MAIL标题后加上流程中重要的字段
            rs.execute("select formid,isbill from workflow_base where id="
                    + workflowid);
            if (rs.next()) {
                formid = rs.getInt(1);
                isbill = rs.getInt(2);

            }
            MailAndMessage mailTitle = new MailAndMessage();
            String titles = mailTitle.getTitle(Util.getIntValue(requestid, -1),
                    Util.getIntValue(workflowid, -1), formid, userlang, isbill);
            if (!titles.equals(""))
                requestname = requestname + "<B>（" + titles + "）</B>";

            boolean isprocessed = false;
            boolean canContinue = false;
            rs
                    .executeSql("select isprocessed, isremark, userid, nodeid from workflow_currentoperator where requestid = "
                            + requestid
                            + " order by receivedate desc, receivetime desc");
            while (rs.next()) {
                String isremark_tmp = Util
                        .null2String(rs.getString("isremark"));
                String isprocessed_tmp = Util.null2String(rs
                        .getString("isprocessed"));
                String userid_tmp = Util.null2String(rs.getString("userid"));
                if ((isremark_tmp.equals("0") && (isprocessed_tmp.equals("2") || isprocessed_tmp
                        .equals("3")))
                        || isremark_tmp.equals("5")) {
                    isprocessed = true;
                }
                // 如果是被抄送或转发，判断是否正是某节点的操作人，取最后一次
                if (("8".equals(isremark) || "9".equals(isremark) || "1"
                        .equals(isremark))
                        && userID.equals(userid_tmp)
                        && "0".equals(isremark_tmp) && canContinue == false) {
                    int nodeid_tmp = Util
                            .getIntValue(rs.getString("nodeid"), 0);
                    if (nodeid_tmp != 0) {
                        isremark = isremark_tmp;
                        nodeid = "" + nodeid_tmp;
                        canContinue = true;
                    }
                }
                if (isprocessed == true && canContinue == true) {
                    break;
                }
            }
            // 改为按要求显示自定义流程标题的前缀信息
            if ("0".equals(isremark)) {
                rs
                        .executeSql("select nodetitle from workflow_flownode where workflowid="
                                + workflowid + " and nodeid=" + nodeid);
                if (rs.next()) {
                    nodetitle = Util.null2String(rs.getString("nodetitle"));
                }
            }
            if (!"".equals(nodetitle) && !"null".equalsIgnoreCase(nodetitle)) {
                nodetitle = "（" + nodetitle + "）";
                requestname = nodetitle + requestname;
            }

            if (!userID.equals(userid2)) {
                rs
                        .executeSql("select * from workflow_currentoperator where userid="
                                + userid2
                                + " and workflowid="
                                + workflowid
                                + " and nodeid="
                                + nodeid
                                + " and requestid="
                                + requestid);
                if (rs.next()) {

                    if (viewtype.equals("0")) {
                        // 新流程,粗体链接加图片
                        if (isprocessed) {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19081,
                                    userlang) + "'/></span>";
                        } else if ("1".equals(agenttype)) {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ") >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19154,
                                    userlang) + "'/></span>";
                        } else {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19154,
                                    userlang) + "'/></span>";
                        }
                    } else if (viewtype.equals("-1")) {
                        // 旧流程,有新的提交信息未查看,普通链接加图片
                        if (isprocessed) {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19081,
                                    userlang) + "'/></span>";
                        } else {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(20288,
                                    userlang) + "'/></span>";
                        }
                    } else {
                        // 旧流程,普通链接
                        if (isprocessed) {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ");doReadIt("
                                    + requestid
                                    + ",\"\",this); >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid
                                    + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                    + SystemEnv.getHtmlLabelName(19081,
                                    userlang) + "'/></span>";
                        } else {
                            returnStr = "<span><img src='/images/ecology8/subwf_wev8.png' title='"
                                    + depName
                                    + "/"
                                    + jobName
                                    + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                    + requestid
                                    + "&f_weaver_belongto_userid="
                                    + userid2
                                    + "&f_weaver_belongto_usertype=0&isovertime="
                                    + isovertime
                                    + "\',"
                                    + requestid
                                    + ") >"
                                    + requestname
                                    + "</a><span id=\'wflist_"
                                    + requestid + "span\'></span>";
                        }
                    }

                }
            } else {
                if (viewtype.equals("0")) {
                    // 新流程,粗体链接加图片
                    if (isprocessed) {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19081, userlang)
                                + "'/></span>";
                    } else if ("1".equals(agenttype)) {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ") >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19154, userlang)
                                + "'/></span>";
                    } else {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19154, userlang)
                                + "'/></span>";
                    }
                } else if (viewtype.equals("-1")) {
                    // 旧流程,有新的提交信息未查看,普通链接加图片
                    if (isprocessed) {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19081, userlang)
                                + "'/></span>";
                    } else {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDNew2_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(20288, userlang)
                                + "'/></span>";
                    }
                } else {
                    //旧流程,普通链接
                    if (isprocessed) {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ");doReadIt("
                                + requestid
                                + ",\"\",this); >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid
                                + "span\'><img src='/images/ecology8/statusicon/BDOut_wev8.png' title='"
                                + SystemEnv.getHtmlLabelName(19081, userlang)
                                + "'/></span>";
                    } else {
                        returnStr = "<span><img src='/images/ecology8/mainwf_wev8.png' title='"
                                + depName
                                + "/"
                                + jobName
                                + "' /></span><a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid="
                                + requestid
                                + "&f_weaver_belongto_userid="
                                + userid2
                                + "&f_weaver_belongto_usertype=0&isovertime="
                                + isovertime
                                + "\',"
                                + requestid
                                + ") >"
                                + requestname
                                + "</a><span id=\'wflist_"
                                + requestid + "span\'></span>";
                    }
                }
            }
        }
        return returnStr;
    }


    /**
     * 返回正文=
     * @return
     */
    public String getContentNewLinkWithTitle(String requestid,String para2){
        String returnStr = "";
        String[] tempStr = Util.splitString(para2, "+");
        //String requestid = Util.null2String(tempStr[0]);
        String workflowid=Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime=Util.getIntValue(tempStr[3],0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String docids = "";
        RecordSet rs = new RecordSet();
        rs.executeSql("select docids from workflow_requestbase where requestid="+requestid);
        if(rs.next()){
            docids = Util.null2String(rs.getString(1));
        }
        if(!docids.equals("") && !docids.equals("0")){
            if(docids.indexOf(",")==-1){//只有一个文档，直接算为正文
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?viewdoc=1&isworkflowdoc=1&seeflowdoc=1&requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+DocComInfo1.getDocname(docids)+"</a>";
            }else{//多个文档，需要查找到哪个字段是正文字段
                rs.executeSql("select flowDocField from workflow_createdoc where workflowid="+workflowid+" order by id desc");
                if(rs.next()){
                    int fieldid = Util.getIntValue(rs.getString(1),-1);
                    if(fieldid>0){
                        int isbill=0;
                        int formid=0;
                        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
                        if (rs.next())
                        {
                            formid=rs.getInt(1);
                            isbill=rs.getInt(2);

                        }
                        if(isbill==1){//新表单或者单据
                            rs.executeSql("select fieldname from workflow_billfield where id="+fieldid+" and billid="+formid);
                            if(rs.next()){
                                String fieldname = Util.null2String(rs.getString(1));
                                if(!fieldname.equals("")){
                                    if(formid<0){//新表单
                                        rs.executeSql("select "+fieldname+" from formtable_main_"+(formid*-1)+" where requestid="+requestid);
                                        if(rs.next()){
                                            returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?viewdoc=1&isworkflowdoc=1&seeflowdoc=1&requestid=" + requestid+"&isovertime="+isovertime
                                                    + "\',"+requestid+") >"+DocComInfo1.getDocname(""+Util.getIntValue(rs.getString(1),0))+"</a>";
                                        }
                                    }else{//单据
                                        rs.executeSql("select tablename from workflow_bill where id="+formid);
                                        if(rs.next()){
                                            String tablename = Util.null2String(rs.getString(1));
                                            if(!tablename.equals("")){
                                                rs.executeSql("select "+fieldname+" from "+tablename+" where requestid="+requestid);
                                                if(rs.next()){
                                                    returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?viewdoc=1&isworkflowdoc=1&seeflowdoc=1&requestid=" + requestid+"&isovertime="+isovertime
                                                            + "\',"+requestid+") >"+DocComInfo1.getDocname(""+Util.getIntValue(rs.getString(1),0))+"</a>";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else{//老表单
                            rs.executeSql("select fieldname from workflow_formdict where id="+fieldid);
                            if(rs.next()){
                                String fieldname = Util.null2String(rs.getString(1));
                                if(!fieldname.equals("")){
                                    rs.executeSql("select "+fieldname+" from workflow_form where requestid="+requestid);
                                    if(rs.next()){
                                        returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?viewdoc=1&isworkflowdoc=1&seeflowdoc=1&requestid=" + requestid+"&isovertime="+isovertime
                                                + "\',"+requestid+") >"+DocComInfo1.getDocname(""+Util.getIntValue(rs.getString(1),0))+"</a>";
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return returnStr;
    }

    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     * @param requestname 流程标题
     * @param para2 请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid + 操作人类性isremark
     * @return   流程标题显示样式
     */
    public String getWfNewLinkWithTitleExt(String requestname, String para2) {
        String returnStr="";

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid=Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime=Util.getIntValue(tempStr[3],0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String nodetitle = "";
        int isbill=0;
        int formid=0;
        RecordSet rs = new RecordSet();
        ////根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
        if (rs.next())
        {
            formid=rs.getInt(1);
            isbill=rs.getInt(2);

        }
        MailAndMessage mailTitle=new MailAndMessage();
        String titles=mailTitle.getTitle(Util.getIntValue(requestid,-1),Util.getIntValue(workflowid,-1),formid,userlang,isbill);

        if (!titles.equals(""))
            requestname=requestname+"<B>（"+titles+"）</B>";
        boolean isprocessed=false;
        boolean canContinue = false;
        rs.executeSql("select isprocessed, isremark, userid, nodeid from workflow_currentoperator where requestid = " + requestid + " order by receivedate desc, receivetime desc");
        while(rs.next()){
            String isremark_tmp = Util.null2String(rs.getString("isremark"));
            String isprocessed_tmp = Util.null2String(rs.getString("isprocessed"));
            String userid_tmp = Util.null2String(rs.getString("userid"));
            if((isremark_tmp.equals("0") && (isprocessed_tmp.equals("2") || isprocessed_tmp.equals("3"))) || isremark_tmp.equals("5")){
                isprocessed=true;
            }
            //如果是被抄送或转发，判断是否正是某节点的操作人，取最后一次
            if(("8".equals(isremark)||"9".equals(isremark)||"1".equals(isremark)) && userID.equals(userid_tmp) && "0".equals(isremark_tmp) && canContinue == false){
                int nodeid_tmp = Util.getIntValue(rs.getString("nodeid"), 0);
                if(nodeid_tmp != 0){
                    isremark = isremark_tmp;
                    nodeid = "" + nodeid_tmp;
                    canContinue = true;
                }
            }
            if(isprocessed == true && canContinue == true){
                break;
            }
        }
        //改为按要求显示自定义流程标题的前缀信息
        if("0".equals(isremark)){
            rs.executeSql("select nodetitle from workflow_flownode where workflowid="+workflowid+" and nodeid="+nodeid);
            if(rs.next()){
                nodetitle = Util.null2String(rs.getString("nodetitle"));
            }
        }
        if(!"".equals(nodetitle) && !"null".equalsIgnoreCase(nodetitle)){
            nodetitle = "（"+nodetitle+"）";
            requestname = nodetitle + requestname;
        }

        if (viewtype.equals("0")) {
            //新流程,粗体链接加图片

            returnStr = "<a href=javaScript:openWfToTab(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                    + "','"+Util.toHtmlForCpt(requestname)+"')  >"+requestname+"</a>";

        } else if(viewtype.equals("-1")) {
            //旧流程,有新的提交信息未查看,普通链接加图片

            returnStr = "<a href=javaScript:openWfToTab(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                    + "','"+Util.toHtmlForCpt(requestname)+"')  >"+requestname+"</a>";

        }else{

            returnStr = "<a href=javaScript:openWfToTab(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                    + "','"+Util.toHtmlForCpt(requestname)+"')  >"+requestname+"</a>";


        }
        return returnStr;
    }

    public String getWfViewTypeExt(String viewtype,String requestid){
        boolean isprocessed=false;
        RecordSet rs = new RecordSet();
        rs.executeSql("select isprocessed from workflow_currentoperator where ((isremark='0' and (isprocessed='2' or isprocessed='3'))  or isremark='5') and requestid = " + requestid);
        if(rs.next()){
            isprocessed=true;
        }
        String returnStr ="";
        if (viewtype.equals("0")) {
            //新流程,粗体链接加图片
            if(isprocessed){
                returnStr = "<IMG src=\'/images/BDOut_wev8.gif\' align=absbottom>";
            }else{
                returnStr = "<IMG src=\'/images/BDNew_wev8.gif\' align=absbottom>";
            }
        } else if(viewtype.equals("-1")) {
            //旧流程,有新的提交信息未查看,普通链接加图片
            if(isprocessed){
                returnStr = "<IMG src=\'/images/BDOut_wev8.gif\' align=absbottom>";
            }else{
                returnStr = "<IMG src=\'/images/BDNew2_wev8.gif\' align=absbottom>";
            }
        }else{
            //旧流程,普通链接
            if(isprocessed){
                returnStr = "<IMG src='/images/BDOut_wev8.gif' align=absbottom>";
            }else{
                returnStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp";
            }
        }
        return returnStr;
    }

    /**
     * 请求状态图标
     * @param viewtype 请求状态
     * @param para2 请求ID + 代理人/被代理人ID + 代理/被代理类型
     * @return   请求状态图标
     */
    public String getWfViewTypeExtIncludeAgent(String viewtype,String para2){

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String agentorbyagentid=Util.null2String(tempStr[1]);
        String agenttype = Util.null2String(tempStr[2]);


        boolean isprocessed=false;
        RecordSet rs = new RecordSet();
        rs.executeSql("select isprocessed from workflow_currentoperator where ((isremark='0' and (isprocessed='2' or isprocessed='3'))  or isremark='5') and requestid = " + requestid);
        if(rs.next()){
            isprocessed=true;
        }
        String returnStr ="";
        if (viewtype.equals("0")) {
            //新流程,粗体链接加图片
            if(isprocessed){
                returnStr = "<IMG src=\'/images/BDOut_wev8.gif\' align=absbottom>";
            }else if("1".equals(agenttype)){
                returnStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp";
            }else {
                returnStr = "<IMG src=\'/images/BDNew_wev8.gif\' align=absbottom>";
            }
        } else if(viewtype.equals("-1")) {
            //旧流程,有新的提交信息未查看,普通链接加图片
            if(isprocessed){
                returnStr = "<IMG src=\'/images/BDOut_wev8.gif\' align=absbottom>";
            }else{
                returnStr = "<IMG src=\'/images/BDNew2_wev8.gif\' align=absbottom>";
            }
        }else{
            //旧流程,普通链接
            if(isprocessed){
                returnStr = "<IMG src='/images/BDOut_wev8.gif' align=absbottom>";
            }else{
                returnStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp";
            }
        }
        return returnStr;
    }

    /**
     * 得到未操作者
     * @param requestid 流程ID
     * @return 未操作者
     */
    public String getUnOperators(String requestid,String  para) {
        String[] tempStr = Util.splitString(para, "+");

        String userLanguage = Util.null2String(tempStr[0]);

        String userid = Util.null2String(tempStr[1]);
        String mainsubuserid = Util.null2String(tempStr[2]);
        String returnStr="<div id='"+mainsubuserid+requestid+"div'>";
        String divid=mainsubuserid+requestid+"div";
        returnStr += "<span style='cursor:hand;text-decoration: underline' onClick=showallreceived('"+requestid+"','"+divid+"') >"+SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage))+"</span>";
        //returnStr=SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage));
        returnStr+="</div>";
        String showoperators="";
        try
        {

            showoperators=RequestDefaultComInfo.getShowoperator(""+userid);
        }
        catch (Exception eshows)
        {}
        if (showoperators.equals("1"))
        {
            returnStr="";
            RecordSet rs = new RecordSet();
            rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid +" union select distinct userid,0 as usertype,'0' as agenttype, -1 agentorbyagentid from ofs_todo_data where requestid="+requestid+" and isremark='0' and islasttimes=1 ");

            while(rs.next()){
                if(returnStr.equals("")){
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  rc.getResourcename(rs.getString("userid"));
                    }else{
                        returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
                else{
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                    }else{
                        //TD11591(人力资源与客户同时存在时、加','处理)
                        returnStr +=  ","+cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
            }

        }
        return returnStr;
    }


    /**
     * 得到未操作者
     * @param requestid 流程ID
     * @return 未操作者
     */
    public String getUnOperators2(String requestid,String  para) {
        String[] tempStr = Util.splitString(para, "+");

        String userLanguage = Util.null2String(tempStr[0]);

        String userid = Util.null2String(tempStr[1]);
        //String mainsubuserid = Util.null2String(tempStr[2]);
        String returnStr="<div id='"+userid+requestid+"div'>";
        String divid=userid+requestid+"div";
        returnStr += "<span style='cursor:hand;text-decoration: underline' onClick=showallreceived('"+requestid+"','"+divid+"') >"+SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage))+"</span>";
        //returnStr=SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage));
        returnStr+="</div>";
        String showoperators="";
        try
        {

            showoperators=RequestDefaultComInfo.getShowoperator(""+userid);
        }
        catch (Exception eshows)
        {}
        if (showoperators.equals("1"))
        {
            returnStr="";
            RecordSet rs = new RecordSet();
            rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);

            while(rs.next()){
                if(returnStr.equals("")){
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  rc.getResourcename(rs.getString("userid"));
                    }else{
                        returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
                else{
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                    }else{
                        //TD11591(人力资源与客户同时存在时、加','处理)
                        returnStr +=  ","+cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
            }

        }
        return returnStr;
    }


    /**
     * 得到未操作者
     * @param requestid 流程ID
     * @return 未操作者
     */
    public String getMUnOperators(String requestid,String  para) {
        String[] tempStr = Util.splitString(para, "+");

        String userLanguage = Util.null2String(tempStr[0]);

        String userid = Util.null2String(tempStr[1]);
        String returnStr="<div id='"+requestid+"div'>";
        String divid=requestid+"div";
        returnStr += "<span style='cursor:hand;text-decoration: underline' onClick=showallreceived('"+requestid+"','"+divid+"') >"+SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage))+"</span>";
        //returnStr=SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage));
        returnStr+="</div>";
        String showoperators="";
        try
        {

            showoperators=RequestDefaultComInfo.getShowoperator(""+userid);
        }
        catch (Exception eshows)
        {}
        if (showoperators.equals("1"))
        {
            returnStr="";
            RecordSet rs = new RecordSet();
            rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);

            while(rs.next()){
                if(returnStr.equals("")){
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  rc.getResourcename(rs.getString("userid"));
                    }else{
                        returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
                else{
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                    }else{
                        //TD11591(人力资源与客户同时存在时、加','处理)
                        returnStr +=  ","+cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
            }

        }
        return returnStr;
    }

    /**
     * 得到流程报表未操作者
     * @param requestid 流程ID
     * @return 未操作者
     */
    public String getUnOptInRep(String requestid) {

        String returnStr = "";

        //returnStr ="<a href=\"javaScript:openhrm("+ id+ ");\" onclick='pointerXY(event);'>"+ rc.getResourcename(id)+ "</a>";
        RecordSet rs = new RecordSet();
        rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);

        String usid = "";
        String usname = "";
        while(rs.next()){
            if(returnStr.equals("")){
                if(rs.getInt("usertype")==0){
                    if(rs.getInt("agenttype")==2){
                        usid = rs.getString("agentorbyagentid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  "<a href=\"javaScript:openhrm("+ usid+ ");\" onclick='pointerXY(event);'>" + usname + "</a>->";
                        returnStr +=  "<a href=\"javaScript:openhrm("+ rs.getString("userid")+ ");\" onclick='pointerXY(event);'>"+ rc.getResourcename("userid")+ "</a>";
                        //returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                    }
                    else{
                        usid = rs.getString("userid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  "<a href=\"javaScript:openhrm("+ usid+ ");\" onclick='pointerXY(event);'>" + usname + "</a>";
                    }
                }else{
                    usid = rs.getString("userid");
                    usname = cci.getCustomerInfoname(usid);
                    returnStr +=  "<a href=\"javaScript:openhrm("+ usid+ ");\" onclick='pointerXY(event);'>" + usname + "</a>";
                    //returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                }
            }
            else{
                if(rs.getInt("usertype")==0){
                    if(rs.getInt("agenttype")==2){
                        usid = rs.getString("agentorbyagentid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  ",<a href=\"javaScript:openhrm("+ usid+ ");\" onclick='pointerXY(event);'>" + usname + "</a>->";
                        returnStr +=  "<a href=\"javaScript:openhrm("+ rs.getString("userid")+ ");\" onclick='pointerXY(event);'>"+ rc.getResourcename("userid")+ "</a>";
                        //returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                    }
                    else{
                        usid = rs.getString("userid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  ",<a href=\"javaScript:openhrm("+ usid+ ");\" onclick='pointerXY(event);'>" + usname + "</a>";
                        //returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                    }
                }else{
                    //TD11591(人力资源与客户同时存在时、加','处理)
                    usid = rs.getString("userid");
                    usname = cci.getCustomerInfoname(usid);
                    returnStr +=  ",<a href=\"javaScript:openhrm("+ usid+ ");\" onclick='pointerXY(event);'>" + usname + "</a>";
                    //returnStr +=  ","+cci.getCustomerInfoname(rs.getString("userid"));
                }
            }
        }

        return returnStr;
    }

    /**
     * 得到流程报表导出未操作者
     * @param requestid 流程ID
     * @return 未操作者
     */
    public String getUnOptOutPutExcel(String requestid) {

        String returnStr = "";

        //returnStr ="<a href=\"javaScript:openhrm("+ id+ ");\" onclick='pointerXY(event);'>"+ rc.getResourcename(id)+ "</a>";
        RecordSet rs = new RecordSet();
        rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);

        String usid = "";
        String usname = "";
        while(rs.next()){
            if(returnStr.equals("")){
                if(rs.getInt("usertype")==0){
                    if(rs.getInt("agenttype")==2){
                        usid = rs.getString("agentorbyagentid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  usname + "->";
                        returnStr +=  rc.getResourcename("userid");
                        //returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                    }
                    else{
                        usid = rs.getString("userid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  usname;
                    }
                }else{
                    usid = rs.getString("userid");
                    usname = cci.getCustomerInfoname(usid);
                    returnStr +=  usname;
                    //returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                }
            }
            else{
                if(rs.getInt("usertype")==0){
                    if(rs.getInt("agenttype")==2){
                        usid = rs.getString("agentorbyagentid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  "," + usname + "->";
                        returnStr +=   rc.getResourcename("userid");
                        //returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                    }
                    else{
                        usid = rs.getString("userid");
                        usname = rc.getResourcename(usid);
                        returnStr +=  "," + usname;
                        //returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                    }
                }else{
                    //TD11591(人力资源与客户同时存在时、加','处理)
                    usid = rs.getString("userid");
                    usname = cci.getCustomerInfoname(usid);
                    returnStr +=  "," + usname;
                    //returnStr +=  ","+cci.getCustomerInfoname(rs.getString("userid"));
                }
            }
        }

        return returnStr;
    }

    public String getUnOperatorsExt(String requestid,String  para) {
        String[] tempStr = Util.splitString(para, "+");
        String userLanguage = Util.null2String(tempStr[0]);
        String userid = Util.null2String(tempStr[1]);
        String returnStr="<div id='"+requestid+"div'>";
        String divid=requestid+"div";
        returnStr += "<span style='cursor:hand;color: blue; text-decoration: underline' onClick=showallreceived('"+requestid+"','"+divid+"',this) >"+SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage))+"</span>";
        //returnStr=SystemEnv.getHtmlLabelName(89, Util.getIntValue(userLanguage));
        returnStr+="</div>";

        String showoperators="";
        try
        {

            showoperators=RequestDefaultComInfo.getShowoperator(""+userid);
        }
        catch (Exception eshows)
        {}
        if (showoperators.equals("1"))
        {
            returnStr="";
            RecordSet rs = new RecordSet();
            rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1') or (isremark='4' and viewtype=0))  and requestid = " + requestid);

            while(rs.next()){
                if(returnStr.equals("")){
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  rc.getResourcename(rs.getString("userid"));
                    }else{
                        returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
                else{
                    if(rs.getInt("usertype")==0){
                        if(rs.getInt("agenttype")==2)
                            returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                        else
                            returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                    }else{
                        //TD11591(人力资源与客户同时存在时、加','处理)
                        returnStr +=  ","+cci.getCustomerInfoname(rs.getString("userid"));
                    }
                }
            }

        }

        return returnStr;
    }

    public String getWfMonLink(String requestname, String requestid) {
        String returnStr = "<a href='/workflow/request/ViewRequest.jsp?ismonitor=y&requestid=" + requestid+"' target='_newworks'>"+requestname+"</a>";
        return returnStr;
    }

    public String getFlowPendingLink(String requestname, String requestid) {
        String returnStr = "<a href='/workflow/request/ViewRequest.jsp?isfromflowreport=1&reportid=-2&requestid=" + requestid+"' target='_newworks'>"+requestname+"</a>";
        return returnStr;
    }
    public String getFlowDocLink(String requestname, String requestid) {
        String returnStr = "<a href='/workflow/request/ViewRequest.jsp?isfromflowreport=1&reportid=-6&requestid=" + requestid+"' target='_newworks'>"+requestname+"</a>";
        return returnStr;
    }

    /**
     * 得到当前节点的显示名
     * @param currentnodeid 节点ID
     * @return 当前节点的显示名
     */
    public String getCurrentNode(String currentnodeid) {
        String returnStr="";

        if(!"".equals(currentnodeid)) {
            RecordSet rs = new RecordSet();
            rs.executeSql("select nodename from workflow_nodebase where id= " + currentnodeid);

            if(rs.next()){
                returnStr =  rs.getString("nodename");
            }
        }

        return returnStr;
    }


    /**
     * 展示流程监控列表操作菜单
     * add by 黄宝 @2014-1-15=
     * @return
     *
     */
    @Deprecated
    public List<String> getWFMonitorListOperation(String workflowid_rs, String para2)
    {
        String showDeleteBtn = "false";	//显示删除按钮
        String showStopBtn = "false";	//显示暂停按钮
        String showCancelBtn = "false";		//显示撤销按钮
        String showRestartBtn = "false";	//显示启用按钮
        String showOvmBtn = "false";	//显示强制归档按钮
        String showRbmBtn = "false"; //显示强制收回按钮
        String showInterventionBtn = "false";	//显示流程干预按钮

        //记录返回结果
        List<String> result = new ArrayList<String>();


        String[] tempStr = Util.splitString(para2, "+");
        String workflowid= "";
        int formid = 0;
        int isbill = 0;
        String requestid = Util.null2String(tempStr[0]);
        int userid = Integer.parseInt(Util.null2String(tempStr[1]));
        int usertype = Integer.parseInt(Util.null2String(tempStr[2]));
        int userlang = Integer.parseInt(Util.null2String(tempStr[3]));
        int currentstatus = Util.getIntValue((Util.null2String(tempStr[4])),-1);
        int creater = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        int currentnodetype = Util.getIntValue((Util.null2String(tempStr[6])),-1);
//        String isNew = "";
//        try{
//        	isNew = Util.null2String(tempStr[4]);
//        }catch(Exception e){
//        }

        boolean isForceDrawBack=false;
        boolean isForceOver=false;
        boolean issooperator = false;
        RecordSet rs = new RecordSet();
        //rs.executeSql("select isForceDrawBack,isForceOver,issooperator from workflow_monitor_bound where monitorhrmid="+userid+" and EXISTS(select 1 from workflow_requestbase where currentnodetype!='3' and workflowid=workflow_monitor_bound.workflowid and requestid="+requestid+")");
        rs.executeSql("select isForceDrawBack,isForceOver,issooperator from workflow_monitor_bound where monitorhrmid="+userid+" and EXISTS(select 1 from workflow_requestbase where currentnodetype!='3' and workflowid=workflow_monitor_bound.workflowid and requestid="+requestid+")");
        while (rs.next()) {
            if(!isForceDrawBack)
                isForceDrawBack=Util.getIntValue(rs.getString("isForceDrawBack"))==1?true:false;
            if(!isForceOver)
                isForceOver=Util.getIntValue(rs.getString("isForceOver"))==1?true:false;
            if(!issooperator)
                issooperator=Util.getIntValue(rs.getString("issooperator"))==1?true:false;
        }
        rs.executeSql("select workflowid from workflow_requestbase where requestid="+requestid);
        if(rs.next())
        {
            workflowid = rs.getString("workflowid");
        }
        WfFunctionManageUtil wfFunctionManageUtil=new WfFunctionManageUtil();

        User user = new User();
        user.setUid(userid);
        WfFunctionManageUtil WfFunctionManageUtil = new WfFunctionManageUtil();
        boolean haveStopright = WfFunctionManageUtil.haveStopright(currentstatus,creater,user,""+currentnodetype,-1,issooperator);//流程不为暂停或者撤销状态，当前用户为流程发起人或者系统管理员，并且流程状态不为创建和归档
        boolean haveCancelright = WfFunctionManageUtil.haveCancelright(currentstatus,creater,user,""+currentnodetype,-1,issooperator);//流程不为撤销状态，当前用户为流程发起人，并且流程状态不为创建和归档
        boolean haveRestartright = WfFunctionManageUtil.haveRestartright(currentstatus,creater,user,""+currentnodetype,-1,issooperator);//流程为暂停或者撤销状态，当前用户为系统管理员，并且流程状态不为创建和归档

        haveStopright = haveStopright&&!((formid==158||formid==156)&&isbill==1);
        haveCancelright = haveCancelright&&!((formid==158||formid==156)&&isbill==1);
        haveRestartright = haveRestartright&&!((formid==158||formid==156)&&isbill==1);
        //判断是否有删除权限
        String delStr = workflowid+"+"+userid+"+"+usertype;
        boolean haveDelright = getWFMonitorCheckBox(delStr).equals("true")?true:false;

        if(haveDelright){
            showDeleteBtn = "true";
        }

        ////System.out.println("haveStopright : "+haveStopright+" haveCancelright : "+haveCancelright+" haveRestartright : "+haveRestartright+" currentstatus : "+currentstatus+" currentnodetype : "+currentnodetype+" creater : "+creater+" userid : "+userid);
        if(haveStopright)
        {
            //returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','stop')\">"+SystemEnv.getHtmlLabelName(20387, userlang)+"</a>";
            showStopBtn = "true";

        }
        if(haveCancelright)
        {
            //returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','cancel')\">"+SystemEnv.getHtmlLabelName(16210, userlang)+"</a>";
            showCancelBtn = "true";
        }
        if(haveRestartright)
        {
            //returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','restart')\">"+SystemEnv.getHtmlLabelName(18095, userlang)+"</a>";
            showRestartBtn = "true";
        }
        if(!haveRestartright||((formid==158||formid==156)&&isbill==1))
        {
            HashMap map = wfFunctionManageUtil.wfFunctionMonitorByNodeid(workflowid,userid+"");
            String ov = (String)map.get("ov");
            String rb = (String)map.get("rb");
            WfForceOver wfForceOver=new WfForceOver();
            if(isForceOver&&"1".equals(ov) && !wfForceOver.isOver(Integer.parseInt(requestid))){
                //      	returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:window.location='/workflow/workflow/wfFunctionManageLink.jsp?flag=ovm&requestid="+requestid+"&isNew="+isNew+"'\">"+SystemEnv.getHtmlLabelName(18360, userlang)+"</a>";
                //returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','ovm')\">"+SystemEnv.getHtmlLabelName(18360, userlang)+"</a>";
                //ishead=false;
                showOvmBtn = "true";//强制归档
            }
            int tempuser = 1;
            int tempusertype = 0;
            rs.executeSql("select userid,usertype from workflow_currentoperator where requestid = " + requestid + " and isremark = '2' order by operatedate desc ,operatetime desc");
            if(rs.next()){
                tempuser = rs.getInt("userid");
                tempusertype = rs.getInt("usertype");
            }
            int nodecounts = 0;//流程没有真正流转，没有强制收回
            rs.executeSql("select count(distinct nodeid) as nodecounts from workflow_currentoperator where requestid="+requestid);
            if(rs.next()){
                nodecounts = rs.getInt("nodecounts");
            }
            WfForceDrawBack wfForceDrawBack=new WfForceDrawBack();
            //现改为查看后也能收回，条件是对该流程有监控权限、不在发起与归档节点、在后台设置强制收回权限
            //if(nodecounts>1&&isForceDrawBack&&!"0".equals(rb) && wfForceDrawBack.isHavePurview(Integer.parseInt(requestid),userid,usertype,tempuser,tempusertype)){
            if(nodecounts>1&&isForceDrawBack&&!"0".equals(rb)){
                //returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','rbm')\">"+SystemEnv.getHtmlLabelName(18359, userlang)+"</a>";
                showRbmBtn = "true"; //强制收回
            }
            if(GCONST.getWorkflowIntervenorByMonitor()){
                rs.executeSql("select workflowid from workflow_monitor_bound where monitorhrmid="+userid+" and isintervenor='1' and EXISTS(select 1 from workflow_requestbase where currentnodetype!='3' and workflowid=workflow_monitor_bound.workflowid and requestid="+requestid+")");
                if(rs.next()){
                    //returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:openFullWindowHaveBar('/workflow/request/ViewRequest.jsp?isintervenor=1&requestid="+requestid+"')\">"+SystemEnv.getHtmlLabelName(18913, userlang)+"</a>";
                    showInterventionBtn = "true";//流程干预
                }
            }
        }


        result.add(showDeleteBtn);
        result.add(showStopBtn);
        result.add(showCancelBtn);
        result.add(showRestartBtn);
        result.add(showOvmBtn);
        result.add(showRbmBtn);
        result.add(showInterventionBtn);

        return result;
    }


    /**
     * 表格中显示强制归档和强制回收
     * @param workflowid_rs
     * @param para2 流程id+用户ID+用户类型+用户显示语言
     * @return 强制归档和强制回收显示
     */
    public String getMonitorLink(String workflowid_rs, String para2) {
        String returnStr="";

        String[] tempStr = Util.splitString(para2, "+");
        String workflowid= "";
        int formid = 0;
        int isbill = 0;
        String requestid = Util.null2String(tempStr[0]);
        int userid = Integer.parseInt(Util.null2String(tempStr[1]));
        int usertype = Integer.parseInt(Util.null2String(tempStr[2]));
        int userlang = Integer.parseInt(Util.null2String(tempStr[3]));
        int currentstatus = Util.getIntValue((Util.null2String(tempStr[4])),-1);
        int creater = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        int currentnodetype = Util.getIntValue((Util.null2String(tempStr[6])),-1);
//        String isNew = "";
//        try{
//        	isNew = Util.null2String(tempStr[4]);
//        }catch(Exception e){
//        }

        returnStr="";
        boolean ishead=true;
        boolean isForceDrawBack=false;
        boolean isForceOver=false;
        boolean issooperator = false;
        RecordSet rs = new RecordSet();
        rs.executeSql("select workflowid from workflow_requestbase where requestid="+requestid);
        if(rs.next())
        {
            workflowid = rs.getString("workflowid");
        }
        Monitor monitor = new Monitor();
        MonitorDTO monitorInfo = monitor.getMonitorInfo(userid+"", creater+"", workflowid);
        isForceDrawBack = monitorInfo.getIsforcedrawback();
        isForceOver = monitorInfo.getIsforceover();
        issooperator = monitorInfo.getIssooperator();

        WfFunctionManageUtil wfFunctionManageUtil=new WfFunctionManageUtil();

        User user = new User();
        user.setUid(userid);
        WfFunctionManageUtil WfFunctionManageUtil = new WfFunctionManageUtil();
        boolean haveStopright = WfFunctionManageUtil.haveStopright(currentstatus,creater,user,""+currentnodetype,-1,issooperator);//流程不为暂停或者撤销状态，当前用户为流程发起人或者系统管理员，并且流程状态不为创建和归档
        boolean haveCancelright = WfFunctionManageUtil.haveCancelright(currentstatus,creater,user,""+currentnodetype,-1,issooperator);//流程不为撤销状态，当前用户为流程发起人，并且流程状态不为创建和归档
        boolean haveRestartright = WfFunctionManageUtil.haveRestartright(currentstatus,creater,user,""+currentnodetype,-1,issooperator);//流程为暂停或者撤销状态，当前用户为系统管理员，并且流程状态不为创建和归档

        haveStopright = haveStopright&&!((formid==158||formid==156)&&isbill==1);
        haveCancelright = haveCancelright&&!((formid==158||formid==156)&&isbill==1);
        haveRestartright = haveRestartright&&!((formid==158||formid==156)&&isbill==1);

        ////System.out.println("haveStopright : "+haveStopright+" haveCancelright : "+haveCancelright+" haveRestartright : "+haveRestartright+" currentstatus : "+currentstatus+" currentnodetype : "+currentnodetype+" creater : "+creater+" userid : "+userid);
        if(haveStopright)
        {
            if(ishead){
                returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','stop')\">"+SystemEnv.getHtmlLabelName(20387, userlang)+"</a>";
                ishead=false;
            }else{
                returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','stop')\">"+SystemEnv.getHtmlLabelName(20387, userlang)+"</a>";
                ishead=false;
            }

        }
        if(haveCancelright)
        {
            if(ishead){
                returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','cancel')\">"+SystemEnv.getHtmlLabelName(16210, userlang)+"</a>";
                ishead=false;
            }else{
                returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','cancel')\">"+SystemEnv.getHtmlLabelName(16210, userlang)+"</a>";
                ishead=false;
            }
        }
        if(haveRestartright)
        {
            if(ishead){
                returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','restart')\">"+SystemEnv.getHtmlLabelName(18095, userlang)+"</a>";
                ishead=false;
            }else{
                returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','restart')\">"+SystemEnv.getHtmlLabelName(18095, userlang)+"</a>";
                ishead=false;
            }
        }
        if(!haveRestartright||((formid==158||formid==156)&&isbill==1))
        {
            HashMap map = wfFunctionManageUtil.wfFunctionManageAsMonitor(Integer.parseInt(workflowid_rs));
            String ov = (String)map.get("ov");
            String rb = (String)map.get("rb");
            WfForceOver wfForceOver=new WfForceOver();
            if(isForceOver&&"1".equals(ov) && !wfForceOver.isOver(Integer.parseInt(requestid))){
                //      	returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:window.location='/workflow/workflow/wfFunctionManageLink.jsp?flag=ovm&requestid="+requestid+"&isNew="+isNew+"'\">"+SystemEnv.getHtmlLabelName(18360, userlang)+"</a>";
                returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','ovm')\">"+SystemEnv.getHtmlLabelName(18360, userlang)+"</a>";
                ishead=false;
            }
            int tempuser = 1;
            int tempusertype = 0;
            rs.executeSql("select userid,usertype from workflow_currentoperator where requestid = " + requestid + " and isremark = '2' order by operatedate desc ,operatetime desc");
            if(rs.next()){
                tempuser = rs.getInt("userid");
                tempusertype = rs.getInt("usertype");
            }
            int nodecounts = 0;//流程没有真正流转，没有强制收回
            rs.executeSql("select count(distinct nodeid) as nodecounts from workflow_currentoperator where requestid="+requestid);
            if(rs.next()){
                nodecounts = rs.getInt("nodecounts");
            }
            WfForceDrawBack wfForceDrawBack=new WfForceDrawBack();
            if(nodecounts>1&&isForceDrawBack&&!"0".equals(rb) && wfForceDrawBack.isHavePurview(Integer.parseInt(requestid),userid,usertype,tempuser,tempusertype)){
                if(ishead){
                    //                 returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:window.location='/workflow/workflow/wfFunctionManageLink.jsp?flag=rbm&requestid="+requestid+"&isNew="+isNew+"'\">"+SystemEnv.getHtmlLabelName(18359, userlang)+"</a>";
                    returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','rbm')\">"+SystemEnv.getHtmlLabelName(18359, userlang)+"</a>";
                    ishead=false;
                }else{
                    //                 returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:window.location='/workflow/workflow/wfFunctionManageLink.jsp?flag=rbm&requestid="+requestid+"&isNew="+isNew+"'\">"+SystemEnv.getHtmlLabelName(18359, userlang)+"</a>";
                    returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:doMonitorRequestSignle('"+requestid+"','rbm')\">"+SystemEnv.getHtmlLabelName(18359, userlang)+"</a>";
                    ishead=false;
                }
            }
            if(GCONST.getWorkflowIntervenorByMonitor()){
                if(monitorInfo.getIsintervenor() && currentnodetype != 3){
                    if(ishead){
                        returnStr+="<table><tr><td height=20><a href=\"#\" onclick=\"javaScript:openFullWindowHaveBar('/workflow/request/ViewRequest.jsp?isintervenor=1&requestid="+requestid+"')\">"+SystemEnv.getHtmlLabelName(18913, userlang)+"</a>";
                        ishead=false;
                    }else{
                        returnStr+="</td></tr><tr><td height=20><a href=\"#\" onclick=\"javaScript:openFullWindowHaveBar('/workflow/request/ViewRequest.jsp?isintervenor=1&requestid="+requestid+"')\">"+SystemEnv.getHtmlLabelName(18913, userlang)+"</a>";
                        ishead=false;
                    }
                }
            }
        }
        if(!ishead) returnStr+="</td></tr></table>";
        //System.out.println("======="+returnStr);
        return returnStr;
    }
    /**
     * 获得子流程链接
     * @param requestId
     * @param userLanguage 用户显示语言
     * @return 子流程链接
     */
    public String getSubWFLink(String requestId, String userLanguage) {
        int intUserLanguage=7;
        if(userLanguage!=null&&!userLanguage.equals("")){
            intUserLanguage=Integer.parseInt(userLanguage);
        }
        String returnStr="";
        RecordSet rs = new RecordSet();
        rs.executeSql("select requestid from workflow_requestbase where mainRequestId="+requestId);
        if(rs.next()){
            returnStr="<a href=\"/workflow/search/SubWFSearchResult.jsp?mainRequestId="+requestId+"\" target=\'_new\'>"+SystemEnv.getHtmlLabelName(361, intUserLanguage)+"</a>";
        }
        return returnStr;
    }

    /**
     * 获得子流程链接
     * @param requestId
     * @param userLanguage 用户显示语言
     * @return 子流程链接
     */
    public String getSubWFLinkNew(String requestId, String userLanguage) {
        int intUserLanguage=7;
        if(userLanguage!=null&&!userLanguage.equals("")){
            intUserLanguage=Integer.parseInt(userLanguage);
        }
        String returnStr="<a href=\"/workflow/search/SubWFSearchResult.jsp?isNew=2New&mainRequestId="+requestId+"\">"+SystemEnv.getHtmlLabelName(361, intUserLanguage)+"</a>";

        return returnStr;
    }

    /**
     * 获得取消监控链接
     * @param workflowid
     * @param hrmiduserLanguage 监控人
     * @return 取消监控链接
     */
    public String getCancleMoniter(String workflowid, String hrmiduserLanguage) {
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String userLanguage = Util.null2String(tempStr[1]);
        String userid = Util.null2String(tempStr[2]);
        int intUserLanguage=7;
        if(userLanguage!=null&&!userLanguage.equals("")){
            intUserLanguage=Integer.parseInt(userLanguage);
        }
        int operatelevel = 0;
        if(tempStr.length>3)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[3]),-1);
        }
        int detachable=0;
        if(tempStr.length>4)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        String typeid = "";
        if(tempStr.length>5)
        {
            typeid = Util.null2String(tempStr[5]);
        }
        String subcompanyid = "";
        if(tempStr.length>6)
        {
            subcompanyid = Util.null2String(tempStr[6]);
        }
        String returnStr="";

        if(userid.equals("1")){//系统管理员
            returnStr="<a href=\"/system/systemmonitor/workflow/systemMonitorOperation.jsp?monitorhrmid="+hrmid+"&actionKey=delflow&flowid="+workflowid+"&subcompanyid="+subcompanyid+"&typeid="+typeid+"\">"+SystemEnv.getHtmlLabelName(201,intUserLanguage)+SystemEnv.getHtmlLabelName(665,intUserLanguage)+"</a>";
        }else{
    	    /*rs.executeSql("select detachable from SystemSet");
    	    int detachable=0;
    	    if(rs.next()){
    	        detachable=rs.getInt("detachable");
    	    }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel>0){//有权限
                    returnStr="<a href=\"/system/systemmonitor/workflow/systemMonitorOperation.jsp?monitorhrmid="+hrmid+"&actionKey=delflow&flowid="+workflowid+"&subcompanyid="+subcompanyid+"&typeid="+typeid+"\">"+SystemEnv.getHtmlLabelName(201,intUserLanguage)+SystemEnv.getHtmlLabelName(665,intUserLanguage)+"</a>";
                }else{//无权限
                    returnStr=SystemEnv.getHtmlLabelName(201,intUserLanguage)+SystemEnv.getHtmlLabelName(665,intUserLanguage);
                }
            }else{
                returnStr="<a href=\"/system/systemmonitor/workflow/systemMonitorOperation.jsp?monitorhrmid="+hrmid+"&actionKey=delflow&flowid="+workflowid+"&subcompanyid="+subcompanyid+"&typeid="+typeid+"\">"+SystemEnv.getHtmlLabelName(201,intUserLanguage)+SystemEnv.getHtmlLabelName(665,intUserLanguage)+"</a>";
            }
        }
        //String returnStr="<a href=\"/workflow/search/SubWFSearchResult.jsp?mainRequestId="+requestId+"\">"+SystemEnv.getHtmlLabelName(201,intUserLanguage)+SystemEnv.getHtmlLabelName(665,intUserLanguage)+"</a>";

        return returnStr;
    }
    /**
     * 获得子流程列表的接收人
     * 子流程第二个节点（创建节点后节点）的操作人，当节点操作人为多个时显示多个
     * @param requestId  子流程的请求id
     * @return 子流程列表的接收人
     */
    public String getSubWFReceiver(String requestId) {
        String returnStr="";

        int workflowId=0;
        int firstNodeId=0;
        int secondNodeId=0;
        String secondNodeIdStr="0";
        int userId=0;
        int userType=0;

        RecordSet rs = new RecordSet();
        rs.executeSql(" select workflowid from workflow_requestbase where requestid="+requestId);
        if(rs.next()){
            workflowId=rs.getInt(1);
        }

        rs.executeSql("select nodeId from workflow_flownode where workflowid="+workflowId+" and nodeType='0'");
        if(rs.next()){
            firstNodeId=rs.getInt(1);
        }

        rs.executeSql("select destnodeid from workflow_nodelink where wfrequestid is null and workflowId="+workflowId+" and EXISTS(select 1 from workflow_nodebase b where workflow_nodelink.destnodeid=b.id and (b.IsFreeNode is null or b.IsFreeNode!='1')) and nodeId="+firstNodeId);
        while(rs.next()){
            secondNodeId=rs.getInt(1);
            secondNodeIdStr+=","+secondNodeId;
        }

        rs.executeSql(" select userId,userType from workflow_currentoperator where requestid="+requestId+"   and nodeId in("+secondNodeIdStr+") ");
        while(rs.next()){
            userId=rs.getInt(1);
            userType=rs.getInt(2);
            if(userType==0){
                returnStr +=" "+
                        "<a href=\"javaScript:openhrm("
                        + userId
                        + ")\" onclick='pointerXY(event);'>"
                        + rc.getResourcename(String.valueOf(userId))
                        + "</a>";
            }else if(userType==1){
                returnStr +=
                        "<a href=\"javaScript:openFullWindowHaveBar(\'/CRM/data/ViewCustomer.jsp?CustomerID="
                                + userId
                                + "\')\">"
                                + cci.getCustomerInfoname(String.valueOf(userId))
                                + "</a>";
            }
        }

        return returnStr;
    }

    /**
     * 获得子流程列表的请求说明
     * @param requestName  子流程的请求id
     * @param paraTwo  流程id+用户ID
     * 当前用户不是子流程操作人时，只显示名称，不能链接进入，否则可通过链接进入流程查看页面
     * @return 子流程列表的请求说明
     */
    public String getSubWFRequestDescription (String requestName,String paraTwo) {

        String returnStr="";

        String[] tempStr = Util.splitString(paraTwo, "+");
        String requestId = Util.null2String(tempStr[0]);
        String userId = Util.null2String(tempStr[1]);

        boolean canView=false;
        RecordSet rs = new RecordSet();
        rs.executeSql("select requestId from workflow_currentoperator where requestid="+requestId+" and userId="+userId);
        if(rs.next()){
            canView=true;
        }
        if(canView){
            returnStr = "<a href=javaScript:openFullWindowHaveBar(\'/workflow/request/ViewRequest.jsp?requestid=" + requestId
                    + "\')>"+requestName+"</a>";
        }else{
            returnStr=requestName;
        };

        return returnStr;
    }
    /**
     * 获得自定义显示字段的显示内容
     * @param paraTwo  字段ID+"+"+字段类型+"+"+字段类型+"+"+显示语言
     * @return 显示内容
     */
    public String getOthers (String fieldvalue,String paraTwo)
    {
        String returnStr="";
        String sql="";
        String[] tempStr = Util.splitString(paraTwo, "+");
        String fieldid = Util.null2String(tempStr[1]);
        String fieldhtmltype = Util.null2String(tempStr[2]);
        String fieldtype = Util.null2String(tempStr[3]);
        int userlanguage = Util.getIntValue(Util.null2String(tempStr[4]));
        String requestid= Util.null2String(tempStr[0]);
        char flag = Util.getSeparator() ;
        String isbill=Util.null2String(tempStr[5]);
        String dbtype=Util.null2String(tempStr[6]);

        RecordSet rs = new RecordSet();
        RecordSet RecordSet = new RecordSet();
        try{
            if (fieldtype.equals("118")) {
                returnStr="<a href=\'/meeting/report/MeetingRoomPlan.jsp\' target=\'_blank\'>"+SystemEnv.getHtmlLabelName(2193,userlanguage)+"</a>";
            }
            if(fieldhtmltype.equals("1") || fieldhtmltype.equals("2") ){  // 单行,多行文本框

                if(fieldhtmltype.equals("1") && fieldtype.equals("4")){
                    returnStr= Util.milfloatFormat(fieldvalue);
                    //returnStr=  Util.numtochinese(fieldvalue);

                }else{
                    returnStr=Util.toHtmlSearch(fieldvalue);
                }

            }                                                           // 单行,多行文本框条件结束
            else if(fieldhtmltype.equals("3")){                         // 浏览按钮 (涉及workflow_broswerurl表)
                if(fieldtype.equals("2") || fieldtype.equals("19")){    // 日期和时间
                    returnStr=fieldvalue;

                } else if(!fieldvalue.equals("")) {
                    String url=BrowserComInfo.getBrowserurl(fieldtype);     // 浏览按钮弹出页面的url
                    String linkurl=BrowserComInfo.getLinkurl(fieldtype);    // 浏览值点击的时候链接的url
                    String showname = "";                                                   // 值显示的名称
                    String showid = "";                                                     // 值

                    ArrayList tempshowidlist=Util.TokenizerString(fieldvalue,",");
                    if(fieldtype.equals("8") || fieldtype.equals("135")){
                        //项目，多项目
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(!linkurl.equals("") ){
                                showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"\' target=\'_new\'>"+ProjectInfoComInfo1.getProjectInfoname((String)tempshowidlist.get(k))+"</a>&nbsp";
                            }else{
                                showname+=ProjectInfoComInfo1.getProjectInfoname((String)tempshowidlist.get(k))+" ";
                            }
                        }
                    }else if(fieldtype.equals("1") ||fieldtype.equals("17")){
                        //人员，多人员
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(!linkurl.equals("") ){
                                if("/hrm/resource/HrmResource.jsp?id=".equals(linkurl))
                                {
                                    showname+="<a href='javaScript:openhrm("+tempshowidlist.get(k)+");' onclick='pointerXY(event);'>"+rc.getResourcename((String)tempshowidlist.get(k))+"</a>&nbsp";
                                }
                                else
                                    showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"\' target=\'_new\'>"+rc.getResourcename((String)tempshowidlist.get(k))+"</a>&nbsp";
                            }else{
                                showname+=rc.getResourcename((String)tempshowidlist.get(k))+" ";
                            }
                        }
                    }else if(fieldtype.equals("7") || fieldtype.equals("18")){
                        //客户，多客户
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(!linkurl.equals("") ){
                                showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"\' target=\'_new\'>"+cci.getCustomerInfoname((String)tempshowidlist.get(k))+"</a>&nbsp";
                            }else{
                                showname+=cci.getCustomerInfoname((String)tempshowidlist.get(k))+" ";
                            }
                        }
                    }else if(fieldtype.equals("4") || fieldtype.equals("57")){
                        //部门，多部门
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(!linkurl.equals("") ){
                                showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"\' target=\'_new\'>"+DepartmentComInfo1.getDepartmentname((String)tempshowidlist.get(k))+"</a>&nbsp";
                            }else{
                                showname+=DepartmentComInfo1.getDepartmentname((String)tempshowidlist.get(k))+" ";
                            }
                        }
                    }else if(fieldtype.equals("9") || fieldtype.equals("37")){
                        //文档，多文档
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(k>0) showname+="<br>";

                            {
                                if(!linkurl.equals("")){
                                    showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"&requestid="+requestid+"\' target=\'_new\'>"+DocComInfo1.getDocname((String)tempshowidlist.get(k))+"</a>";
                                }else{
                                    showname+=DocComInfo1.getDocname((String)tempshowidlist.get(k));
                                }
                            }
                        }
                    }else if(fieldtype.equals("23")){
                        //资产
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(!linkurl.equals("") ){
                                showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"\' target=\'_new\'>"+CapitalComInfo1.getCapitalname((String)tempshowidlist.get(k))+"</a>&nbsp";
                            }else{
                                showname+=CapitalComInfo1.getCapitalname((String)tempshowidlist.get(k))+" ";
                            }
                        }
                    }else if(fieldtype.equals("16") || fieldtype.equals("152")|| fieldtype.equals("171")){
                        //相关请求
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(k>0) showname+="<br>";
                            if(!linkurl.equals("")){
                                int tempnum=0;
                                tempnum++;

                                showname+="<a href=\'"+linkurl+tempshowidlist.get(k)+"&wflinkno="+tempnum+"\' target=\'_new\'>"+WorkflowRequestComInfo1.getRequestName((String)tempshowidlist.get(k))+"</a>";
                            }else{
                                showname+=WorkflowRequestComInfo1.getRequestName((String)tempshowidlist.get(k));
                            }
                        }
                    }else if(fieldtype.equals("141")){
                        //人力资源条件
                        showname+=rcm.getFormShowName(fieldvalue,userlanguage);
                    }
//add by fanggsh for TD4528   20060621 end
                    //added by alan for td:10814
                    else if(fieldtype.equals("142")) {
                        //收发文单位
                        for(int k=0;k<tempshowidlist.size();k++){
                            if(!linkurl.equals("")){
                                showname += "<a href='"+linkurl+tempshowidlist.get(k)+"' target='_new'>"+ duc.getReceiveUnitName(""+tempshowidlist.get(k))+"</a>&nbsp";
                            }else{
                                showname += duc.getReceiveUnitName(""+tempshowidlist.get(k))+" ";
                            }
                        }
                    }
                    //end by alan for td:10814
                    else if(fieldtype.equals("161")){//自定义单选
                        showname = "";                                   // 新建时候默认值显示的名称
                        String showdesc="";
                        showid =fieldvalue;                                     // 新建时候默认值
                        try{
                            Browser browser=(Browser) StaticObj.getServiceByFullname(dbtype, Browser.class);
                            BrowserBean bb=browser.searchById(showid);
                            String desc=Util.null2String(bb.getDescription());
                            String name=Util.null2String(bb.getName());
//						showname="<a title='"+desc+"'>"+name+"</a>&nbsp";
                            String href=Util.null2String(bb.getHref());
                            if(href.equals("")){
                                showname="<a title='"+desc+"'>"+name+"</a>&nbsp";
                            }else{
                                showname="<a title='"+desc+"' href='"+href+"' target='_blank'>"+name+"</a>&nbsp";
                            }
                        }catch(Exception e){
                        }
                    }
                    else if(fieldtype.equals("162")){//自定义多选
                        showname = "";                                   // 新建时候默认值显示的名称
                        showid =fieldvalue;                                     // 新建时候默认值
                        try{
                            Browser browser=(Browser)StaticObj.getServiceByFullname(dbtype, Browser.class);
                            List l=Util.TokenizerString(showid,",");
                            for(int j=0;j<l.size();j++){
                                String curid=(String)l.get(j);
                                BrowserBean bb=browser.searchById(curid);
                                String name=Util.null2String(bb.getName());
                                ////System.out.println("showname:"+showname);
                                String desc=Util.null2String(bb.getDescription());
//						    showname+="<a title='"+desc+"'>"+name+"</a>&nbsp";
                                String href=Util.null2String(bb.getHref());
                                if(href.equals("")){
                                    showname+="<a title='"+desc+"'>"+name+"</a>&nbsp";
                                }else{
                                    showname+="<a title='"+desc+"' href='"+href+"' target='_blank'>"+name+"</a>&nbsp";
                                }
                            }
                        }catch(Exception e){
                        }
                    }else if(fieldtype.equals("224")||fieldtype.equals("225")||fieldtype.equals("226")||fieldtype.equals("227")){//集成浏览按钮
                        //System.out.println(paraTwo+"内容"+fieldvalue);
                        showname=fieldvalue;
                    }
                    else{
                        String tablename=BrowserComInfo.getBrowsertablename(fieldtype); //浏览框对应的表,比如人力资源表
                        String columname=BrowserComInfo.getBrowsercolumname(fieldtype); //浏览框对应的表名称字段
                        String keycolumname=BrowserComInfo.getBrowserkeycolumname(fieldtype);   //浏览框对应的表值字段
                        fieldvalue=deleteFirstAndEndchar(fieldvalue,",");
                        if(fieldvalue.indexOf(",")!=-1){
                            sql= "select "+keycolumname+","+columname+" from "+tablename+" where "+keycolumname+" in( "+fieldvalue+")";
                        }
                        else {
                            sql= "select "+keycolumname+","+columname+" from "+tablename+" where "+keycolumname+"="+fieldvalue;
                        }
                        RecordSet.executeSql(sql);
                        while(RecordSet.next()){
                            if(!linkurl.equals("") ){
                                showname += "<a href=\'"+linkurl+RecordSet.getString(1)+"\' target=\'_new\'>"+Util.toScreen(RecordSet.getString(2),userlanguage)+"</a>&nbsp";
                            }else{
                                showname +=Util.toScreen(RecordSet.getString(2),userlanguage)+" ";
                            }
                        }    // end of while
                    }

                    returnStr=showname;

                }
            }                                                       // 浏览按钮条件结束
            else if(fieldhtmltype.equals("4")) {                    // check框
                returnStr=fieldvalue;
            }                                                       // check框条件结束
            else if(fieldhtmltype.equals("5")){                     // 选择框   select
                // 查询选择框的所有可以选择的值
                rs.executeProc("workflow_SelectItemSelectByid",""+fieldid+flag+isbill);
                while(rs.next()){
                    String tmpselectvalue = Util.null2String(rs.getString("selectvalue"));
                    String tmpselectname = Util.toScreen(rs.getString("selectname"),userlanguage);
                    if(fieldvalue.equals(tmpselectvalue)){ returnStr=tmpselectname;}
                }
            }else if(fieldhtmltype.equals("6")){
                if(!fieldvalue.equals("")) {
                    sql="select id,docsubject,accessorycount from docdetail where id in("+fieldvalue+") order by id asc";
                    int linknum=-1;
                    RecordSet.executeSql(sql);
                    while(RecordSet.next()){
                        linknum++;
                        if(linknum>0) returnStr+="<br>";
                        String showid = Util.null2String(RecordSet.getString(1)) ;
                        String tempshowname= Util.toScreen(RecordSet.getString(2),userlanguage) ;
                        int accessoryCount=RecordSet.getInt(3);

                        DocImageManager.resetParameter();
                        DocImageManager.setDocid(Integer.parseInt(showid));
                        DocImageManager.selectDocImageInfo();

                        String docImagefileid = "";
                        long docImagefileSize = 0;
                        String docImagefilename = "";
                        String fileExtendName = "";
                        int versionId = 0;

                        if(DocImageManager.next()){
                            docImagefileid = DocImageManager.getImagefileid();
                            docImagefileSize = DocImageManager.getImageFileSize(Util.getIntValue(docImagefileid));
                            docImagefilename = DocImageManager.getImagefilename();
                            fileExtendName = docImagefilename.substring(docImagefilename.lastIndexOf(".")+1).toLowerCase();
                            versionId = DocImageManager.getVersionId();
                        }

                        if(accessoryCount==1 && (fileExtendName.equalsIgnoreCase("xls")||fileExtendName.equalsIgnoreCase("doc")||fileExtendName.equalsIgnoreCase("pdf"))){
                            //returnStr= "<a href=\"javascript:openFullWindowHaveBar(\'/docs/docs/DocDspExt.jsp?id="+showid+"&imagefileId="+docImagefileid+"&from=accessory\')\">"+docImagefilename+"</a> ";
                            returnStr+= "<a href=\"javascript:openFullWindowHaveBar(\'/docs/docs/DocDspExt.jsp?id="+showid+"&imagefileId="+docImagefileid+"&isFromAccessory=true\')\">"+docImagefilename+"</a> ";
                        }else{
                            returnStr+="<a href=\"javascript:openFullWindowHaveBar(\'/docs/docs/DocDsp.jsp?id="+showid+" \')\">"+tempshowname+"</a> ";
                        }}
                }
            }
        }
        catch (Exception e)
        {
            returnStr="";
        }
        return returnStr;

    }

    /**
     * 流程标题显示样式（是否加图片等）
     * @param requestname 流程标题
     * @param para2 流程ID+，+显示类型+，+用户语言
     * @return   流程标题显示样式
     */
    public String getWfNewLinkByUrger(String requestname, String para2) {
        String returnStr="";

        String[] tempStr = Util.splitString(para2, "+");
        int requestid = Util.getIntValue(Util.null2String(tempStr[0]));
        int workflowid=Util.getIntValue(Util.null2String(tempStr[1]));
        int userid = Util.getIntValue(Util.null2String(tempStr[2]),0);
        int usertype=Util.getIntValue(Util.null2String(tempStr[3]),0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        int isbill=0;
        int formid=0;
        boolean isnew = false;
//        String viewerSql = "select a.viewdate,a.viewtime from workflow_requestviewlog a "+
//        				   " where a.id="+requestid+
//        				   " and a.viewer="+userid+
//        				   " order by a.viewdate desc,a.viewtime desc";
//        
//    	String logSql = "select b.operator, b.operatedate,b.operatetime from workflow_requestlog b "+
//    					" where b.requestid="+requestid+
//    					" order by b.operatedate desc ,b.operatetime desc";
        //td
        String newsql = "select b.lastoperatedate,b.lastoperatetime,b.creater,b.lastoperator, b.lastoperatortype from workflow_requestbase b where b.requestid = "
                + requestid;
        String requestdate = "";
        String viewdate = "";
        RecordSet rs = new RecordSet();
        rs.execute(newsql);
        if (rs.next())
        {
            if(userid!=rs.getInt(4)||usertype!=rs.getInt(5)){
                if("".equals(Util.null2String(rs.getString(1))) || "".equals(Util.null2String(rs.getString(2)))) {
                    if(rs.getInt(3) != userid) {
                        newsql = "select w.viewdate from workflow_requestviewlog w where w.viewer="
                                +userid + " and id="+requestid;
                        rs.execute(newsql);
                        if(!rs.next()) {
                            isnew = true;
                        }
                    }
                }else {
                    requestdate = rs.getString(1) + rs.getString(2);
                    newsql = "select max(w.viewdate) as viewdate,max(w.viewtime) as viewtime from workflow_requestviewlog w where w.viewer="
                            +userid + " and id="+requestid +" group by id";
                    rs.execute(newsql);
                    if(rs.next()) {
                        viewdate = rs.getString(1) + rs.getString(2);
                        if(viewdate.compareTo(requestdate)<0) {
                            isnew = true;
                        }
                    }else {
                        isnew = true;
                    }
                }
            }
        }
        ////根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
        if (rs.next())
        {
            formid=rs.getInt(1);
            isbill=rs.getInt(2);
        }
        MailAndMessage mailTitle=new MailAndMessage();
        String titles=mailTitle.getTitle(requestid,workflowid,formid,userlang,isbill);
        if (!titles.equals(""))
            requestname=requestname+"<B>（"+titles+"）</B>";

        if (isnew) {
            //新流程,粗体链接加图片
            returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid
                    + "&urger=1\',"+requestid+")>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><img src='/images/ecology8/statusicon/BDNew_wev8.png' title='" + SystemEnv.getHtmlLabelName(19154, userlang) + "'/></span>";
        }
        else
        {
            returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid
                    + "&urger=1\',"+requestid+")>"+requestname+"</a><span id=\'wflist_"+requestid+"span\'></span>";
        }
        return returnStr;
    }

    public String  getWfNewLinkByUrgerExt(String requestname, String para2) {
        String returnStr="";

        String[] tempStr = Util.splitString(para2, "+");
        int requestid = Util.getIntValue(Util.null2String(tempStr[0]));
        int workflowid=Util.getIntValue(Util.null2String(tempStr[1]));
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        int isbill=0;
        int formid=0;
        RecordSet rs = new RecordSet();
        ////根据后台设置在MAIL标题后加上流程中重要的字段
        rs.execute("select formid,isbill from workflow_base where id="+workflowid);
        if (rs.next())
        {
            formid=rs.getInt(1);
            isbill=rs.getInt(2);
        }
        MailAndMessage mailTitle=new MailAndMessage();
        String titles=mailTitle.getTitle(requestid,workflowid,formid,userlang,isbill);
        if (!titles.equals(""))
            requestname=requestname+"<B>（"+titles+"）</B>";

        returnStr = "<a href=javaScript:openWfToTab(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid
                + "&urger=1\','"+Util.toHtmlForCpt(requestname)+"')>"+requestname+"</a>";

        return returnStr;
    }

    public String getWfNewLinkImageExt(String requestid, String userid){

        String returnStr="";

        String viewerSql = "select a.viewdate,a.viewtime from workflow_requestviewlog a "+
                " where a.id="+requestid+
                " and a.viewer="+userid+
                " order by a.viewdate desc,a.viewtime desc";
        String logSql = "select b.operator, b.operatedate,b.operatetime from workflow_requestlog b "+
                " where b.requestid="+requestid+
                " order by b.operatedate desc ,b.operatetime desc";
        String viewdate = "";
        String logdate = "";
        int operator = -1;
        RecordSet rs = new RecordSet();
        rs.execute(viewerSql);
        if (rs.next())
        {
            viewdate = rs.getString(1)+rs.getString(2);
        }
        rs.execute(logSql);
        if (rs.next())
        {
            operator = rs.getInt(1);
            logdate = rs.getString(2)+rs.getString(3);
        }
        ////根据后台设置在MAIL标题后加上流程中重要的字段


        if(!(operator+"").equals(userid))
        {
            int compareResult = viewdate.compareTo(logdate);
            if (compareResult<0) {
                //新流程,粗体链接加图片
                returnStr = "<IMG src=\'/images/BDNew_wev8.gif\' align=absbottom>";
            }else{
                returnStr = "";
            }
        }
        else
        {
            returnStr = "";
        }
        return returnStr;
    }
    /**
     * 获得是否能查看流程内容
     * @param isview
     * @param hrmiduserLanguage
     * @return 是否能查看流程内容
     */
    public String getViewWorkflow(String isview, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(isview, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisview(this,")
                    .append(hrmid)
                    .append(",").append(workflowid).append(",0)'").append(" name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisview(this,")
                    .append(hrmid)
                    .append(",").append(workflowid).append(",1)'").append(" name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
        //if(Util.getIntValue(isview, 0)>0){
        //returnstr= "<INPUT type=radio name=\"vradio_"+workflowid+"\" CHECKED onclick=\"setisview('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"vradio_"+workflowid+"\" onclick=\"setisview('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
        //}else{
        //returnstr= "<INPUT type=radio name=\"vradio_"+workflowid+"\" onclick=\"setisview('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"vradio_"+workflowid+"\" CHECKED onclick=\"setisview('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
        //}
        return sb.toString();
    }

    /**
     * 获得是否能编辑流程表单
     * @param isview
     * @param hrmiduserLanguage
     * @return 是否能编辑流程表单
     */
    public String getEditWorkflow(String isview, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(isview, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisedit(this,")
                    .append(hrmid)
                    .append(",").append(workflowid).append(",0)'").append(" name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisedit(this,")
                    .append(hrmid)
                    .append(",").append(workflowid).append(",1)'").append(" name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
        //if(Util.getIntValue(isview, 0)>0){
        //returnstr= "<INPUT type=radio name=\"vradio_"+workflowid+"\" CHECKED onclick=\"setisview('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"vradio_"+workflowid+"\" onclick=\"setisview('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
        //}else{
        //returnstr= "<INPUT type=radio name=\"vradio_"+workflowid+"\" onclick=\"setisview('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"vradio_"+workflowid+"\" CHECKED onclick=\"setisview('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
        //}
        return sb.toString();
    }

    /**
     * 获得是否允许流程干预
     * @param isintervenor
     * @param hrmiduserLanguage
     * @return 是否允许流程干预
     */
    public String getIntervenorWorkflow(String isintervenor, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(isintervenor, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisIntervenor(this,"+hrmid+","+workflowid+",0)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisIntervenor(this,"+hrmid+","+workflowid+",1)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
//        if(Util.getIntValue(isintervenor, 0)>0){
//            returnstr= "<INPUT type=radio name=\"iradio_"+workflowid+"\" CHECKED onclick=\"setisIntervenor('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"iradio_"+workflowid+"\" onclick=\"setisIntervenor('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }else{
//            returnstr= "<INPUT type=radio name=\"iradio_"+workflowid+"\" onclick=\"setisIntervenor('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"iradio_"+workflowid+"\" CHECKED onclick=\"setisIntervenor('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }
        return sb.toString();
    }

    /**
     * 获得是否允许删除
     * @param isdelete
     * @param hrmiduserLanguage
     * @return 是否允许流程干预
     */
    public String getDelWorkflow(String isdelete, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(isdelete, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisDel(this,"+hrmid+","+workflowid+",0)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisDel(this,"+hrmid+","+workflowid+",1)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
//        if(Util.getIntValue(isdelete, 0)>0){
//            returnstr= "<INPUT type=radio name=\"dradio_"+workflowid+"\" CHECKED onclick=\"setisDel('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"dradio_"+workflowid+"\" onclick=\"setisDel('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }else{
//            returnstr= "<INPUT type=radio name=\"dradio_"+workflowid+"\" onclick=\"setisDel('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"dradio_"+workflowid+"\" CHECKED onclick=\"setisDel('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }
        return sb.toString();
    }

    /**
     * 获得是否允许强制归档
     * @param isforceover
     * @param hrmiduserLanguage
     * @return 是否允许流程干预
     */
    public String getFOWorkflow(String isforceover, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(isforceover, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisFO(this,"+hrmid+","+workflowid+",0)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisFO(this,"+hrmid+","+workflowid+",1)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
//        if(Util.getIntValue(isforceover, 0)>0){
//            returnstr= "<INPUT type=radio name=\"bradio_"+workflowid+"\" CHECKED onclick=\"setisFO('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"bradio_"+workflowid+"\" onclick=\"setisFO('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }else{
//            returnstr= "<INPUT type=radio name=\"bradio_"+workflowid+"\" onclick=\"setisFO('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"bradio_"+workflowid+"\" CHECKED onclick=\"setisFO('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }
        return sb.toString();
    }
    /**
     * 获得是否暂停撤销启用=
     * @param hrmiduserLanguage
     * @return 是否暂停撤销启用
     */
    public String getSOWorkflow(String issooperator, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(issooperator, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisSO(this,"+hrmid+","+workflowid+",0)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisSO(this,"+hrmid+","+workflowid+",1)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
//        if(Util.getIntValue(issooperator, 0)>0){
//            returnstr= "<INPUT type=radio name=\"soadio_"+workflowid+"\" CHECKED onclick=\"setisSO('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"soadio_"+workflowid+"\" onclick=\"setisSO('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }else{
//            returnstr= "<INPUT type=radio name=\"soadio_"+workflowid+"\" onclick=\"setisSO('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"soadio_"+workflowid+"\" CHECKED onclick=\"setisSO('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }
        return sb.toString();
    }
    /**
     * 获得是否允许强制收回
     * @param isForceDrawBack
     * @param hrmiduserLanguage
     * @return 是否允许流程干预
     */
    public String getFBWorkflow(String isForceDrawBack, String hrmiduserLanguage) {
        String returnstr="";
        String[] tempStr = Util.splitString(hrmiduserLanguage, "+");
        String hrmid = Util.null2String(tempStr[0]);
        String workflowid = Util.null2String(tempStr[1]);
        int intUserLanguage = Util.getIntValue(tempStr[2],7);
        String userid = Util.null2String(tempStr[3]);
        int operatelevel = 0;
        if(tempStr.length>4)
        {
            operatelevel = Util.getIntValue(Util.null2String(tempStr[4]),-1);
        }
        int detachable=0;
        if(tempStr.length>5)
        {
            detachable = Util.getIntValue(Util.null2String(tempStr[5]),-1);
        }
        String editString = "";
        if(!userid.equals("1")){
            /*rs.executeSql("select detachable from SystemSet");
            int detachable=0;
            if(rs.next()){
                detachable=rs.getInt("detachable");
            }*/
            if(detachable==1){//分权情况下要判断是否有权限删除
                //rs.executeSql("select * from workflow_monitor_bound where monitorhrmid="+hrmid+" and workflowid="+workflowid+" and operator="+userid);
                if(operatelevel<=0){//无权限
                    editString = " disabled ";
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if(Util.getIntValue(isForceDrawBack, 0)>0){
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisFB(this,"+hrmid+","+workflowid+",0)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' checked />");
        } else {
            sb.append("<input type='checkbox' tzCheckbox='true'  class='InputStyle' onclick='setisFB(this,"+hrmid+","+workflowid+",1)' name='vradio_"+workflowid+"' "+editString)
                    .append("value='");
            sb.append("' />");
        }
//        if(Util.getIntValue(isForceDrawBack, 0)>0){
//            returnstr= "<INPUT type=radio name=\"oradio_"+workflowid+"\" CHECKED onclick=\"setisFB('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"oradio_"+workflowid+"\" onclick=\"setisFB('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }else{
//            returnstr= "<INPUT type=radio name=\"oradio_"+workflowid+"\" onclick=\"setisFB('"+hrmid+"','"+workflowid+"','1')\""+editString+">"+SystemEnv.getHtmlLabelName(115,intUserLanguage)+"<INPUT type=radio name=\"oradio_"+workflowid+"\" CHECKED onclick=\"setisFB('"+hrmid+"','"+workflowid+"','0')\""+editString+">"+SystemEnv.getHtmlLabelName(17875,intUserLanguage)+"";
//        }
        return sb.toString();
    }

    /**
     * 是否在列表显示选择筐
     * @param workflowid 流程ID
     * @return true/false
     */
    @Deprecated
    public String getWFMonitorCheckBox(String workflowid) {
        String[] tempStr = Util.splitString(workflowid, "+");
        int wfid = Util.getIntValue(tempStr[0]);
        int hrmid = Util.getIntValue(tempStr[1]);
        int logintype= Util.getIntValue(tempStr[2],1);
        String flag="false";
        if(logintype==1){
            RecordSet rs = new RecordSet();
            rs.executeSql("select workflowid from workflow_monitor_bound where (isdelete='1' or isforceover='1') and workflowid="+wfid+" and monitorhrmid="+hrmid);
            if(rs.next()){
                flag="true";
            }
        }
        return flag;
    }

    /**
     * 获得流程耗时统计报表里的未操作者
     * @param requestid
     * @return
     */
    public String getUnOperatorsForStat(String requestid) {
        String returnStr="";
        RecordSet rs = new RecordSet();
        rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1') or (isremark='4' and viewtype=0))  and requestid = " + requestid);
        while(rs.next()){
            if(returnStr.equals("")){
                if(rs.getInt("usertype")==0){
                    if(rs.getInt("agenttype")==2)
                        returnStr +=  rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                    else
                        returnStr +=  rc.getResourcename(rs.getString("userid"));
                }else{
                    returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                }
            }
            else{
                if(rs.getInt("usertype")==0){
                    if(rs.getInt("agenttype")==2)
                        returnStr +=  ","+rc.getResourcename(rs.getString("agentorbyagentid"))+"->"+rc.getResourcename(rs.getString("userid"));
                    else
                        returnStr +=  ","+rc.getResourcename(rs.getString("userid"));
                }else{
                    returnStr +=  cci.getCustomerInfoname(rs.getString("userid"));
                }
            }
        }
        return returnStr;
    }

    /**
     * 去除前后指定字符
     * @param str
     * @param splitstr
     * @return
     */
    public String deleteFirstAndEndchar(String str,String splitstr){
        boolean needround=false;
        if(str.substring(0,splitstr.length()).equals(splitstr)){
            str=str.substring(splitstr.length());
            needround=true;
        }
        if(str.substring(str.length()-splitstr.length()).equals(splitstr)){
            str=str.substring(0,str.length()-splitstr.length());
            needround=true;
        }
        if(needround){
            return deleteFirstAndEndchar(str,splitstr);
        }else{
            return str;
        }
    }
    public String getIsmultiprintStr(String ismultiprint, String language){
        String retStr = "";
        try{
            int languageid = Util.getIntValue(language, 7);
            if("1".equals(ismultiprint)){
                retStr = SystemEnv.getHtmlLabelName(27046, languageid);
            }else{
                retStr = SystemEnv.getHtmlLabelName(27045, languageid);
            }
        }catch(Exception e){
            retStr = SystemEnv.getHtmlLabelName(27045, 7);
        }
        return retStr;
    }
    /**
     * 流程标题显示样式（是否加图片等），区分颜色
     * @param requestname 流程标题
     * @param para2 请求ID + 流程ID + 显示类型 + 是否超时 + 用户语言 + 节点ID currentnodeid + 操作人类性isremark+用户id userID+ 代理人/被代理人ID + 代理/被代理类型
     * @return   流程标题显示样式
     */
    public String getWfNewLinkWithTitleNoAdditional(String requestname, String para2) {
        String returnStr="";

        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String workflowid=Util.null2String(tempStr[1]);
        String viewtype = Util.null2String(tempStr[2]);
        int isovertime=Util.getIntValue(tempStr[3],0);
        int userlang = Util.getIntValue(Util.null2String(tempStr[4]),7);
        String nodeid = Util.null2String(tempStr[5]);
        String isremark = Util.null2String(tempStr[6]);
        String userID = Util.null2String(tempStr[7]);
        String isprocessedvalue = "";
        if(tempStr.length>=11)
        {
            isprocessedvalue = Util.null2String(tempStr[10]);
        }

        String agentorbyagentid="";
        String agenttype = "";

        if(tempStr.length>=10){
            agenttype= Util.null2String(tempStr[9]);
        }
        boolean isprocessed=false;
        if((isremark.equals("0")&&(isprocessedvalue.equals("2")||isprocessedvalue.equals("3")))||isremark.equals("5"))
        {
            isprocessed=true;
        }
        if (viewtype.equals("0")) {
            //新流程,粗体链接加图片
            if(isprocessed){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDOut_wev8.gif\' align=absbottom></span>";
            }else if("1".equals(agenttype)){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'></span>";
            }else{
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDNew_wev8.gif\' align=absbottom></span>";
            }
        } else if(viewtype.equals("-1")) {
            //旧流程,有新的提交信息未查看,普通链接加图片
            if(isprocessed){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDOut_wev8.gif\' align=absbottom></span>";
            }else{
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src=\'/images/BDNew2_wev8.gif\' align=absbottom></span>";
            }
        }else{
            //旧流程,普通链接
            if(isprocessed){
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'><IMG src='/images/BDOut_wev8.gif' align=absbottom></span>";
            }else{
                returnStr = "<a href=javaScript:openFullWindowHaveBarForWFList(\'/workflow/request/ViewRequest.jsp?requestid=" + requestid+"&isovertime="+isovertime
                        + "\',"+requestid+") >"+requestname+"</a><span id=\'wflist_"+requestid+"span\'></span>";
            }
        }
        return returnStr;
    }

    /**
     *流程接口部署列表的checkbox框
     */
    public String getInterfaceChecBox(String deployStatus){
        deployStatus = Util.null2String(deployStatus);
        if(deployStatus.equals("1")){
            return "false";
        }
        return "true";
    }

    /**
     *流程接口部署详细的checkbox框
     */
    public String getInterfaceDetailChecBox(String id){
        return "true";
    }

    /**
     *流程接口部署状态描述
     */
    public String getDeployDesc(String value){
        value = Util.null2String(value);
        if(value.equals("1")){
            return "已部署";
        }
        return "<span class='noDeploy'>未部署</span>";
    }

    /**
     *流程接口封存状态描述
     */
    public String getClosedDesc(String value){
        value = Util.null2String(value);
        if(value.equals("1")){
            return "封存";
        }
        return "正常";
    }

    /**
     *类型描述
     */
    public String getTypeDesc(String isnode,String ispreadd){
        isnode = Util.null2String(isnode);
        ispreadd = Util.null2String(ispreadd);
        if(isnode.equals("0")){
            return "出口";
        }
        if(ispreadd.equals("0")){
            return "节点后";
        }
        return "节点前";
    }

    /**
     *流程接口部署显示操作
     */
    public List<String> getInterfaceOperate(String id,String deployStatus,String closed){
        List<String> returnList = new ArrayList<String>();
        if(deployStatus.equals("1")){
            returnList.add("false");
            returnList.add("true");
        }else{
            returnList.add("true");
            returnList.add("false");
        }
        if(closed.equals("1")){
            returnList.add("false");
            returnList.add("true");
        }else{
            returnList.add("true");
            returnList.add("false");
        }
        return returnList;
    }

    /**
     *流程接口部署详细显示操作
     */
    public List<String> getInterfaceDetailOperate(String id){
        List<String> returnList = new ArrayList<String>();
        returnList.add("true");
        return returnList;
    }

    public List getshowTransOperate(String id){
        List delList = new ArrayList();
        delList.add("true");
        return delList;
    }

    /**
     * 仅用于浏览按钮功能的演示，请不要用于真实的场景
     * 该功能最终的目的是返回一个JSON格式的字符串，
     * 具体的格式为：[{"browserValue":"11111","browserSpanValue":"张三"},{"browserValue":"2222","browserSpanValue":"李四"}]
     * browserValue对应的是页面上的浏览按钮的隐藏域中的值，比如人的ID、客户的ID等
     * browserSpanValue对应的是页面上的浏览按钮显示的中文名称，比如人的名称、客户的名称等
     * 从对象到字符串的转换可以用JsonUtils.list2json,非常方便；当然你也可以自己写其他的方法去拼，只要格式正确就可以
     */
    public String getBrowserPerson(String requestid, String para) {
        String[] tempStr = Util.splitString(para, "+");
        String userid = Util.null2String(tempStr[1]);
        String showoperators = "";
        try {
            showoperators = RequestDefaultComInfo.getShowoperator("" + userid);
        } catch (Exception eshows) {
        }
        ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        if (showoperators.equals("1")) {
            RecordSet rs = new RecordSet();
            rs.executeSql("select distinct userid,usertype,agenttype,agentorbyagentid from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = "+ requestid);

            while (rs.next()) {
                if (rs.getInt("usertype") == 0) {
                    String personId = rs.getString("userid");
                    String persoonName = rc.getResourcename(personId);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("browserValue", personId);
                    map.put("browserSpanValue", persoonName);
                    maps.add(map);
                }
            }
        }
        return JsonUtils.list2json(maps);
    }

    public String getWFMultiSubmit(String wfid,int nodeid,String isremark,int takisremark){
        String returnstr = "0";
        RecordSet rs = new RecordSet();

        if(!"".equals(wfid.trim()) && StringUtils.isNumeric(wfid)){
            rs.execute("select multiSubmit,submittype from WORKFLOW_BASE WHERE ID = "+ wfid);
            while (rs.next()) {
                int multiSubmit = Util.getIntValue(rs.getString("multiSubmit"),0);
                int submittype = Util.getIntValue(rs.getString("submittype"),0);

                if(multiSubmit == 1){
                    if(("1".equals(isremark) && takisremark != 2) || "9".equals(isremark)){//转发，抄送只判断有没有开启批量提交开关，不判断节点
                        returnstr = "1";
                    }else{
                        if(submittype == 0){//全部
                            returnstr = "1";
                        }else{
                            //自由节点，集成发起节点的配置
                            rs.execute("select startnodeid from workflow_nodebase where isfreenode = 1 and id = " + nodeid);
                            if(rs.next()){
                                int startnodeid = Util.getIntValue(rs.getString(1),-1);
                                if(startnodeid != -1)
                                    nodeid = startnodeid;
                            }
                            rs.execute("select 1 from workflow_flownode where workflowid = " + wfid + " and nodeid = " + nodeid + " and batchsubmit = 1");
                            if(submittype == 1){//选择节点
                                if(rs.next()){
                                    returnstr = "1";
                                }
                            }else if(submittype == 2){//排除节点
                                if(!rs.next()){
                                    returnstr = "1";
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnstr;
    }
    /**
     * 获取接收时间
     * @param requestid
     * @param para
     * @return
     */
    public String getWFUrgerRecievedate(String requestid,String  para){

        String returnStr="";
        RecordSet rs = new RecordSet();
        if(rs.getDBType().equals("oracle")){
            rs.executeSql("select max(receivedate||' '||receivetime) as receivedatetime from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);
        }else{
            rs.executeSql("select max(receivedate +' '+ receivetime) as receivedatetime from workflow_currentoperator where (isremark in ('0','1','5','7','8','9') or (isremark='4' and viewtype=0))  and requestid = " + requestid);
        }
        if(rs.next()){
            returnStr = Util.null2String(rs.getString("receivedatetime"));
        }
        return returnStr;
    }

    public String getWorkflowname(String workflowid){
        if(workflowid.indexOf("-")!=-1){
            RecordSet rs = new RecordSet();
            rs.executeSql("select workflowname from ofs_workflow where workflowid="+workflowid);
            rs.next();
            return Util.null2String(rs.getString(1));
        }else{
            return wf.getWorkflowname(workflowid);
        }
    }

    public String getCurrentNode(String currentnodeid,String para2) {
        String returnStr="";
        String[] tempStr = Util.splitString(para2, "+");
        String requestid = Util.null2String(tempStr[0]);
        String nodename = "";
        try{
            if(tempStr.length>1){
                nodename = Util.null2String(tempStr[1]);
            }
        }catch(Exception e){

        }
        RecordSet rs = new RecordSet();
        if(currentnodeid.equals("-1")) {
            //if(nodename.equals("")){
            rs.executeSql("select nodename from ofs_todo_data where requestid="+requestid+" order by id desc");
            if (rs.next()) {
                returnStr = rs.getString("nodename");
            }
            //}else{
            //	returnStr = nodename ;		
            //}
        }else{
            rs.executeSql("select nodename from workflow_nodebase where id= " + currentnodeid);
            if (rs.next()) {
                returnStr = rs.getString("nodename");
            }
        }

        return returnStr;
    }

}