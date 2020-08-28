<%@ page import="org.json.JSONObject"%>
<%@ page import="org.json.JSONException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.general.AttachFileUtil" %>
<%@ page import="weaver.common.StringUtil" %>
<%@ page import="weaver.conn.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="weaver.rtx.RTXConfig" %>
<%@ page import="weaver.file.Prop,weaver.general.GCONST" %>
<%@ page import="weaver.workflow.request.SubWorkflowTriggerService" %>
<%@page import="weaver.workflow.request.RequestShare"%>
<%@ page import="weaver.file.FileUpload" %>
<%@ page import="weaver.rdeploy.portal.PortalUtil" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>

<!-- 
<script type="text/javascript" src="/wui/common/js/ckeditor/ckeditor_wev8.js"></script>
<script type="text/javascript" src="/wui/common/js/ckeditor/ckeditorext_wev8.js"></script>
-->

<!--引入ueditor相关文件-->
<script type="text/javascript" charset="UTF-8" src="/ueditor/ueditor.config_wev8.js"></script>
<script type="text/javascript" charset="UTF-8" src="/ueditor/ueditor.all.min_wev8.js"> </script>
<script type="text/javascript" charset="UTF-8" src="/ueditor/lang/zh-cn/zh-cn_wev8.js"></script>
<!--添加插件-->
<script type="text/javascript" charset="UTF-8" src="/ueditor/custbtn/appwfat_wev8.js"></script>
<link type="text/css" href="/ueditor/ueditorext_wf_wev8.css" rel="stylesheet"></link>
<LINK href="/hrm/area/browser/areabrowser.css" type=text/css rel=STYLESHEET>
<!-- ckeditor的一些方法在uk中的实现 -->
<script type="text/javascript" charset="UTF-8" src="/js/workflow/ck2uk_wev8.js"></script>
<SCRIPT language="javascript" src="/hrm/area/browser/areabrowser_wev8.js"></script>
<script language=javascript>
window.__userid = '<%=request.getParameter("f_weaver_belongto_userid")%>';
window.__usertype = '<%=request.getParameter("f_weaver_belongto_usertype")%>';
</script>

<!-- word转html插件 -->
<script type="text/javascript" charset="UTF-8" src="/ueditor/custbtn/wordtohtml_wev8.js"></script>

<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="RecordSet2" class="weaver.conn.RecordSet" scope="page" /> <%-- xwj for td2104 on 20050802--%>
<jsp:useBean id="RecordSet3" class="weaver.conn.RecordSet" scope="page" /> <%-- xwj for td2104 on 20050802--%>
<jsp:useBean id="RecordSet5" class="weaver.conn.RecordSet" scope="page" /> <%-- xwj for td3665 on 20060227--%>
<jsp:useBean id="RecordSet6" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="RecordSet7" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="RecordSet8" class="weaver.conn.RecordSet" scope="page" /> 
<jsp:useBean id="RecordSet9" class="weaver.conn.RecordSet" scope="page" /> 
<jsp:useBean id="RequestTriDiffWfManager" class="weaver.workflow.request.RequestTriDiffWfManager" scope="page" /> 
<jsp:useBean id="RequestCheckUser" class="weaver.workflow.request.RequestCheckUser" scope="page"/>
<jsp:useBean id="FieldComInfo" class="weaver.workflow.field.FieldComInfo" scope="page" />
<jsp:useBean id="WorkflowComInfo" class="weaver.workflow.workflow.WorkflowComInfo" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page"/>
<jsp:useBean id="BrowserComInfo" class="weaver.workflow.field.BrowserComInfo" scope="page"/>
<jsp:useBean id="CustomerInfoComInfo" class="weaver.crm.Maint.CustomerInfoComInfo" scope="page" />
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rs1" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="DocImageManager" class="weaver.docs.docs.DocImageManager" scope="page" />
<jsp:useBean id="WfFunctionManageUtil" class="weaver.workflow.workflow.WfFunctionManageUtil" scope="page"/><!--xwj for td3665 20060224-->
<jsp:useBean id="WfForceOver" class="weaver.workflow.workflow.WfForceOver" scope="page" /><!--xwj for td3665 20060224-->
<jsp:useBean id="WfForceDrawBack" class="weaver.workflow.workflow.WfForceDrawBack" scope="page" /><!--xwj for td3665 20060224-->
<jsp:useBean id="flowDoc" class="weaver.workflow.request.RequestDoc" scope="page"/>
<jsp:useBean id="WFUrgerManager" class="weaver.workflow.request.WFUrgerManager" scope="page" />
<jsp:useBean id="sysPubRefComInfo" class="weaver.general.SysPubRefComInfo" scope="page" />
<jsp:useBean id="RequestDetailImport" class="weaver.workflow.request.RequestDetailImport" scope="page" />
<jsp:useBean id="WFLinkInfo_nf" class="weaver.workflow.request.WFLinkInfo" scope="page"/>
<jsp:useBean id="WFManager" class="weaver.workflow.workflow.WFManager" scope="session" />
<jsp:useBean id="CptWfUtil" class="weaver.cpt.util.CptWfUtil" scope="page" />
<!-- 微信提醒(QC:98106) -->
<jsp:useBean id="WechatPropConfig" class="weaver.wechat.util.WechatPropConfig" scope="page"/>
<%
int init_road=-1;//当前节点的路径编辑权限
String init_nodename=SystemEnv.getHtmlLabelName(15070, user.getLanguage());
int init_frms=-1;
int currtype=-1;//当前节点的类型
String init_nodeDo="";
boolean isfromtab =  Util.null2String(request.getParameter("isfromtab")).equals("true")?true:false;
%>
<%
String isworkflowdoc = Util.getIntValue(request.getParameter("isworkflowdoc"),0)+"";//是否为公文
int seeflowdoc = Util.getIntValue(request.getParameter("seeflowdoc"),0);
int userlanguage=Util.getIntValue(request.getParameter("languageid"),7);
String clientip = request.getRemoteAddr();
Calendar today = Calendar.getInstance();
String currentdate = Util.add0(today.get(Calendar.YEAR), 4) + "-" +
                     Util.add0(today.get(Calendar.MONTH) + 1, 2) + "-" +
                     Util.add0(today.get(Calendar.DAY_OF_MONTH), 2) ;

String currenttime = Util.add0(today.get(Calendar.HOUR_OF_DAY), 2) + ":" +
                     Util.add0(today.get(Calendar.MINUTE), 2) + ":" +
                     Util.add0(today.get(Calendar.SECOND), 2) ;
                     
String newfromdate="a";
String newenddate="b";
String  isrequest = Util.null2String(request.getParameter("isrequest")); // 是否是从相关工作流的链接中来,1:是
int salesMessage = Util.getIntValue(request.getParameter("salesMessage"),-1);
int requestid = Util.getIntValue(request.getParameter("requestid"),0);
String wfdoc = Util.null2String((String)session.getAttribute(requestid+"_wfdoc"));
String message = Util.null2String(session.getAttribute("errormsgid_"+user.getUID()+"_"+requestid));  // 返回的错误信息
if(StringUtil.isNull(message)){
    message = Util.null2String(request.getParameter("message"));
}
session.removeAttribute("errormsgid_"+user.getUID()+"_"+requestid);
int isovertime= Util.getIntValue(request.getParameter("isovertime"),0) ;
String requestname="";      //请求名称
String requestlevel="";     //请求重要级别 0:正常 1:重要 2:紧急
String requestmark = "" ;   //请求编号
String isbill="0";          //是否单据 0:否 1:是
int creater=0;              //请求的创建人
int creatertype = 0;        //创建人类型 0: 内部用户 1: 外部用户
int deleted=0;              //请求是否删除  1:是 0或者其它 否
int billid=0 ;              //如果是单据,对应的单据表的id
String  fromFlowDoc=Util.null2String(request.getParameter("fromFlowDoc"));  //是否从流程创建文档而来
int workflowid=0;           //工作流id
String workflowtype = "" ;  //工作流种类
int formid=0;               //表单或者单据的id
int helpdocid = 0;          //帮助文档 id
int nodeid=0;               //节点id
String nodetype="";         //节点类型  0:创建 1:审批 2:实现 3:归档
String workflowname = "" ;          //工作流名称
String isreopen="";                 //是否可以重打开
String isreject="";                 //是否可以退回
int isremark = -1 ;              //当前操作状态  modify by xhheng @ 20041217 for TD 1291
int handleforwardid = -1;     //转办状态
int takisremark = -1;     //意见征询状态
String status = "" ;     //当前的操作类型
String needcheck="requestname,";

String topage = Util.null2String(request.getParameter("topage")) ;        //返回的页面
topage = URLEncoder.encode(topage);
String isaffirmance=Util.null2String(request.getParameter("isaffirmance"));//是否需要提交确认
String reEdit=Util.null2String(request.getParameter("reEdit"));//是否为编辑
// 工作流新建文档的处理
String docfileid = Util.null2String(request.getParameter("docfileid"));   // 新建文档的工作流字段
String isrejectremind="";
String ischangrejectnode="";
String isselectrejectnode="";
// 董平 2004-12-2 FOR TD1421  新建流程时如果点击表单上的文档新建按钮，在新建完文档之后，能返回流程，但是文档却没有挂带进来。
String newdocid = Util.null2String(request.getParameter("newdocid"));        // 新建的文档

// 操作的用户信息
FileUpload fu = new FileUpload(request);
String f_weaver_belongto_userid=request.getParameter("f_weaver_belongto_userid");//需要增加的代码
String f_weaver_belongto_usertype=request.getParameter("f_weaver_belongto_usertype");//需要增加的代码
user = HrmUserVarify.getUser(request, response, f_weaver_belongto_userid, f_weaver_belongto_usertype) ;//需要增加的代码
int userid=user.getUID();                   //当前用户id
//System.out.println("-wfadd--89-222222222222222222222222222222222-userid------"+userid);
int usertype = 0;                           //用户在工作流表中的类型 0: 内部 1: 外部
String logintype = user.getLogintype();     //当前用户类型  1: 类别用户  2:外部用户
String username = "";
boolean wfmonitor="true".equals(session.getAttribute(userid+"_"+requestid+"wfmonitor"))?true:false;                //流程监控人
boolean haveBackright=false;            //强制收回权限
boolean haveOverright=false;            //强制归档权限
boolean haveStopright = false;			//暂停权限
boolean haveCancelright = false;		//撤销权限
boolean haveRestartright = false;		//启用权限
String currentnodetype = "";
int currentnodeid = 0;
int desrequestid=0;
int lastOperator=0;
String lastOperateDate="";
String lastOperateTime="";

if(logintype.equals("1"))
	username = Util.toScreen(ResourceComInfo.getResourcename(""+userid),user.getLanguage()) ;
if(logintype.equals("2"))
	username = Util.toScreen(CustomerInfoComInfo.getCustomerInfoname(""+userid),user.getLanguage());

if(logintype.equals("1")) usertype = 0;
if(logintype.equals("2")) usertype = 1;

String sql = "" ;
char flag = Util.getSeparator() ;
String fromPDA= Util.null2String((String)session.getAttribute("loginPAD"));   //从PDA登录

status = Util.null2String((String)session.getAttribute(userid+"_"+requestid+"status"));
requestname= Util.null2String((String)session.getAttribute(userid+"_"+requestid+"requestname"));
requestlevel=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"requestlevel"));
requestmark= Util.null2String((String)session.getAttribute(userid+"_"+requestid+"requestmark"));
creater= Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"creater"),0);
creatertype=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"creatertype"),0);
deleted=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"deleted"),0);
workflowid=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"workflowid"),0);
nodeid=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"nodeid"),0);
nodetype=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"nodetype"));
workflowname=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"workflowname"));
workflowtype=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"workflowtype"));
formid=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"formid"),0);
billid=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"billid"),0);
isbill=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"isbill"));
helpdocid=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"helpdocid"),0);
currentnodeid=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"currentnodeid"),0);
currentnodetype=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"currentnodetype"));

lastOperator=Util.getIntValue((String)session.getAttribute(userid+"_"+requestid+"lastOperator"),0);
lastOperateDate=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"lastOperateDate"));
lastOperateTime=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"lastOperateTime"));
String coadsigntype=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"coadsigntype"));

//资产自定义流程类型
    String cptwftype=CptWfUtil.getAllCptWftype(""+workflowid);
    boolean isCptwf=false;
    boolean ismodeCptwf=false;
    if(!"".equals(cptwftype)){
        isCptwf="fetch".equalsIgnoreCase(cptwftype)
                ||"move".equalsIgnoreCase(cptwftype)
                ||"lend".equalsIgnoreCase(cptwftype)
                ||"discard".equalsIgnoreCase(cptwftype)
                ||"back".equalsIgnoreCase(cptwftype)
                ||"loss".equalsIgnoreCase(cptwftype)
                ||"mend".equalsIgnoreCase(cptwftype)
                ||"mode_fetch".equalsIgnoreCase(cptwftype)
                ||"mode_move".equalsIgnoreCase(cptwftype)
                ||"mode_lend".equalsIgnoreCase(cptwftype)
                ||"mode_discard".equalsIgnoreCase(cptwftype)
                ||"mode_back".equalsIgnoreCase(cptwftype)
                ||"mode_loss".equalsIgnoreCase(cptwftype)
                ||"mode_mend".equalsIgnoreCase(cptwftype);
        ismodeCptwf=cptwftype.startsWith("mode_");
    }

//开启云部署后，自由节点流程是否已在高级设置中修改过
PortalUtil putil = new PortalUtil();
boolean cloudstatus = putil.isuserdeploy();
boolean cloudchange = false;
String iscnodefree = "0";
RecordSet.execute("select 1 from Workflow_Initialization where wfid = 35");
if(RecordSet.next()){
	cloudchange = true;
}
if(cloudstatus && cloudchange && workflowid == 35){
	iscnodefree = "1";
}
//是否具有分享权限
boolean iscanshare = false;
// 当前用户表中该请求对应的信息 isremark为0为当前操作者, isremark为1为当前被转发者,isremark为2为可跟踪查看者
//RecordSet.executeProc("workflow_currentoperator_SByUs",userid+""+flag+usertype+flag+requestid+"");
RecordSet.executeSql("select * from workflow_currentoperator where (isremark<8 or isremark>8) and requestid="+requestid+" and userid="+userid+" and usertype="+usertype+" order by isremark,islasttimes desc");
while(RecordSet.next())	{
	iscanshare = true;
    int tempisremark = Util.getIntValue(RecordSet.getString("isremark"),0) ;
	handleforwardid = Util.getIntValue(RecordSet.getString("handleforwardid"),-1) ;     //转办状态
	 takisremark = Util.getIntValue(RecordSet.getString("takisremark"),0) ;     //意见征询状态

    //if(tempisremark==8){//抄送（不提交）查看页面即更新为已办事宜。
    //	RecordSet2.executeSql("update workflow_currentoperator set isremark=2,operatedate='"+currentdate+"',operatetime='"+currenttime+"' where requestid="+requestid+" and userid="+userid+" and usertype="+usertype);
    //	response.sendRedirect("/workflow/request/ViewRequest.jsp?requestid="+requestid+"&isremark=8");
    //}
    if( tempisremark == 0 || tempisremark == 1 || tempisremark == 5 || tempisremark == 9|| tempisremark == 7) {                       // 当前操作者或被转发者
        isremark = tempisremark ;
        break ;
    }
}
if(isremark==-1){
	RecordSet.executeSql("select isremark from workflow_currentoperator where isremark=8 and requestid="+requestid+" and userid="+userid+" and usertype="+usertype);
	while(RecordSet.next())	{
	iscanshare = true;
    int tempisremark = Util.getIntValue(RecordSet.getString("isremark"),0) ;
    if(tempisremark==8){//抄送（不提交）查看页面即更新为已办事宜。
    	//RecordSet2.executeSql("update workflow_currentoperator set isremark=2,operatedate='"+currentdate+"',operatetime='"+currenttime+"' where requestid="+requestid+" and userid="+userid+" and usertype="+usertype);
    	//时间与数据库保持一致，所以采用存储过程来更新数据库。
    	RecordSet2.executeProc("workflow_CurrentOperator_Copy",requestid+""+flag+userid+flag+usertype+"");
    	if(currentnodetype.equals("3")){
    		RecordSet2.executeSql("update workflow_currentoperator set iscomplete=1 where requestid="+requestid+" and userid="+userid+" and usertype="+usertype);
    	} 
    	response.sendRedirect("/workflow/request/ViewRequest.jsp?requestid="+requestid+"&isremark=8&fromFlowDoc="+fromFlowDoc+"&f_weaver_belongto_userid="+userid+"&f_weaver_belongto_usertype="+usertype);
    	return;
    }
	}
}
if( isremark != 0 && isremark != 1&& isremark != 5 && isremark != 9&& isremark != 7) {
    response.sendRedirect("/notice/noright.jsp");
    return;
}
int currentstatus = -1;//当前流程状态(对应流程暂停、撤销而言)
String isModifyLog = "";
RecordSet.executeSql("select t1.ismodifylog,t2.currentstatus from workflow_base t1, workflow_requestbase t2 where t1.id=t2.workflowid and t2.requestid="+requestid);
if(RecordSet.next()) {
	currentstatus = Util.getIntValue(RecordSet.getString("currentstatus"));
	isModifyLog = RecordSet.getString("isModifyLog");
	
}
haveStopright = WfFunctionManageUtil.haveStopright(currentstatus,creater,user,currentnodetype,requestid,false);//流程不为暂停或者撤销状态，当前用户为流程发起人或者系统管理员，并且流程状态不为创建和归档
haveCancelright = WfFunctionManageUtil.haveCancelright(currentstatus,creater,user,currentnodetype,requestid,false);//流程不为撤销状态，当前用户为流程发起人，并且流程状态不为创建和归档
haveRestartright = WfFunctionManageUtil.haveRestartright(currentstatus,creater,user,currentnodetype,requestid,false);//流程为暂停或者撤销状态，当前用户为系统管理员，并且流程状态不为创建和归档
if(currentstatus>-1&&!haveCancelright&&!haveRestartright)
{
	String tips = "";
	if(currentstatus==0)
	{
		tips = SystemEnv.getHtmlLabelName(26154,user.getLanguage());//流程已暂停,请与流程发起人或系统管理员联系!
	}
	else
	{
		tips = SystemEnv.getHtmlLabelName(26155,user.getLanguage());//流程已撤销,请与系统管理员联系!
	}
%>
	<script language="JavaScript">
		var tips = '<%=tips%>';
		window.top.Dialog.alert(tips,function(){
			window.location.href="/notice/noright.jsp?isovertime=<%=isovertime%>";
		});
	</script>
<%
    return ;
}
String IsPendingForward=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsPendingForward"));
String IsTakingOpinions=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsTakingOpinions"));
String IsHandleForward=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsHandleForward"));
String IsBeForward=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsBeForward"));
String IsBeForwardTodo=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsBeForwardTodo"));
String IsBeForwardAlready=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsBeForwardAlready"));  // 已办转发
String IsBeForwardSubmitAlready=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsBeForwardSubmitAlready"));
String IsBeForwardSubmitNotaries=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsBeForwardSubmitNotaries"));
String IsFromWFRemark_T=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"IsFromWFRemark_T"));

RecordSet.executeSql("select * from workflow_flownode where workflowid="+workflowid+" and nodeid="+nodeid);
if(RecordSet.next()){
IsPendingForward=Util.null2String(RecordSet.getString("IsPendingForward"));
IsTakingOpinions=Util.null2String(RecordSet.getString("IsTakingOpinions"));
IsHandleForward=Util.null2String(RecordSet.getString("IsHandleForward"));
}

boolean canForwd = false;
if(isremark == 1 && takisremark ==2 && "1".equals(IsPendingForward)){  //征询意见回复\
canForwd = true;
}
if(takisremark !=2){
if(("0".equals(IsFromWFRemark_T) && "1".equals(IsBeForwardTodo)) 
        || ("1".equals(IsFromWFRemark_T) && "1".equals(IsBeForwardAlready)) 
        || ("2".equals(IsFromWFRemark_T) && "1".equals(IsBeForward))){
	canForwd = true;
}
}
boolean IsCanSubmit="true".equals(session.getAttribute(userid+"_"+requestid+"IsCanSubmit"))?true:false;
boolean IsFreeWorkflow="true".equals(session.getAttribute(userid+"_"+requestid+"IsFreeWorkflow"))?true:false;
boolean isImportDetail="true".equals(session.getAttribute(userid+"_"+requestid+"isImportDetail"))?true:false;
session.setAttribute(userid+"_"+requestid+"isremark",""+isremark);
boolean IsCanModify="true".equals(session.getAttribute(userid+"_"+requestid+"IsCanModify"))?true:false;
//String coadisforward=Util.null2String((String)session.getAttribute(userid+"_"+requestid+"coadisforward"));
boolean coadCanSubmit="true".equals(session.getAttribute(userid+"_"+requestid+"coadCanSubmit"))?true:false;
boolean isMainSubmitted="true".equals(session.getAttribute(userid+"_"+requestid+"isMainSubmitted"))?true:false;
RecordSet.executeSql("select isremark,nodeid from workflow_currentoperator where (isremark<8 or isremark>8) and requestid="+requestid+" and userid="+userid+" and usertype="+usertype+" order by isremark,islasttimes desc");
while(RecordSet.next())	{
    int tempisremark = Util.getIntValue(RecordSet.getString("isremark"),0) ;
    int tmpnodeid=Util.getIntValue(RecordSet.getString("nodeid"));
    if( tempisremark == 0 || tempisremark == 1 || tempisremark == 5 || tempisremark == 9|| tempisremark == 7) {                       // 当前操作者或被转发者
        isremark = tempisremark ;
        nodeid=tmpnodeid;
        nodetype=WFLinkInfo_nf.getNodeType(nodeid);
        break ;
    }
}
//add by mackjoe at 2005-12-20 增加模板应用
String ismode="";
int modeid=0;
int isform=0;
int showdes=0;
String isFormSignatureOfThisJsp=null;
String FreeWorkflowname="";
RecordSet.executeSql("select ismode,showdes,isFormSignature,freewfsetcurnameen,freewfsetcurnametw,freewfsetcurnamecn from workflow_flownode where workflowid="+workflowid+" and nodeid="+nodeid);
if(RecordSet.next()){
    ismode=Util.null2String(RecordSet.getString("ismode"));
    showdes=Util.getIntValue(Util.null2String(RecordSet.getString("showdes")),0);
	isFormSignatureOfThisJsp = Util.null2String(RecordSet.getString("isFormSignature"));
    if(user.getLanguage() == 8){
        FreeWorkflowname=Util.null2String(RecordSet.getString("freewfsetcurnameen"));
    }
    else if(user.getLanguage() == 9){
        FreeWorkflowname=Util.null2String(RecordSet.getString("freewfsetcurnametw"));
    }
    else {
        FreeWorkflowname=Util.null2String(RecordSet.getString("freewfsetcurnamecn"));
    }
}
int isUseWebRevisionOfThisJsp = Util.getIntValue(new weaver.general.BaseBean().getPropValue("weaver_iWebRevision","isUseWebRevision"), 0);
if(isUseWebRevisionOfThisJsp != 1){
	isFormSignatureOfThisJsp = "";
}
if("".equals(FreeWorkflowname.trim())){
	FreeWorkflowname = SystemEnv.getHtmlLabelName(21781,user.getLanguage());
}
session.setAttribute(userid+"_"+requestid+"FreeWorkflowname",FreeWorkflowname);

if(ismode.equals("1") && showdes!=1){
    RecordSet.executeSql("select id from workflow_nodemode where isprint='0' and workflowid="+workflowid+" and nodeid="+nodeid);
    if(RecordSet.next()){
        modeid=RecordSet.getInt("id");
    }else{
            RecordSet.executeSql("select id from workflow_formmode where isprint='0' and formid="+formid+" and isbill='"+isbill+"'");
        if(RecordSet.next()){
            modeid=RecordSet.getInt("id");
            isform=1;
        }
    }
}else if("2".equals(ismode)){
	weaver.workflow.exceldesign.HtmlLayoutOperate htmlLayoutOperate = new weaver.workflow.exceldesign.HtmlLayoutOperate();
	modeid = htmlLayoutOperate.getActiveHtmlLayout(workflowid, nodeid, 0);
}

//---------------------------------------------------------------------------------
//跨浏览器添加-当前浏览器是非IE，表单为单据，并且是未修改的单据，则跳转至公共页面 START
//---------------------------------------------------------------------------------
//模板模式-如果用户使用的是非IE则自动使用一般模式来显示流程 START 2011-11-23 CC
//if (!isIE.equalsIgnoreCase("true") && ismode.equals("1")) {
if (!isIE.equalsIgnoreCase("true") && ismode.equals("1") && modeid != 0) {
	String messageLableId = "";
	if (ismode.equals("1")) {
		messageLableId = "18017";
	} else {
		messageLableId = "23682";
	}
	ismode = "0";	
	//response.sendRedirect("/wui/common/page/sysRemind.jsp?labelid=" + messageLableId);
	%>

	<script type="text/javascript">
	
	window.parent.location.href = "/wui/common/page/sysRemind.jsp?labelid=<%=messageLableId %>&f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>;
	
	</script>

<%
	return;
}
//模板模式-如果用户使用的是非IE则自动使用一般模式来显示流程 END
//---------------------------------------------------------------------------------
//跨浏览器添加-当前浏览器是非IE，表单为单据，并且是未修改的单据，则跳转至公共页面 END
//---------------------------------------------------------------------------------


if(ismode.equals("1")&&modeid>0&&!fromPDA.equals("1")){
    response.sendRedirect("ManageRequestNoFormMode.jsp?fromFlowDoc="+fromFlowDoc+"&fromPDA="+fromPDA+"&requestid="+requestid+"&message="+message+"&topage="+topage+"&docfileid="+docfileid+"&newdocid="+newdocid+"&modeid="+modeid+"&isform="+isform+"&isovertime="+isovertime+"&isrequest="+isrequest+"&isaffirmance="+isaffirmance+"&reEdit="+reEdit+"&seeflowdoc="+seeflowdoc+"&isworkflowdoc="+isworkflowdoc+"&isfromtab="+isfromtab+"&f_weaver_belongto_userid="+userid+"&f_weaver_belongto_usertype="+usertype);
    return;
}
//end by mackjoe
ArrayList specialBillIDList = sysPubRefComInfo.getDetailCodeList("SpecialBillID");
boolean isSpecialBill = specialBillIDList.contains(""+formid);
/*------ xwj for td3131 20051117 ----- begin--------*/
if(isSpecialBill==true && isbill.equals("1")){
    topage = URLEncoder.encode(topage);
    response.sendRedirect("ManageRequestNoFormBill.jsp?f_weaver_belongto_userid="+userid+"&f_weaver_belongto_usertype="+usertype+"&fromFlowDoc="+fromFlowDoc+"&fromPDA="+fromPDA+"&requestid="+requestid+"&message="+message+"&topage="+topage+"&docfileid="+
            docfileid+"&newdocid="+newdocid+"&isovertime="+isovertime+"&isrequest="+isrequest+"&isaffirmance="+isaffirmance+"&reEdit="+reEdit+"&seeflowdoc="+seeflowdoc+"&isworkflowdoc="+isworkflowdoc+"&isfromtab="+isfromtab);
    return;
}
int IsFreeNode=0;
/*------ xwj for td3131 20051117 ----- end----------*/
RecordSet.executeProc("workflow_Nodebase_SelectByID",nodeid+"");
if(RecordSet.next()){
	isreopen=RecordSet.getString("isreopen");
	isreject=RecordSet.getString("isreject");
	IsFreeNode=Util.getIntValue(RecordSet.getString("IsFreeNode"),0);
}
// 记录查看日志
weaver.workflow.request.WorkflowIsFreeStartNode stnode=new weaver.workflow.request.WorkflowIsFreeStartNode();
String nodeidss= stnode.getIsFreeStartNode(""+nodeid);
String freedis=stnode.getNodeid(nodeidss);
String isornotFree=stnode.isornotFree(""+nodeid);
WFManager.setWfid(Util.getIntValue(""+workflowid));
WFManager.getWfInfo();
String isFree = WFManager.getIsFree();
 //判断是不是创建保存
 boolean iscreate=stnode.IScreateNode(""+requestid); 
  if(!isornotFree.equals("")&&!freedis.equals("")&&!iscreate&&(!"1".equals(isFree) || !"2".equals(nodetype))){
   isreject = "1";
 }

/*--  xwj for td2104 on 20050802 begin  --*/
boolean isOldWf = false;
RecordSet3.executeSql("select nodeid from workflow_currentoperator where requestid = " + requestid);
while(RecordSet3.next()){
	if(RecordSet3.getString("nodeid") == null || "".equals(RecordSet3.getString("nodeid")) || "-1".equals(RecordSet3.getString("nodeid"))){
			isOldWf = true;
	}
}

if(! currentnodetype.equals("3") )
    RecordSet.executeProc("SysRemindInfo_DeleteHasnewwf",""+userid+flag+usertype+flag+requestid);
else
    RecordSet.executeProc("SysRemindInfo_DeleteHasendwf",""+userid+flag+usertype+flag+requestid);

String imagefilename = "/images/hdReport_wev8.gif";
String titlename =  SystemEnv.getHtmlLabelName(18015,user.getLanguage())+":"
	                +SystemEnv.getHtmlLabelName(553,user.getLanguage())+" - "+Util.toScreen(workflowname,user.getLanguage())+ " - " + status + " "+"<span id='requestmarkSpan'>"+requestmark+"</span>";//Modify by 杨国生 2004-10-26 For TD1231
String needfav ="1";
String needhelp ="";
//if(helpdocid !=0 ) {titlename=titlename + "<img src=/images/help_wev8.gif style=\"CURSOR:hand\" width=12 onclick=\"location.href='/docs/docs/DocDsp.jsp?id="+helpdocid+"'\">";}
//判断是否有流程创建文档，并且在该节点是有正文字段
boolean docFlag=flowDoc.haveDocFiled(""+workflowid,""+nodeid);
String  docFlagss=docFlag?"1":"0";
session.setAttribute("requestAdd"+requestid,docFlagss);
//判断正文字段是否显示选择按钮
ArrayList newTextList = flowDoc.getDocFiled(""+workflowid);
if(newTextList != null && newTextList.size() > 0){
  String flowDocField = ""+newTextList.get(1);
  String newTextNodes = ""+newTextList.get(5);
  session.setAttribute("requestFlowDocField"+user.getUID(),flowDocField);
  session.setAttribute("requestAddNewNodes"+user.getUID(),newTextNodes);
}
%>

<HTML><HEAD>
<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
<LINK href="/css/rp_wev8.css" rel="STYLESHEET" type="text/css">
<script language=javascript src="/js/weaver_wev8.js"></script>
 
<script type='text/javascript' src='/dwr/interface/DocReadTagUtil.js'></script>
<script type='text/javascript' src='/dwr/engine.js'></script>
<script type='text/javascript' src='/dwr/util.js'></script>
<link href="/js/swfupload/default_wev8.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/swfupload/swfupload_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/swfupload.queue_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/fileprogressBywf_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/handlersBywf_wev8.js"></script>    
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" charset="UTF-8" src="/ueditor/custbtn/applocation_wev8.js"></script>
<script type="text/javascript" charset="UTF-8" src="/ueditor/custbtn/mapOperation.js"></script>
<!-- 
<style>
  .wordSpan{font-family:MS Shell Dlg,Arial;CURSOR: hand;font-weight:bold;FONT-SIZE: 10pt}
  
  #insertObjectContainer_remark{		
    position: fixed !important;
	top: 84px !important;
	left: 442.5px !important;
	z-index: 10010 !important;
  }
  .signResourceBlockClass,.signDoc,signWorkflow{
    cursor: pointer;
  }
  #insertObjectContainer_remark button{
   border-right: #6a6a6a 1px solid;   
   border-top: #6a6a6a 1px solid; 
   font-size: 12px;    
   background: #dedede;    
   padding: 3px;    
   border-left: #6a6a6a 1px solid;     
   border-bottom: #6a6a6a 1px solid;
  }
</style>
 -->
</head>
<title><%=requestname%></title>

<script language="javascript">

//TD4262 增加提示信息  开始
function windowOnload()
{
<%
	if( message.equals("1") ) {//工作流信息保存错误
%>
//	      contentBox = $G("divFavContent16332");
//          showObjectPopup(contentBox);
//          window.setTimeout("oPopup.hide()", 2000);
		  //var content="<%=SystemEnv.getHtmlLabelName(16332,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);
<%
	} else if( message.equals("2") ) {//工作流下一节点或下一节点操作者错误
%>
//	      contentBox = $G("divFavContent16333");
//          showObjectPopup(contentBox);
//          window.setTimeout("oPopup.hide()", 2000);
		  //var content="<%=SystemEnv.getHtmlLabelName(16333,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	} else if( message.equals("3") ) {//子流程创建人无值，请检查子流程创建人设置。
%>

		  //var content="<%=SystemEnv.getHtmlLabelName(19455,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	} else if( message.equals("4") ) {//已经流转到下一节点，不可以再提交。
%>

		  //var content="<%=SystemEnv.getHtmlLabelName(21266,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	} else if( message.equals("5") ) {//流程流转超时，请重试。
%>

		  //var content="<%=SystemEnv.getHtmlLabelName(21270,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	}else if( message.equals("6") ) {//转发失败，请重试！
%>

		  //var content="<%=SystemEnv.getHtmlLabelName(21766 ,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	} else if( message.equals("7") ) {//已经处理，不可重复处理！
%>

		  //var content="<%=SystemEnv.getHtmlLabelName(22751,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	} else if( message.equals("8") ) {//流程数据已更改，请核对后再处理！
%>

		  //var content="<%=SystemEnv.getHtmlLabelName(24676,user.getLanguage())%>";
		  //showPrompt(content);
          //window.setTimeout("message_table_Div.style.display='none';document.all.HelpFrame.style.display='none'", 2000);

<%
	}
%>

//window.setTimeout(function(){displayAllmenu();},6000);
displayAllmenu();
}
//TD4262 增加提示信息  结束

<%--added by xwj for td3247 20051201--%>
window.onbeforeunload =  function protectManageFormFlow(event){
	var opt = true;
	try {
		var __aeleclicktime = window.__aeleclicktime;
		var __currenttime = new Date().getTime();
		if (isIE()) {
			if (__currenttime <= __aeleclicktime + 500) {
				opt = false;
			}
		}
	} catch (e) {}

  		if(opt && !checkDataChange())
       return "<%=SystemEnv.getHtmlLabelName(18407,user.getLanguage())%>";
   }
     function doBack(){
         jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
		 <%if (!fromFlowDoc.equals("1")) {%>
        parent.location.href="/workflow/request/RequestView.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>"; //xwj for td3425 20051201
		<%}else {%>
        parent.parent.location.href="/workflow/request/RequestView.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>"; 
		<%}%>
   }
function replaceAll(strOrg,strFind,strReplace){ 
var index = 0; 
while(strOrg.indexOf(strFind,index) != -1){ 
strOrg = strOrg.replace(strFind,strReplace); 
index = strOrg.indexOf(strFind,index); 
} 
return strOrg 
} 

/** added by cyril on 2008-07-01 for TD:8835*/
function doViewModifyLog() {
	//window.open("/workflow/request/RequestModifyLogView.jsp?requestid=<%=requestid%>&nodeid=<%=nodeid%>&isAll=0&ismonitor=<%=wfmonitor?"1":"0"%>&urger=0");
	var dialog = new window.top.Dialog();
	dialog.currentWindow = window;
	var url = "/workflow/request/RequestModifyLogView.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid=<%=requestid%>&nodeid=<%=nodeid%>&isAll=0&ismonitor=<%=wfmonitor?"1":"0"%>&urger=0";
	dialog.Title = "<%=SystemEnv.getHtmlLabelName(21625,user.getLanguage())%>";
	dialog.Width = 1000;
	dialog.Height = 550;
	dialog.Drag = true;
	dialog.maxiumnable = true;
	dialog.URL = url;
	dialog.show();
}

/**
  解决表单序列化中文编码
  (jquery默认序列化采用的是encodeuricompent方法,转码成utf8,所以我们先反转,然后escape编码)
**/
function serialize(objs)

{

    var parmString = jQuery(objs).serialize();

    var parmArray = parmString.split("&");

    var parmStringNew="";

    jQuery.each(parmArray,function(index,data){

        var li_pos = data.indexOf("=");

        if(li_pos >0){

            var name = data.substring(0,li_pos);

            var value = escape(decodeURIComponent(data.substr(li_pos+1)));

            var parm = name+"="+value;

            parmStringNew = parmStringNew=="" ? parm : parmStringNew + '&' + parm;

        }

    });

    return parmStringNew;

}

//打开转发对话框
function openDialog(title, url) {　
	var dlg = new window.top.Dialog(); //定义Dialog对象
	dlg.currentWindow = window;
	dlg.Model = false;　　　
	dlg.Width = 1060; //定义长度
	dlg.Height = 500;　　　
	dlg.URL = url;　　　
	dlg.Title = "<%=SystemEnv.getHtmlLabelName(24964,user.getLanguage())%>";
	dlg.maxiumnable = true;　　　
	dlg.show();
	//保留对话框对象
	window.dialog = dlg;
}

//打开转发对话框
function openDialog2(title, url) {　
	var dlg = new window.top.Dialog(); //定义Dialog对象
	dlg.currentWindow = window;
	dlg.Model = false;　　　
	dlg.Width = 1060; //定义长度
	dlg.Height = 500;　　　
	dlg.URL = url;　　　
	dlg.Title = "<%=SystemEnv.getHtmlLabelName(82578,user.getLanguage())%>";
	dlg.maxiumnable = true;　　　
	dlg.show();
	//保留对话框对象
	window.dialog = dlg;
}

//打开转发对话框
function openDialog3(title, url) {　
	var dlg = new window.top.Dialog(); //定义Dialog对象
	dlg.currentWindow = window;
	dlg.Model = false;　　　
	dlg.Width = 1060; //定义长度
	dlg.Height = 500;　　　
	dlg.URL = url;　　　
	dlg.Title = "<%=SystemEnv.getHtmlLabelName(23746,user.getLanguage())%>";
	dlg.maxiumnable = true;　　　
	dlg.show();
	//保留对话框对象
	window.dialog = dlg;
}

/** end by cyril on 008-07-01 for TD:8835*/

function doLocationHref(resourceid){
    //获取相关文档-相关流程-相关附件参数
    //var signdocids=$G("signdocids").value;
    //var signworkflowids=$G("signworkflowids").value;
	var signworkflowids='';
	var param = '';
    //附件上传参数
    //var param = "&annexmainId=" + signannexParam.annexmainId + "&annexsubId=" + signannexParam.annexsubId + "&annexsecId=" + signannexParam.annexsecId + "&userid=" + signannexParam.userid + "&logintype=" + signannexParam.logintype + "&annexmaxUploadImageSize=" + signannexParam.annexmaxUploadImageSize + "&userlanguage=" + signannexParam.userlanguage + "&field_annexupload=" + $G("field-annexupload").value + "&field_annexupload_del_id=" + $G("field_annexupload_del_id").value;
    //获取参数end
	var id = <%=requestid%>;
	var workflowRequestLogId=0;
	var remarklag = "";

   // var oEditor = CKEDITOR.instances['remark'];

	//alert(oEditor.getData());

	if($G("workflowRequestLogId")!=null){
		workflowRequestLogId=$G("workflowRequestLogId").value;
	}
	try{
		CkeditorExt.updateContent('remark');

		var params=serialize("#formitem");


		//附件上传
                        StartUploadAll();
                        checkfileuploadcomplet();
		var remark="";
		try{
			remark = CkeditorExt.getHtml("remark");
		}catch(e){}
		 //frmmain.target = "_blank";
		 //frmmain.action = "/workflow/request/Remark.jsp?requestid="+id+"&workflowRequestLogId="+workflowRequestLogId;
         //document.frmmain.submit();
		if(resourceid)
		openDialog("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid + "&requestname=<%=requestname %>");
		else
        openDialog("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&requestname=<%=requestname %>");

	}catch(e){
		var remark="";
		try{
			remark = CkeditorExt.getHtml("remark");
		}catch(e){}
		var forwardurl = "/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&signworkflowids="+signworkflowids+param+"&workflowRequestLogId="+workflowRequestLogId;
		openFullWindowHaveBar(forwardurl);
	}
}

function doLocationHref(resourceid,forwardflag){
    //获取相关文档-相关流程-相关附件参数
    //var signdocids=$G("signdocids").value;
    //var signworkflowids=$G("signworkflowids").value;
	var signworkflowids='';
	var param = '';
    //附件上传参数
    //var param = "&annexmainId=" + signannexParam.annexmainId + "&annexsubId=" + signannexParam.annexsubId + "&annexsecId=" + signannexParam.annexsecId + "&userid=" + signannexParam.userid + "&logintype=" + signannexParam.logintype + "&annexmaxUploadImageSize=" + signannexParam.annexmaxUploadImageSize + "&userlanguage=" + signannexParam.userlanguage + "&field_annexupload=" + $G("field-annexupload").value + "&field_annexupload_del_id=" + $G("field_annexupload_del_id").value;
    //获取参数end
	var id = <%=requestid%>;
	var workflowRequestLogId=0;
	//var remarklag = flag;

   // var oEditor = CKEDITOR.instances['remark'];

	//alert(oEditor.getData());

	if($G("workflowRequestLogId")!=null){
		workflowRequestLogId=$G("workflowRequestLogId").value;
	}
	try{
		CkeditorExt.updateContent('remark');

		var params=serialize("#formitem");


		//附件上传
                        StartUploadAll();
                        checkfileuploadcomplet();
		var remark="";
		try{
			remark = CkeditorExt.getHtml("remark");
		}catch(e){}
		 //frmmain.target = "_blank";
		 //frmmain.action = "/workflow/request/Remark.jsp?requestid="+id+"&workflowRequestLogId="+workflowRequestLogId;
         //document.frmmain.submit();
		if(forwardflag==2){
		if(resourceid)
		openDialog2("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid +"&forwardflag="+forwardflag + "&requestname=<%=requestname %>");
		else
        openDialog2("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&forwardflag=" + forwardflag + "&requestname=<%=requestname %>");
		}else if (forwardflag==3)
		{
			if(resourceid)
		openDialog3("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid +"&forwardflag="+forwardflag + "&requestname=<%=requestname %>");
		else
        openDialog3("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&forwardflag=" + forwardflag + "&requestname=<%=requestname %>");
		}else{
		if(resourceid)
		openDialog("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid +"&forwardflag="+forwardflag + "&requestname=<%=requestname %>");
		else
        openDialog("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&forwardflag=" + forwardflag + "&requestname=<%=requestname %>");
		}
	}catch(e){
		var remark="";
		try{
			remark = CkeditorExt.getHtml("remark");
		}catch(e){}
		var forwardurl = "/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+ "&forwardflag="+ forwardflag +"&signworkflowids="+signworkflowids+param+"&workflowRequestLogId="+workflowRequestLogId;
		openFullWindowHaveBar(forwardurl);
	}
}

function doLocationHrefnoback(resourceid,forwardflag){
    //获取相关文档-相关流程-相关附件参数
    //var signdocids=$G("signdocids").value;
    //var signworkflowids=$G("signworkflowids").value;
	var signworkflowids='';
	var param = '';
    //附件上传参数
    //var param = "&annexmainId=" + signannexParam.annexmainId + "&annexsubId=" + signannexParam.annexsubId + "&annexsecId=" + signannexParam.annexsecId + "&userid=" + signannexParam.userid + "&logintype=" + signannexParam.logintype + "&annexmaxUploadImageSize=" + signannexParam.annexmaxUploadImageSize + "&userlanguage=" + signannexParam.userlanguage + "&field_annexupload=" + $G("field-annexupload").value + "&field_annexupload_del_id=" + $G("field_annexupload_del_id").value;
    //获取参数end
	var id = <%=requestid%>;
	var workflowRequestLogId=0;
	//var remarklag = flag;

   // var oEditor = CKEDITOR.instances['remark'];

	//alert(oEditor.getData());

	if($G("workflowRequestLogId")!=null){
		workflowRequestLogId=$G("workflowRequestLogId").value;
	}
	try{
		CkeditorExt.updateContent('remark');

		var params=serialize("#formitem");


		//附件上传
                        StartUploadAll();
                        checkfileuploadcomplet();
		var remark="";
		try{
			remark = CkeditorExt.getHtml("remark");
		}catch(e){}
		 //frmmain.target = "_blank";
		 //frmmain.action = "/workflow/request/Remark.jsp?requestid="+id+"&workflowRequestLogId="+workflowRequestLogId;
         //document.frmmain.submit();
		if(forwardflag==2){
		if(resourceid)
		openDialog2("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid +"&forwardflag="+forwardflag+"&needwfback=0&requestname=<%=requestname %>");
		else
        openDialog2("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&forwardflag=" + forwardflag + "&needwfback=0&requestname=<%=requestname %>");
		}else if (forwardflag==3)
		{
			if(resourceid)
		openDialog3("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid +"&forwardflag="+forwardflag + "&needwfback=0&requestname=<%=requestname %>");
		else
        openDialog3("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&forwardflag=" + forwardflag + "&needwfback=0&requestname=<%=requestname %>");
		}else{
		if(resourceid)
		openDialog("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params+"&resourceids="+resourceid +"&forwardflag="+forwardflag + "&needwfback=0&requestname=<%=requestname %>");
		else
        openDialog("<%=requestname%>","/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+"&workflowRequestLogId="+workflowRequestLogId+"&"+params + "&forwardflag=" + forwardflag + "&needwfback=0&requestname=<%=requestname %>");
		}
	}catch(e){
		var remark="";
		try{
			remark = CkeditorExt.getHtml("remark");
		}catch(e){}
		var forwardurl = "/workflow/request/Remark.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid="+id+ "&forwardflag="+ forwardflag +"&needwfback=0&signworkflowids="+signworkflowids+param+"&workflowRequestLogId="+workflowRequestLogId;
		openFullWindowHaveBar(forwardurl);
	}
}


/**
转发
resourceid（转发人id）
**/
function doReview(resourceid){
         jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
//保存签章数据
<%if("1".equals(isFormSignatureOfThisJsp)){%>
				try{  
					if(typeof(eval("SaveSignature"))=="function"){
	                    //转发时不验证签字意见必填
	                    if(SaveSignature()||!SaveSignature()){
                            doLocationHref(resourceid);
                        }else{
							if(isDocEmpty==1){
								alert("\""+"<%=SystemEnv.getHtmlLabelName(17614,user.getLanguage())%>"+"\""+"<%=SystemEnv.getHtmlLabelName(21423,user.getLanguage())%>");
								isDocEmpty=0;
							}else{
							    alert("<%=SystemEnv.getHtmlLabelName(21442,user.getLanguage())%>");
							}
							return ;
						}
					   }
				    }catch(e){
				    	doLocationHref(resourceid);
				    }
<%}else{%>
                        doLocationHref(resourceid);
<%}%>

   }
   
   /**
征求意见
resourceid（转发人id）
**/
function doReview2(resourceid){
         jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
//保存签章数据
<%if("1".equals(isFormSignatureOfThisJsp)){%>
				try{  
					if(typeof(eval("SaveSignature"))=="function"){
	                    //转发时不验证签字意见必填
	                    if(SaveSignature()||!SaveSignature()){
                            doLocationHref(resourceid,2);
                        }else{
							if(isDocEmpty==1){
								alert("\""+"<%=SystemEnv.getHtmlLabelName(17614,user.getLanguage())%>"+"\""+"<%=SystemEnv.getHtmlLabelName(21423,user.getLanguage())%>");
								isDocEmpty=0;
							}else{
							    alert("<%=SystemEnv.getHtmlLabelName(21442,user.getLanguage())%>");
							}
							return ;
						}
					   }
				    }catch(e){
				    	doLocationHref(resourceid,2);
				    }
<%}else{%>
                        doLocationHref(resourceid,2);
<%}%>

   }
   
   
   /**
转办
resourceid（转发人id）
**/
function doReview3(resourceid){
         jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
//保存签章数据
<%if("1".equals(isFormSignatureOfThisJsp)){%>
				try{  
					if(typeof(eval("SaveSignature"))=="function"){
	                    //转发时不验证签字意见必填
	                    if(SaveSignature()||!SaveSignature()){
                            doLocationHref(resourceid,3);
                        }else{
							if(isDocEmpty==1){
								alert("\""+"<%=SystemEnv.getHtmlLabelName(17614,user.getLanguage())%>"+"\""+"<%=SystemEnv.getHtmlLabelName(21423,user.getLanguage())%>");
								isDocEmpty=0;
							}else{
							    alert("<%=SystemEnv.getHtmlLabelName(21442,user.getLanguage())%>");
							}
							return ;
						}
					   }
				    }catch(e){
				    	doLocationHref(resourceid,3);
				    }
<%}else{%>
                        doLocationHref(resourceid,3);
<%}%>

   }
   
      /**
转办
resourceid（转发人id）
**/
function doReviewback3(resourceid){
			$G("needwfback").value = "1";
         jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
//保存签章数据
<%if("1".equals(isFormSignatureOfThisJsp)){%>
				try{  
					if(typeof(eval("SaveSignature"))=="function"){
	                    //转发时不验证签字意见必填
	                    if(SaveSignature()||!SaveSignature()){
                            doLocationHref(resourceid,3);
                        }else{
							if(isDocEmpty==1){
								alert("\""+"<%=SystemEnv.getHtmlLabelName(17614,user.getLanguage())%>"+"\""+"<%=SystemEnv.getHtmlLabelName(21423,user.getLanguage())%>");
								isDocEmpty=0;
							}else{
							    alert("<%=SystemEnv.getHtmlLabelName(21442,user.getLanguage())%>");
							}
							return ;
						}
					   }
				    }catch(e){
				    	doLocationHref(resourceid,3);
				    }
<%}else{%>
                        doLocationHref(resourceid,3);
<%}%>

   }

   
      /**
转办
resourceid（转发人id）
**/
function doReviewnoback3(resourceid){
	$G("needwfback").value = "0";
         jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
//保存签章数据
<%if("1".equals(isFormSignatureOfThisJsp)){%>
				try{  
					if(typeof(eval("SaveSignature"))=="function"){
	                    //转发时不验证签字意见必填
	                    if(SaveSignature()||!SaveSignature()){
                            doLocationHrefnoback(resourceid,3);
                        }else{
							if(isDocEmpty==1){
								alert("\""+"<%=SystemEnv.getHtmlLabelName(17614,user.getLanguage())%>"+"\""+"<%=SystemEnv.getHtmlLabelName(21423,user.getLanguage())%>");
								isDocEmpty=0;
							}else{
							    alert("<%=SystemEnv.getHtmlLabelName(21442,user.getLanguage())%>");
							}
							return ;
						}
					   }
				    }catch(e){
				    	doLocationHrefnoback(resourceid,3);
				    }
<%}else{%>
                        doLocationHrefnoback(resourceid,3);
<%}%>

   }



var isfirst = 0 ;

function displaydiv()
{
    if(oDivAll.style.display == ""){
		oDivAll.style.display = "none";
		oDivInner.style.display = "none";
        oDiv.style.display = "none";

        spanimage.innerHTML = "<img src='/images/ArrowDownRed_wev8.gif' border=0>" ;
    }
    else{
        if(isfirst == 0) {
			$G("picInnerFrame").src="/workflow/request/WorkflowRequestPictureInner.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&fromFlowDoc=<%=fromFlowDoc%>&modeid=<%=modeid%>&requestid=<%=requestid%>&workflowid=<%=workflowid%>&nodeid=<%=nodeid%>&isbill=<%=isbill%>&formid=<%=formid%>";				
			$G("picframe").src="/workflow/request/WorkflowRequestPicture.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&requestid=<%=requestid%>&workflowid=<%=workflowid%>&nodeid=<%=nodeid%>&isbill=<%=isbill%>&formid=<%=formid%>";

            isfirst ++ ;
        }

        spanimage.innerHTML = "<img src='/images/ArrowUpGreen_wev8.gif' border=0>" ;
		oDivAll.style.display = "";
		oDivInner.style.display = "";
        oDiv.style.display = "";
        workflowStatusLabelSpan.innerHTML="<font color=green><%=SystemEnv.getHtmlLabelName(19678,user.getLanguage())%></font>";
        workflowChartLabelSpan.innerHTML="<font color=green><%=SystemEnv.getHtmlLabelName(19676,user.getLanguage())%></font>";
    }
}


function displaydivOuter()
{
    if(oDiv.style.display == ""){
        oDiv.style.display = "none";
        workflowStatusLabelSpan.innerHTML="<font color=red><%=SystemEnv.getHtmlLabelName(19677,user.getLanguage())%></font>";
		if(oDiv.style.display == "none"&&oDivInner.style.display == "none"){
		    oDivAll.style.display = "none";
            spanimage.innerHTML = "<img src='/images/ArrowDownRed_wev8.gif' border=0>" ;
		}
    }
    else{
        oDiv.style.display = "";
        workflowStatusLabelSpan.innerHTML="<font color=green><%=SystemEnv.getHtmlLabelName(19678,user.getLanguage())%></font>";
    }
}

function displaydivInner()
{
    if(oDivInner.style.display == ""){
        oDivInner.style.display = "none";
        workflowChartLabelSpan.innerHTML="<font color=red><%=SystemEnv.getHtmlLabelName(19675,user.getLanguage())%></font>";
		if(oDiv.style.display == "none"&&oDivInner.style.display == "none"){
		    oDivAll.style.display = "none";
            spanimage.innerHTML = "<img src='/images/ArrowDownRed_wev8.gif' border=0>" ;
		}
    }
    else{
        oDivInner.style.display = "";
        workflowChartLabelSpan.innerHTML="<font color=green><%=SystemEnv.getHtmlLabelName(19676,user.getLanguage())%></font>";
    }
}


function downloads(files)
{ jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
$G("fileDownload").src="/weaver/weaver.file.FileDownload?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&fileid="+files+"&download=1&requestid=<%=requestid%>&desrequestid=<%=desrequestid%>&fromrequest=1";
}
function downloadsBatch(fieldvalue,requestid)
{ 
jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
$G("fileDownload").src="/weaver/weaver.file.FileDownload?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&fieldvalue="+fieldvalue+"&download=1&downloadBatch=1&desrequestid=<%=desrequestid%>&fromrequest=1&requestid="+requestid;
}
function opendoc(showid,versionid,docImagefileid)
{jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
openFullWindowHaveBar("/docs/docs/DocDspExt.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&id="+showid+"&imagefileId="+docImagefileid+"&isFromAccessory=true&isrequest=1&requestid=<%=requestid%>&desrequestid=<%=desrequestid%>");
}
function opendoc1(showid)
{
jQuery($GetEle("flowbody")).attr("onbeforeunload", "");
openFullWindowHaveBar("/docs/docs/DocDsp.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&id="+showid+"&isrequest=1&requestid=<%=requestid%>&desrequestid=<%=desrequestid%>&isOpenFirstAss=1");
}
<%-----------xwj for td3131 20051115 begin -----%>
//主表中金额转换字段调用
function numberToFormat(index){
    if($G("field_lable"+index).value != ""){
        var floatNum = floatFormat($G("field_lable"+index).value);
        var val = numberChangeToChinese(floatNum)
        if(val == ""){
            alert("<%=SystemEnv.getHtmlLabelName(31181,user.getLanguage())%>");
            $G("field"+index).value = "";
            $G("field_lable"+index).value = "";
            $G("field_chinglish"+index).value = "";
        } else {
            $G("field"+index).value = floatNum;
            $G("field_lable"+index).value = milfloatFormat(floatNum);
            $G("field_chinglish"+index).value = val;
        }
    }else{
        $G("field"+index).value = "";
        $G("field_chinglish"+index).value = "";
    }
}
function FormatToNumber(index){
	var elm = $GetEle("field_lable"+index);
	var n = getLocation(elm);
    if($G("field_lable"+index).value != ""){
        $G("field_lable"+index).value = $G("field"+index).value;
    }else{
        $G("field"+index).value = "";
        $G("field_chinglish"+index).value = "";
    }
	setLocation(elm,n);
}
<%-----------xwj for td3131 20051115 end -----%>

//明细表中金额转换字段调用
function numberToChinese(index){
    if($G("field_lable"+index).value != ""){
        var floatNum = floatFormat($G("field_lable"+index).value);
        var val = numberChangeToChinese(floatNum);
        if(val == ""){
            alert("<%=SystemEnv.getHtmlLabelName(31181,user.getLanguage())%>");
            $G("field_lable"+index).value = "";
            $G("field"+index).value = "";
        }else{
            $G("field_lable"+index).value = val;
            $G("field"+index).value = floatNum;
        }
    } else {
        $G("field"+index).value = "";
    }
}
function ChineseToNumber(index){
if($G("field_lable"+index).value != ""){
$G("field_lable"+index).value = chineseChangeToNumber($G("field_lable"+index).value);
$G("field"+index).value = $G("field_lable"+index).value;
}
else{
$G("field"+index).value = "";
}
}
</SCRIPT>

<BODY id="flowbody" <%if (docFlag) {%> onload="changeTab()"<%}%> <%if (!docFlag&&!fromPDA.equals("1")) {%>  <%}%>><%--Modified by xwj for td3247 20051201--%>
<%if (!docFlag) {%>
<%@ include file="RequestTopTitle.jsp" %>
<%}%>
<iframe id="fileDownload" frameborder=0 scrolling=no src=""  style="display:none"></iframe>
<iframe id="triSubwfIframe" frameborder=0 scrolling=no src=""  style="display:none"></iframe>

<%//TD9145
Prop prop = Prop.getInstance();
String ifchangstatus = Util.null2String(prop.getPropValue(GCONST.getConfigFile() , "ecology.changestatus"));
String submitname = "" ; // 提交按钮的名称 : 创建, 审批, 实现
String forwardName = "";//转发
String takingopinionsName = ""; //征求意见
String HandleForwardName = ""; //转办
String forhandName = ""; //转办提交
String forhandbackName = ""; //转办需反馈
String forhandnobackName = ""; //转办不需反馈
String givingopinionsName ="";  //回复
String givingOpinionsnobackName = ""; // 回复不反馈
String givingOpinionsbackName = ""; // 回复需反馈

String saveName = "";//保存
String rejectName = "";//退回
String forsubName = "";//转发提交
String ccsubName = "";//抄送提交
String newWFName = "";//新建流程按钮
String newSMSName = "";//新建短信按钮
String haswfrm = "";//是否使用新建流程按钮
String hassmsrm = "";//是否使用新建短信按钮
int t_workflowid = 0;//新建流程的ID
String subnobackName = "";//提交不需反馈
String subbackName = "";//提交需反馈
String hasnoback = "";//使用提交不需反馈按钮
String hasback = "";//使用提交需反馈按钮
String forsubnobackName = "";//转发批注不需反馈
String forsubbackName = "";//转发批注需反馈
String hasfornoback = "";//使用转发批注不需反馈按钮
String hasforback = "";//使用转发批注需反馈按钮
String ccsubnobackName = "";//抄送批注不需反馈
String ccsubbackName = "";//抄送批注需反馈
String hasccnoback = "";//使用抄送批注不需反馈按钮
String hasccback = "";//使用抄送批注需反馈按钮
String newOverTimeName=""; //超时设置按钮
String hasovertime="";    //是否使用超时设置按钮
String newCHATSName = "";//新建微信按钮 (QC:98106)
String haschats = "";//是否使用新建微信按钮 (QC:98106)

String hasforhandback = "";  //是否转办反馈
String hasforhandnoback = ""; //是否转办不需反馈
String hastakingOpinionsback = ""; //是否回复反馈
String hastakingOpinionsnoback = "";//是否回复不需反馈
String isSubmitDirect = ""; // 是否启用提交至退回节点
String submitDirectName = ""; // 提交至退回节点按钮名称
int subbackCtrl = 0;
int forhandbackCtrl = 0;
int forsubbackCtrl = 0;
int ccsubbackCtrl = 0;
int takingOpinionsbackCtrl = 0;

String sqlselectName = "select * from workflow_nodecustomrcmenu where wfid="+workflowid+" and nodeid="+nodeid;
String sqlselectNewName = "select * from workflow_nodeCustomNewMenu where enable = 1 and wfid=" + workflowid + " and nodeid=" + nodeid + " order by menuType, id";
if(isremark != 0){
	RecordSet.executeSql("select nodeid from workflow_currentoperator c where c.requestid="+requestid+" and c.userid="+userid+" and c.usertype="+usertype+" and c.isremark='"+isremark+"' ");
	String tmpnodeid="";
	if(RecordSet.next()){
		tmpnodeid = Util.null2String(RecordSet.getString("nodeid"));
	}
	sqlselectName = "select * from workflow_nodecustomrcmenu where wfid="+workflowid+" and nodeid="+tmpnodeid;
	sqlselectNewName = "select * from workflow_nodeCustomNewMenu where enable = 1 and wfid=" + workflowid + " and nodeid=" + tmpnodeid + " order by menuType, id";
}
RecordSet.executeSql(sqlselectName);

if(RecordSet.next()){
	if(user.getLanguage() == 7){
		submitname = Util.null2String(RecordSet.getString("submitname7"));
		forwardName = Util.null2String(RecordSet.getString("forwardName7"));

		takingopinionsName = Util.null2String(RecordSet.getString("takingOpName7"));
		HandleForwardName = Util.null2String(RecordSet.getString("forhandName7"));
		forhandnobackName = Util.null2String(RecordSet.getString("forhandnobackName7"));
		forhandbackName = Util.null2String(RecordSet.getString("forhandbackName7"));
		givingopinionsName = Util.null2String(RecordSet.getString("takingOpinionsName7"));
		givingOpinionsnobackName = Util.null2String(RecordSet.getString("takingOpinionsnobackName7"));
		givingOpinionsbackName = Util.null2String(RecordSet.getString("takingOpinionsbackName7"));

		saveName = Util.null2String(RecordSet.getString("saveName7"));
		rejectName = Util.null2String(RecordSet.getString("rejectName7"));
		forsubName = Util.null2String(RecordSet.getString("forsubName7"));
		ccsubName = Util.null2String(RecordSet.getString("ccsubName7"));
		newWFName = Util.null2String(RecordSet.getString("newWFName7"));
		newSMSName = Util.null2String(RecordSet.getString("newSMSName7"));
		newCHATSName = Util.null2String(RecordSet.getString("newCHATSName7")); //微信提醒(QC:98106)
		subnobackName = Util.null2String(RecordSet.getString("subnobackName7"));
		subbackName = Util.null2String(RecordSet.getString("subbackName7"));
		forsubnobackName = Util.null2String(RecordSet.getString("forsubnobackName7"));
		forsubbackName = Util.null2String(RecordSet.getString("forsubbackName7"));
		ccsubnobackName = Util.null2String(RecordSet.getString("ccsubnobackName7"));
		ccsubbackName = Util.null2String(RecordSet.getString("ccsubbackName7"));
        newOverTimeName = Util.null2String(RecordSet.getString("newOverTimeName7"));
        submitDirectName = Util.null2String(RecordSet.getString("submitDirectName7"));
	}
	else if(user.getLanguage() == 9){
		submitname = Util.null2String(RecordSet.getString("submitname9"));
		forwardName = Util.null2String(RecordSet.getString("forwardName9"));

		takingopinionsName = Util.null2String(RecordSet.getString("takingOpName9"));
		HandleForwardName = Util.null2String(RecordSet.getString("forhandName9"));
		forhandnobackName = Util.null2String(RecordSet.getString("forhandnobackName9"));
		forhandbackName = Util.null2String(RecordSet.getString("forhandbackName9"));
		givingopinionsName = Util.null2String(RecordSet.getString("takingOpinionsName9"));
		givingOpinionsnobackName = Util.null2String(RecordSet.getString("takingOpinionsnobackName9"));
		givingOpinionsbackName = Util.null2String(RecordSet.getString("takingOpinionsbackName9"));

		saveName = Util.null2String(RecordSet.getString("saveName9"));
		rejectName = Util.null2String(RecordSet.getString("rejectName9"));
		forsubName = Util.null2String(RecordSet.getString("forsubName9"));
		ccsubName = Util.null2String(RecordSet.getString("ccsubName9"));
		newWFName = Util.null2String(RecordSet.getString("newWFName9"));
		newSMSName = Util.null2String(RecordSet.getString("newSMSName9"));
		newCHATSName = Util.null2String(RecordSet.getString("newCHATSName9")); //微信提醒(QC:98106)
		subnobackName = Util.null2String(RecordSet.getString("subnobackName9"));
		subbackName = Util.null2String(RecordSet.getString("subbackName9"));
		forsubnobackName = Util.null2String(RecordSet.getString("forsubnobackName9"));
		forsubbackName = Util.null2String(RecordSet.getString("forsubbackName9"));
		ccsubnobackName = Util.null2String(RecordSet.getString("ccsubnobackName9"));
		ccsubbackName = Util.null2String(RecordSet.getString("ccsubbackName9"));
        newOverTimeName = Util.null2String(RecordSet.getString("newOverTimeName9"));
        submitDirectName = Util.null2String(RecordSet.getString("submitDirectName9"));
	}
	else{
		submitname = Util.null2String(RecordSet.getString("submitname8"));
		forwardName = Util.null2String(RecordSet.getString("forwardName8"));

		takingopinionsName = Util.null2String(RecordSet.getString("takingOpName8"));
		HandleForwardName = Util.null2String(RecordSet.getString("forhandName8"));
		forhandnobackName = Util.null2String(RecordSet.getString("forhandnobackName8"));
		forhandbackName = Util.null2String(RecordSet.getString("forhandbackName8"));
		givingopinionsName = Util.null2String(RecordSet.getString("takingOpinionsName8"));
		givingOpinionsnobackName = Util.null2String(RecordSet.getString("takingOpinionsnobackName8"));
		givingOpinionsbackName = Util.null2String(RecordSet.getString("takingOpinionsbackName8"));

		saveName = Util.null2String(RecordSet.getString("saveName8"));
		rejectName = Util.null2String(RecordSet.getString("rejectName8"));
		forsubName = Util.null2String(RecordSet.getString("forsubName8"));
		ccsubName = Util.null2String(RecordSet.getString("ccsubName8"));
		newWFName = Util.null2String(RecordSet.getString("newWFName8"));
		newSMSName = Util.null2String(RecordSet.getString("newSMSName8"));
		newCHATSName = Util.null2String(RecordSet.getString("newCHATSName8")); //微信提醒(QC:98106)
		subnobackName = Util.null2String(RecordSet.getString("subnobackName8"));
		subbackName = Util.null2String(RecordSet.getString("subbackName8"));
		forsubnobackName = Util.null2String(RecordSet.getString("forsubnobackName8"));
		forsubbackName = Util.null2String(RecordSet.getString("forsubbackName8"));
		ccsubnobackName = Util.null2String(RecordSet.getString("ccsubnobackName8"));
		ccsubbackName = Util.null2String(RecordSet.getString("ccsubbackName8"));
        newOverTimeName = Util.null2String(RecordSet.getString("newOverTimeName8"));
        submitDirectName = Util.null2String(RecordSet.getString("submitDirectName8"));
	}
	haschats = Util.null2String(RecordSet.getString("haschats"));//微信提醒(QC:98106)
	haswfrm = Util.null2String(RecordSet.getString("haswfrm"));
	hassmsrm = Util.null2String(RecordSet.getString("hassmsrm"));
	hasnoback = Util.null2String(RecordSet.getString("hasnoback"));
	hasback = Util.null2String(RecordSet.getString("hasback"));
	hasfornoback = Util.null2String(RecordSet.getString("hasfornoback"));
	hasforback = Util.null2String(RecordSet.getString("hasforback"));
	hasccnoback = Util.null2String(RecordSet.getString("hasccnoback"));
	hasccback = Util.null2String(RecordSet.getString("hasccback"));
	t_workflowid = Util.getIntValue(RecordSet.getString("workflowid"), 0);
    hasovertime = Util.null2String(RecordSet.getString("hasovertime"));
	hasforhandback = Util.null2String(RecordSet.getString("hasforhandback"));
	hasforhandnoback = Util.null2String(RecordSet.getString("hasforhandnoback"));
	hastakingOpinionsback = Util.null2String(RecordSet.getString("hastakingOpinionsback"));
	hastakingOpinionsnoback = Util.null2String(RecordSet.getString("hastakingOpinionsnoback"));
	isSubmitDirect = Util.null2String(RecordSet.getString("isSubmitDirect"));
	subbackCtrl = Util.getIntValue(Util.null2String(RecordSet.getString("subbackCtrl")), 0);
	forhandbackCtrl = Util.getIntValue(Util.null2String(RecordSet.getString("forhandbackCtrl")), 0);
	forsubbackCtrl = Util.getIntValue(Util.null2String(RecordSet.getString("forsubbackCtrl")), 0);
	ccsubbackCtrl = Util.getIntValue(Util.null2String(RecordSet.getString("ccsubbackCtrl")), 0);
	takingOpinionsbackCtrl = Util.getIntValue(Util.null2String(RecordSet.getString("takingOpinionsbackCtrl")), 0);
}
ArrayList newMenuList0 = new ArrayList(); // 新建流程菜单列表
ArrayList newMenuList1 = new ArrayList(); // 新建短信菜单列表
ArrayList newMenuList2 = new ArrayList(); // 新建微信菜单列表
RecordSet.executeSql(sqlselectNewName);
while(RecordSet.next()) {
	int menuType = Util.getIntValue(RecordSet.getString("menuType"), -1);
	if(menuType < 0) {
		continue;
	}
	HashMap newMenuMap = new HashMap();
	newMenuMap.put("id", Util.getIntValue(RecordSet.getString("id"), 0));
	newMenuMap.put("newName", Util.null2String(RecordSet.getString("newName" + user.getLanguage())));
	if(0 == menuType) {
		newMenuMap.put("workflowid", Util.getIntValue(RecordSet.getString("workflowid"), 0));
		newMenuList0.add(newMenuMap);
	}else if(1 == menuType) {
		newMenuList1.add(newMenuMap);
	}else if(2 == menuType) {
		newMenuList2.add(newMenuMap);
	}
}
String lastnodeid = ""; // 上一次退回操作的节点id
String isSubmitDirectNode = ""; // 上一次退回操作的节点是否启用退回后再提交直达本节点
String sql_isreject = " select a.nodeid lastnodeid, a.logtype from workflow_requestlog a, workflow_nownode b where a.requestid = b.requestid and a.destnodeid = b.nownodeid "
	+ " and b.requestid=" + requestid + " and a.destnodeid=" + nodeid + " and a.nodeid != " + nodeid + " order by a.logid desc";
RecordSet.executeSql(sql_isreject);
while(RecordSet.next()) {
	String logtype = Util.null2String(RecordSet.getString("logtype"));
	if("3".equals(logtype)) {
		lastnodeid = Util.null2String(RecordSet.getString("lastnodeid"));
		break;
	}
	if("0".equals(logtype) || "2".equals(logtype) || "e".equals(logtype) || "i".equals(logtype) || "j".equals(logtype)){
		break;
	}
}
if(!"".equals(lastnodeid)&&!WFLinkInfo_nf.isCanSubmitToRejectNode(requestid,currentnodeid,Util.getIntValue(lastnodeid,0))){
	lastnodeid = "";
}
if(!"".equals(lastnodeid)) {
	RecordSet.executeSql("select * from workflow_flownode where workflowid=" + workflowid + " and nodeid=" + lastnodeid);
	if(RecordSet.next()) {
		isSubmitDirectNode = Util.null2String(RecordSet.getString("isSubmitDirectNode"));
	}
	if("1".equals(isSubmitDirectNode)) { // 如果启用，退回到的节点点击【提交/批准】直接到达执行退回的这个节点。此时不再显示【提交至退回节点】操作按钮
		isSubmitDirect = "";
	}
}else {
	isSubmitDirect = "";
}
if("".equals(submitDirectName)) {
	submitDirectName = SystemEnv.getHtmlLabelName(126507, user.getLanguage());
}
//forhandName = HandleForwardName;
if("".equals(HandleForwardName)){
	HandleForwardName = SystemEnv.getHtmlLabelName(23745,user.getLanguage());
	//	forhandName = SystemEnv.getHtmlLabelName(615,user.getLanguage());

}

if("".equals(forhandbackName)){
	if(forhandbackCtrl == 2) {
	forhandbackName = SystemEnv.getHtmlLabelName(23745,user.getLanguage())+"("+SystemEnv.getHtmlLabelName(21761,user.getLanguage())+")";
	}else {
		forhandbackName = SystemEnv.getHtmlLabelName(23745,user.getLanguage());
	}
}


if("".equals(forhandnobackName)){
	if(forhandbackCtrl == 2) {
	forhandnobackName = SystemEnv.getHtmlLabelName(23745,user.getLanguage())+"("+SystemEnv.getHtmlLabelName(21762,user.getLanguage())+")";
	}else {
		forhandnobackName = SystemEnv.getHtmlLabelName(23745,user.getLanguage());
	}
}

if("".equals(givingopinionsName)){
	givingopinionsName = SystemEnv.getHtmlLabelName(18540,user.getLanguage());
}

if("".equals(givingOpinionsnobackName)){
	if(takingOpinionsbackCtrl == 2) {
	givingOpinionsnobackName = SystemEnv.getHtmlLabelName(18540,user.getLanguage())+"("+SystemEnv.getHtmlLabelName(21762,user.getLanguage())+")";
	}else {
		givingOpinionsnobackName = SystemEnv.getHtmlLabelName(18540,user.getLanguage());
	}
}


if("".equals(givingOpinionsbackName)){
	if(takingOpinionsbackCtrl == 2) {
	givingOpinionsbackName = SystemEnv.getHtmlLabelName(18540,user.getLanguage())+"("+SystemEnv.getHtmlLabelName(21761,user.getLanguage())+")";
	}else {
		givingOpinionsbackName = SystemEnv.getHtmlLabelName(18540,user.getLanguage());
	}
}


if(handleforwardid > 0){
	submitname = submitname; // 转办批注
	subnobackName = subnobackName;
	subbackName = subbackName;
}

if(isremark == 1 && takisremark ==2){  //征询意见回复
	submitname = givingopinionsName;
	subnobackName = givingOpinionsnobackName;
	subbackName = givingOpinionsbackName;
}


if(isremark == 1 && takisremark !=2){
	submitname = forsubName;
	subnobackName = forsubnobackName;
	subbackName = forsubbackName;
}
if(isremark == 9||isremark == 7){
	submitname = ccsubName;
	subnobackName = ccsubnobackName;
	subbackName =  ccsubbackName;
}
if("".equals(submitname)){
	if(isremark == 1 || isremark == 9||isremark == 7){
		submitname = SystemEnv.getHtmlLabelName(1006,user.getLanguage()); 
	}else if(nodetype.equals("0") ){
		submitname = SystemEnv.getHtmlLabelName(615,user.getLanguage());      // 创建节点或者转发, 为批注
	}else if(nodetype.equals("1")){
		submitname = SystemEnv.getHtmlLabelName(142,user.getLanguage());  // 审批
	}else if(nodetype.equals("2")){
		submitname = SystemEnv.getHtmlLabelName(725,user.getLanguage());  // 实现
	}
}
if("".equals(subbackName)){
		if( isremark == 1 || isremark == 9||isremark == 7)	{
			if((isremark==1 && ("1".equals(hasforback) && forsubbackCtrl == 2)) || (isremark==9 && ("1".equals(hasccback) && ccsubbackCtrl == 2))){
				subbackName = SystemEnv.getHtmlLabelName(1006,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21761,user.getLanguage())+"）";      // 创建节点或者转发, 为提交
			}else{
				subbackName = SystemEnv.getHtmlLabelName(1006,user.getLanguage());
			}
		}else if(nodetype.equals("0"))	{
			if((nodetype.equals("0") && ("1".equals(hasback) && subbackCtrl == 2))){
				subbackName = SystemEnv.getHtmlLabelName(615,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21761,user.getLanguage())+"）";      // 创建节点或者转发, 为提交
			}else{
				subbackName = SystemEnv.getHtmlLabelName(615,user.getLanguage());
			}
		}else if(nodetype.equals("1")){
			if("1".equals(hasback) && subbackCtrl == 2){
				subbackName = SystemEnv.getHtmlLabelName(142,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21761,user.getLanguage())+"）";  // 审批
			}else{
				subbackName = SystemEnv.getHtmlLabelName(142,user.getLanguage());
			}
		}else if(nodetype.equals("2")){
			if("1".equals(hasback) && subbackCtrl == 2){
				subbackName = SystemEnv.getHtmlLabelName(725,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21761,user.getLanguage())+"）";  // 实现
			}else{
				subbackName = SystemEnv.getHtmlLabelName(725,user.getLanguage());
			}
		}
}
//System.out.println("******isremark="+isremark+"     ***********nodetype="+nodetype +"      "+username+"     *****");
if("".equals(subnobackName)){
	if( isremark == 1 || isremark == 9 ||isremark == 7)	{
		if((isremark==1 && forsubbackCtrl == 2) || (isremark==9 && ccsubbackCtrl == 2)){
		subnobackName = SystemEnv.getHtmlLabelName(1006,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21762,user.getLanguage())+"）";      //批注
		}else {
			subnobackName = SystemEnv.getHtmlLabelName(1006,user.getLanguage());
		}
	}else if(nodetype.equals("0"))	{
		if(subbackCtrl == 2) {
		subnobackName = SystemEnv.getHtmlLabelName(615,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21762,user.getLanguage())+"）";      // 创建节点或者转发, 为提交
		}else {
			subnobackName = SystemEnv.getHtmlLabelName(615,user.getLanguage());
		}
	}else if(nodetype.equals("1")){
		if(subbackCtrl == 2) {
		subnobackName = SystemEnv.getHtmlLabelName(142,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21762,user.getLanguage())+"）";  // 审批
		}else {
			subnobackName = SystemEnv.getHtmlLabelName(142,user.getLanguage());
		}
	}else if(nodetype.equals("2")){
		if(subbackCtrl == 2) {
		subnobackName = SystemEnv.getHtmlLabelName(725,user.getLanguage())+"（"+SystemEnv.getHtmlLabelName(21762,user.getLanguage())+"）";  // 实现
		}else {
			subnobackName = SystemEnv.getHtmlLabelName(725,user.getLanguage());
		}
	}
}
if("".equals(forwardName)){
	forwardName = SystemEnv.getHtmlLabelName(6011,user.getLanguage());
}
if("".equals(takingopinionsName)){
	takingopinionsName = SystemEnv.getHtmlLabelName(82578,user.getLanguage());
}

if("".equals(HandleForwardName)){
	HandleForwardName = SystemEnv.getHtmlLabelName(23745,user.getLanguage());
}
if("".equals(saveName)){
	saveName = SystemEnv.getHtmlLabelName(86,user.getLanguage());
}
if("".equals(rejectName)){
	rejectName = SystemEnv.getHtmlLabelName(236,user.getLanguage());
}
%>

<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%String strBar = "[";//菜单%>
<%if (!fromPDA.equals("1") && !wfmonitor) {%>  <!--pda登录,流程监控不要菜单-->
<%if (isaffirmance.equals("1") && nodetype.equals("0") && !reEdit.equals("1")){//提交确认菜单
if(IsCanSubmit||coadCanSubmit){
RCMenu += "{"+SystemEnv.getHtmlLabelName(16634,user.getLanguage())+",javascript:doSubmit_Pre(this),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(16634,user.getLanguage())+"',iconCls:'btn_draft',handler: function(){doSubmit_Pre(this);}},";
	if(!"1".equals(isSubmitDirectNode)) {
		isSubmitDirect = Util.null2String(request.getParameter("isSubmitDirect"));
	}
}

//topage = URLEncoder.encode(topage);
RCMenu += "{"+SystemEnv.getHtmlLabelName(93,user.getLanguage())+",javascript:doEdit(this),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(93,user.getLanguage())+"',iconCls:'btn_draft',handler: function(){location.href='ViewRequest.jsp?reEdit=1&fromFlowDoc="+fromFlowDoc+"&f_weaver_belongto_userid="+userid+"&f_weaver_belongto_usertype="+usertype+"&requestid="+requestid+"&isovertime="+isovertime+"&topage="+topage+"';}},";
}else{
 if(isremark == 1 || isremark == 9){
    if("".equals(ifchangstatus)){
		RCMenu += "{"+submitname+",javascript:doRemark_nNoBack(),_self}" ;
		RCMenuHeight += RCMenuHeightStep ;
		strBar += "{text: '"+submitname+"',iconCls:'btn_submit',handler: function(){doRemark_nNoBack();}},";
	}else{
		if(takisremark == 2){
		if(!"1".equals(hastakingOpinionsback)&&!"1".equals(hastakingOpinionsnoback)&&isremark==1 && takisremark ==2){
			RCMenu += "{"+submitname+",javascript:doRemark_nBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+submitname+"',iconCls:'btn_subbackName',handler: function(){doRemark_nBack(this);}},";
		}
		}else if((!"1".equals(hasforback)&&!"1".equals(hasfornoback)&&isremark==1) || (!"1".equals(hasccback)&&!"1".equals(hasccnoback)&&isremark==9)){
			RCMenu += "{"+submitname+",javascript:doRemark_nBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+submitname+"',iconCls:'btn_subbackName',handler: function(){doRemark_nBack(this);}},";
		}
		  if(takisremark == 2){
		if(("1".equals(hastakingOpinionsback)&&isremark==1 && takisremark ==2)){
				RCMenu += "{"+subbackName+",javascript:doRemark_nBack(this),_self}";
				RCMenuHeight += RCMenuHeightStep;
				strBar += "{text: '"+subbackName+"',iconCls:'btn_subbackName',handler: function(){doRemark_nBack(this);}},";
			}
		if(("1".equals(hastakingOpinionsnoback)&&isremark==1 && takisremark ==2)){
			RCMenu += "{"+subnobackName+",javascript:doRemark_nNoBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+subnobackName+"',iconCls:'btn_subnobackName ',handler: function(){doRemark_nNoBack(this);}},";
			}
		  }else{
		  	if(("1".equals(hasforback)&&isremark==1) || ("1".equals(hasccback)&&isremark==9)){
				RCMenu += "{"+subbackName+",javascript:doRemark_nBack(this),_self}";
				RCMenuHeight += RCMenuHeightStep;
				strBar += "{text: '"+subbackName+"',iconCls:'btn_subbackName',handler: function(){doRemark_nBack(this);}},";
			}
		if(("1".equals(hasfornoback)&&isremark==1) || ("1".equals(hasccnoback)&&isremark==9)){
			RCMenu += "{"+subnobackName+",javascript:doRemark_nNoBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+subnobackName+"',iconCls:'btn_subnobackName ',handler: function(){doRemark_nNoBack(this);}},";
			}
		  }
		
		}
	
	  if(isremark==1&&IsCanModify){
        RCMenu += "{"+saveName+",javascript:doSave_nNew(this),_self}" ;
        RCMenuHeight += RCMenuHeightStep ;
        strBar += "{text: '"+saveName+"',iconCls:'btn_wfSave',handler: function(){doSave_nNew(this);}},";
    }
    if(((isremark==1&&canForwd)||(isremark==9&&IsPendingForward.equals("1")))&&!"1".equals(session.getAttribute("istest"))){
        RCMenu += "{"+forwardName+",javascript:doReview(),_self}" ;//add by mackjoe
        RCMenuHeight += RCMenuHeightStep ;
        strBar += "{text: '"+forwardName+"',iconCls:'btn_forward',handler: function(){doReview();}},";
    }
}else if(isremark == 5){
    if("".equals(ifchangstatus)){
		RCMenu += "{"+submitname+",javascript:doSubmitNoBack(this),_self}" ;
		RCMenuHeight += RCMenuHeightStep ;
		strBar += "{text: '"+submitname+"',iconCls:'btn_submit',handler: function(){doSubmitNoBack(this);}},";
	}else{
		if(!"1".equals(hasnoback)){
			RCMenu += "{"+subbackName+",javascript:doSubmitBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+subbackName+"',iconCls:'btn_subbackName',handler: function(){doSubmitBack(this);}},";
		}else{
			if("1".equals(hasback)){
				RCMenu += "{"+subbackName+",javascript:doSubmitBack(this),_self}";
				RCMenuHeight += RCMenuHeightStep;
				strBar += "{text: '"+subbackName+"',iconCls:'btn_subbackName',handler: function(){doSubmitBack(this);}},";
			}
			RCMenu += "{"+subnobackName+",javascript:doSubmitNoBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+subnobackName+"',iconCls:'btn_subnobackName',handler: function(){doSubmitNoBack(this);}},";
		}
	}
    if("1".equals(isSubmitDirect)) {
		RCMenu += "{" + submitDirectName + ", javascript:doSubmitDirect(this, \\\"Submit\\\"), _self}";
		RCMenuHeight += RCMenuHeightStep;
		strBar += "{text: '" + submitDirectName + "', iconCls: 'btn_submit', handler: function(){doSubmitDirect(this, \\\"Submit\\\");}},";
	}
}else { 
	String subfun = "Submit";
    if(IsCanSubmit||coadCanSubmit){
	if (isaffirmance.equals("1") && nodetype.equals("0") && reEdit.equals("1")){
		subfun = "Affirmance";
	}
	if("".equals(ifchangstatus)){
		RCMenu += "{"+submitname+",javascript:do"+subfun+"NoBack(this),_self}" ;
		RCMenuHeight += RCMenuHeightStep ;
		strBar += "{text: '"+submitname+"',iconCls:'btn_submit',handler: function(){do"+subfun+"NoBack(this);}},";
	}else{
		if((!"1".equals(hasnoback)&&!"1".equals(hasback))){
			RCMenu += "{"+submitname+",javascript:do"+subfun+"Back(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+submitname+"',iconCls:'btn_subbackName',handler: function(){do"+subfun+"Back(this);}},";
		}else{
			if("1".equals(hasback)){
				RCMenu += "{"+subbackName+",javascript:do"+subfun+"Back(this),_self}";
				RCMenuHeight += RCMenuHeightStep;
				strBar += "{text: '"+subbackName+"',iconCls:'btn_subbackName',handler: function(){do"+subfun+"Back(this);}},";
			}
			if("1".equals(hasnoback)){
			RCMenu += "{"+subnobackName+",javascript:do"+subfun+"NoBack(this),_self}";
			RCMenuHeight += RCMenuHeightStep;
			strBar += "{text: '"+subnobackName+"',iconCls:'btn_subnobackName',handler: function(){do"+subfun+"NoBack(this);}},";
		}
		}
	}
	if("1".equals(isSubmitDirect)) {
		RCMenu += "{" + submitDirectName + ", javascript:doSubmitDirect(this, \\\"" + subfun + "\\\"), _self}";
		RCMenuHeight += RCMenuHeightStep;
		strBar += "{text: '" + submitDirectName + "', iconCls: 'btn_submit', handler: function(){doSubmitDirect(this, \\\"" + subfun + "\\\");}},";
	}
    if(IsFreeWorkflow){
    	if(iscnodefree.equals("0")){
	        RCMenu += "{"+FreeWorkflowname+",javascript:doFreeWorkflow(),_self}" ;
	        RCMenuHeight += RCMenuHeightStep ;
    	}
        strBar += "{text: '"+FreeWorkflowname+"',iconCls:'btn_edit',handler: function(){doFreeWorkflow(this);}},";
    }
    	 if (isImportDetail) {
            RCMenu += "{" + SystemEnv.getHtmlLabelName(26255, user.getLanguage()) + ",javascript:doImportDetail(),_self}";
            RCMenuHeight += RCMenuHeightStep;
        }
    RCMenu += "{"+saveName+",javascript:doSave_nNew(this),_self}" ;
    RCMenuHeight += RCMenuHeightStep ;
    strBar += "{text: '"+saveName+"',iconCls:'btn_wfSave',handler: function(){doSave_nNew(this);}},";
    if( isreject.equals("1") ){

	    if(isremark==7 && coadsigntype.equals("2")){
	    	
	    }else{
		    RCMenu += "{"+rejectName+",javascript:doReject_New(),_self}" ;
		    RCMenuHeight += RCMenuHeightStep ;
		    strBar += "{text: '"+rejectName+"',iconCls:'btn_rejectName',handler: function(){doReject_New();}},";
	    }
      }
    }
if(IsPendingForward.equals("1")&&!"1".equals(session.getAttribute("istest"))){   //转发
RCMenu += "{"+forwardName+",javascript:doReview(),_self}" ;//Modified by xwj for td3247 20051201
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+forwardName+"',iconCls:'btn_forward',handler: function(){doReview();}},";
//rmklag = 1;
}
//System.out.println("-I-1188----IsTakingOpinions-----"+IsTakingOpinions);
if(IsTakingOpinions.equals("1")&&!"1".equals(session.getAttribute("istest"))){  //征求意见
RCMenu += "{"+takingopinionsName+",javascript:doReview2(),_self}" ;//Modified by xwj for td3247 20051201
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+takingopinionsName+"',iconCls:'btn_forward',handler: function(){doReview2();}},";
//rmklag = 2;
}
//System.out.println("-I-1194----IsHandleForward-----"+IsHandleForward);
if(IsHandleForward.equals("1")){   //转办
	if("".equals(ifchangstatus)&&!"1".equals(session.getAttribute("istest"))){
		RCMenu += "{"+HandleForwardName+",javascript:doReview3(),_self}" ;//Modified by xwj for td3247 20051201
		RCMenuHeight += RCMenuHeightStep ;
		strBar += "{text: '"+HandleForwardName+"',iconCls:'btn_forward',handler: function(){doReview3();}},";
	}else{
		if((!"1".equals(hasforhandnoback)&&!"1".equals(hasforhandback)&&!"1".equals(session.getAttribute("istest")))){
			RCMenu += "{"+HandleForwardName+",javascript:doReview3(),_self}" ;//Modified by xwj for td3247 20051201
			RCMenuHeight += RCMenuHeightStep ;
			strBar += "{text: '"+HandleForwardName+"',iconCls:'btn_forward',handler: function(){doReview3();}},";
		}else{
			if( "1".equals(hasforhandback)&&!"1".equals(session.getAttribute("istest"))){
				RCMenu += "{"+forhandbackName+",javascript:doReviewback3(),_self}" ;//Modified by xwj for td3247 20051201
			RCMenuHeight += RCMenuHeightStep ;
			strBar += "{text: '"+forhandbackName+"',iconCls:'btn_forwardback3',handler: function(){doReviewback3();}},";
				//strBar += "{text: '"+forhandbackName+"',iconCls:'btn_forward',handler: function(){bodyiframe.doReview3();}},";  //转发
			}
			if( "1".equals(hasforhandnoback)){
				RCMenu += "{"+forhandnobackName+",javascript:doReviewnoback3(),_self}" ;//Modified by xwj for td3247 20051201
			RCMenuHeight += RCMenuHeightStep ;
			strBar += "{text: '"+forhandnobackName+"',iconCls:'btn_forwardnobacke3',handler: function(){doReviewnoback3();}},";
		//strBar += "{text: '"+forhandnobackName+"',iconCls:'btn_forward',handler: function(){bodyiframe.doReview3();}},";  //转发
			}
		}
	}
//rmklag = 3;
}
  if(isreopen.equals("1") && false ){

RCMenu += "{"+SystemEnv.getHtmlLabelName(244,user.getLanguage())+",javascript:doReopen(),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(244,user.getLanguage())+"',iconCls:'btn_doReopen',handler: function(){doReopen();}},";
  }

}
%>
<%
/*added by cyril on 2008-07-09 for TD:8835*/
if(isModifyLog.equals("1")) {
	RCMenu += "{"+SystemEnv.getHtmlLabelName(21625,user.getLanguage())+",javascript:doViewModifyLog(),_self}" ;
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+SystemEnv.getHtmlLabelName(21625,user.getLanguage())+"',iconCls:'btn_doViewModifyLog',handler: function(){doViewModifyLog();}},";
}
/*end added by cyril on 2008-07-09 for TD:8835*/
/*TD9145 START*/
for(int i = 0; i < newMenuList0.size(); i++) {
	HashMap newMenuMap = (HashMap) newMenuList0.get(i);
	int menuid = (Integer) newMenuMap.get("id");
	if(menuid > 0) {
		newWFName = (String) newMenuMap.get("newName");
		t_workflowid = (Integer) newMenuMap.get("workflowid");
	if("".equals(newWFName)){
		newWFName = SystemEnv.getHtmlLabelName(1239,user.getLanguage()) + (i + 1);
	}
	RequestCheckUser.resetParameter();
	RequestCheckUser.setUserid(userid);
	RequestCheckUser.setWorkflowid(t_workflowid);
	RequestCheckUser.setLogintype(logintype);
	RequestCheckUser.checkUser();
	int  t_hasright=RequestCheckUser.getHasright();
	if(t_hasright == 1){
		RCMenu += "{"+newWFName+",javascript:onNewRequest("+t_workflowid+", "+requestid+",0),_top} " ;
		RCMenuHeight += RCMenuHeightStep ;
		strBar += "{text: '"+newWFName+"',iconCls:'btn_newWFName',handler: function(){onNewRequest("+t_workflowid+", "+requestid+",0);}},";
	}
	}
}
RTXConfig rtxconfig = new RTXConfig();
String temV = rtxconfig.getPorp(rtxconfig.CUR_SMS_SERVER_IS_VALID);
boolean valid = false;
if (temV != null && temV.equalsIgnoreCase("true")) {
	valid = true;
} else {
	valid = false;
}
if(valid == true && HrmUserVarify.checkUserRight("CreateSMS:View", user)){
	for(int i = 0; i < newMenuList1.size(); i++) {
		HashMap newMenuMap = (HashMap) newMenuList1.get(i);
		int menuid = (Integer) newMenuMap.get("id");
		if(menuid > 0) {
			newSMSName = (String) newMenuMap.get("newName");
	if("".equals(newSMSName)){
		newSMSName = SystemEnv.getHtmlLabelName(16444,user.getLanguage()) + (i + 1);
	}
	RCMenu += "{"+newSMSName+",javascript:onNewSms("+workflowid+", "+nodeid+", "+requestid+", " + menuid + "),_top} " ;
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+newSMSName+"',iconCls:'btn_newSMSName',handler: function(){onNewSms("+workflowid+", "+nodeid+", "+requestid+", " + menuid + ");}},";
		}
	}
}
//微信提醒(QC:98106)
if(WechatPropConfig.isUseWechat()){
	for(int i = 0; i < newMenuList2.size(); i++) {
		HashMap newMenuMap = (HashMap) newMenuList2.get(i);
		int menuid = (Integer) newMenuMap.get("id");
		if(menuid > 0) {
			newCHATSName = (String) newMenuMap.get("newName");
	if("".equals(newCHATSName)){
		newCHATSName = SystemEnv.getHtmlLabelName(32818,user.getLanguage()) + (i + 1);
	}
	RCMenu += "{"+newCHATSName+",javascript:onNewChats("+workflowid+", "+nodeid+", "+requestid+", " + menuid + "),_top} " ;
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+newCHATSName+"',iconCls:'btn_newSMSName',handler: function(){onNewChats("+workflowid+", "+nodeid+", "+requestid+", " + menuid + ");}},";
		}
	}
}
/*TD9145 END*/
if("1".equals(hasovertime)&&isremark==0){
	if("".equals(newOverTimeName)){
		newOverTimeName = SystemEnv.getHtmlLabelName(18818,user.getLanguage());
	}
	RCMenu += "{"+newOverTimeName+",javascript:onNewOverTime(),_top} " ;
	RCMenuHeight += RCMenuHeightStep ;
    strBar += "{text: '"+newOverTimeName+"',iconCls:'btn_newSMSName',handler: function(){onNewOverTime();}},";
}
String isTriDiffWorkflow=null; 
RecordSet.executeSql("select a.isTriDiffWorkflow, b.* from workflow_base a, workflow_flownode b where a.id = b.workflowid and a.id="+workflowid + " and b.nodeid=" + nodeid);

if(RecordSet.next()){
	isTriDiffWorkflow=Util.null2String(RecordSet.getString("isTriDiffWorkflow"));
    isrejectremind=Util.null2String(RecordSet.getString("isrejectremind"));
    ischangrejectnode=Util.null2String(RecordSet.getString("ischangrejectnode"));
	  isselectrejectnode=Util.null2String(RecordSet.getString("isselectrejectnode"));

} 

if (isremark != 1 && isremark != 8 && isremark != 9) {
	List<Map<String, String>> buttons = SubWorkflowTriggerService.getManualTriggerButtons(workflowid, nodeid);
	for(int indexId = 0; indexId < buttons.size() ; indexId++){
		Map<String, String> button = buttons.get(indexId);
		int subwfSetId = Util.getIntValue(button.get("subwfSetId"),0);
		int buttonNameId = Util.getIntValue(button.get("buttonNameId"),0);
		String triSubwfName7 = Util.null2String(button.get("triSubwfName7"));
		String triSubwfName8 = Util.null2String(button.get("triSubwfName8"));
		String workflowNames = Util.null2String(button.get("workflowNames"));
		String triSubwfName = "";
		if(user.getLanguage() == 8){
			triSubwfName = triSubwfName8;
		}else{
			triSubwfName = triSubwfName7;
		}
		if(triSubwfName.equals("")){
			triSubwfName = SystemEnv.getHtmlLabelName(22064,user.getLanguage())+(indexId+1);
		}
		RCMenu += "{"+triSubwfName+",javascript:triSubwf2("+subwfSetId+",\\\""+workflowNames+"\\\"),_top} " ;
		RCMenuHeight += RCMenuHeightStep;
		strBar += "{text: '"+triSubwfName+"',iconCls:'btn_relateCwork',handler: function(){triSubwf2("+subwfSetId+",\\\""+workflowNames+"\\\");}},";
	}
}

if(!isfromtab&&!"1".equals(session.getAttribute("istest"))){
RCMenu += "{"+SystemEnv.getHtmlLabelName(1290,user.getLanguage())+",javascript:doBack(),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(1290,user.getLanguage())+"',iconCls:'btn_back',handler: function(){doBack();}},";
}
%>

<%--xwj for td3665 20060224 begin--%>
<%
HashMap map = WfFunctionManageUtil.wfFunctionManageByNodeid(workflowid,nodeid);
String ov = (String)map.get("ov");
String rb = (String)map.get("rb");
haveOverright=((isremark != 1&&isremark != 9&&isremark != 5) || (isremark == 7 && !"2".equals(coadsigntype))) && "1".equals(ov) && WfForceOver.isNodeOperator(requestid,userid) && !currentnodetype.equals("3");
//haveBackright=isremark != 1&&isremark != 9&&isremark != 7&&isremark != 5 && !"0".equals(rb) && WfForceDrawBack.isHavePurview(requestid,userid,Integer.parseInt(logintype),-1,-1) && !currentnodetype.equals("0");
haveBackright=WfForceDrawBack.checkOperatorIsremark(requestid, userid, usertype, isremark) && !"0".equals(rb) && WfForceDrawBack.isHavePurview(requestid,userid,Integer.parseInt(logintype),-1,-1) && !currentnodetype.equals("0");
%>
<%if(haveOverright){
RCMenu += "{"+SystemEnv.getHtmlLabelName(18360,user.getLanguage())+",javascript:doDrawBack(this),_self}" ;//xwj for td3665 20060224
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(18360,user.getLanguage())+"',iconCls:'btn_doDrawBack',handler: function(){doDrawBack(this);}},";
}
if(haveBackright){
RCMenu += "{"+SystemEnv.getHtmlLabelName(18359,user.getLanguage())+",javascript:doRetract(this),_self}" ;//xwj for td3665 20060224
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(18359,user.getLanguage())+"',iconCls:'btn_doRetract',handler: function(){doRetract(this);}},";
}
if(haveStopright)
{
	RCMenu += "{"+SystemEnv.getHtmlLabelName(20387,user.getLanguage())+",javascript:doStop(this),_self}" ;//xwj for td3665 20060224
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+SystemEnv.getHtmlLabelName(20387,user.getLanguage())+"',iconCls:'btn_end',handler: function(){bodyiframe.doStop(this);}},";
}
//add by jianyong.tang 2018/4/9 start
String canshowdelbutton="0";
if(requestid !=0 && workflowid == 741 ){
	String tableName_del = "";
	String fwzh = "";
	String sql_del = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql_del);
		if (rs.next()) {
			tableName_del = Util.null2String(rs.getString("tablename"));
		}
		if(!"".equals(tableName_del)){
			sql_del="select * from "+tableName_del+" where requestid="+requestid;
			rs.executeSql(sql_del);
			if(rs.next()){
				fwzh = Util.null2String(rs.getString("fwzh"));	
			}
			if(!"".equals(fwzh)){
				canshowdelbutton = "1";
			
			}
		}
		
}
//add by jianyong.tang 2018/4/9 end

if(haveCancelright)
{
	RCMenu += "{"+SystemEnv.getHtmlLabelName(16210,user.getLanguage())+",javascript:doCancel(this),_self}" ;
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+SystemEnv.getHtmlLabelName(16210,user.getLanguage())+"',iconCls:'btn_backSubscrible',handler: function(){bodyiframe.doCancel(this);}},";
}
if(haveRestartright)
{
	RCMenu += "{"+SystemEnv.getHtmlLabelName(18095,user.getLanguage())+",javascript:doRestart(this),_self}" ;
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+SystemEnv.getHtmlLabelName(18095,user.getLanguage())+"',iconCls:'btn_next',handler: function(){bodyiframe.doRestart(this);}},";
}

if(nodetype.equals("0")&&isremark != 1&&isremark != 9&&isremark != 7&&isremark != 5&&WfFunctionManageUtil.IsShowDelButtonByReject(requestid,workflowid) && !"1".equals(session.getAttribute("istest")) && "0".equals(canshowdelbutton)){    // 创建节点(退回创建节点也是)
RCMenu += "{"+SystemEnv.getHtmlLabelName(91,user.getLanguage())+",javascript:doDelete(),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(91,user.getLanguage())+"',iconCls:'btn_doDelete',handler: function(){doDelete();}},";
}
}
RCMenu += "{"+SystemEnv.getHtmlLabelName(257,user.getLanguage())+",javascript:openSignPrint(),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(257,user.getLanguage())+"',iconCls:'btn_print',handler: function(){openSignPrint();}},";
}


RCMenu += "{"+SystemEnv.getHtmlLabelName(21533,user.getLanguage())+",javascript:doPrintViewLog(),_self}" ;
RCMenuHeight += RCMenuHeightStep ;
strBar += "{text: '"+SystemEnv.getHtmlLabelName(21533,user.getLanguage())+"',iconCls:'btn_collect',handler: function(){doPrintViewLog();}},";

//分享
if(iscanshare){
	RCMenu += "{"+SystemEnv.getHtmlLabelName(126091,user.getLanguage())+",javascript:doShare(),_self}" ;
	RCMenuHeight += RCMenuHeightStep ;
	strBar += "{text: '"+SystemEnv.getHtmlLabelName(126091,user.getLanguage())+"',iconCls:'btn_collect',handler: function(){doShare();}},";
}

/** 任务相关菜单 QC129917 **/
if(weaver.workrelate.util.TransUtil.istask()){
	RCMenu += "{"+SystemEnv.getHtmlLabelName(15266, user.getLanguage())+",javascript:doCreateTask(\\\"requestids="+requestid+"\\\"),_top} " ;
	RCMenuHeight += RCMenuHeightStep;
	
	RCMenu += "{"+SystemEnv.getHtmlLabelName(124912, user.getLanguage())+",javascript:openTaskList(\\\"requestids="+requestid+"\\\"),_top} " ;
	RCMenuHeight += RCMenuHeightStep ;
%>
<script language="javascript" src="/workrelate/js/relateoperate_wev8.js"></script>
<%
}

if(strBar.lastIndexOf(",")>-1) strBar = strBar.substring(0,strBar.lastIndexOf(","));
strBar+="]";
%>
<%--xwj for td3665 20060224 end--%>

<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<script language="javascript">
enableAllmenu();
</script>
        <input type=hidden name=seeflowdoc value="<%=seeflowdoc%>">
		<input type=hidden name=isworkflowdoc value="<%=isworkflowdoc%>">
        <input type=hidden name=wfdoc value="<%=wfdoc%>">
        <input type=hidden name=picInnerFrameurl value="/workflow/request/WorkflowRequestPictureInner.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&fromFlowDoc=<%=fromFlowDoc%>&modeid=<%=modeid%>&requestid=<%=requestid%>&workflowid=<%=workflowid%>&nodeid=<%=nodeid%>&isbill=<%=isbill%>&formid=<%=formid%>">

		<TABLE width="100%">
		<tr>
		<td valign="top">

    <table width="100%" style="background:#fff;border-spacing:0px">
        <tr><td width="80%" align=left>
      		<jsp:include page="/workflow/request/wfSubmitErrorMsg.jsp">
       			<jsp:param name="message" value="<%=message %>" />
       			<jsp:param name="requestid" value="<%=requestid %>" />
       		</jsp:include>
        </td>
        </tr>
    </table>

<%
String managepage= "";
String operationpage = "" ;
String hasfileup="";
if(isbill.equals("1")){
    RecordSet.executeProc("bill_includepages_SelectByID",formid+"");
    if(RecordSet.next()) {
        managepage = Util.null2String(RecordSet.getString("managepage"));
        operationpage = Util.null2String(RecordSet.getString("operationpage"));
        hasfileup=Util.null2String(RecordSet.getString("hasfileup"));
    }
}

//---------------------------------------------------------------------------------
//跨浏览器添加-当前浏览器是非IE，表单为单据，并且是未修改的单据，则跳转至公共页面 START
//---------------------------------------------------------------------------------
if (!isIE.equalsIgnoreCase("true") && false) {
	if (isbill.equals("1") && formid > 0 && !managepage.equals("")) {
		if (!"159".equals(formid + "") && !"180".equals(formid + "") && !"85".equals(formid + "") &&!"7".equals(formid + "") && !"79".equals(formid + "") && !"158".equals(formid + "") && !"157".equals(formid + "") && !"156".equals(formid + "") ) {
			//response.sendRedirect("/wui/common/page/sysRemind.jsp?labelid=15590");
%>

<script type="text/javascript">

window.parent.location.href = "/wui/common/page/sysRemind.jsp?f_weaver_belongto_userid=<%=userid%>&f_weaver_belongto_usertype=<%=usertype%>&labelid=27826";

</script>

<%
			return;
		}
	}
}
//---------------------------------------------------------------------------------
//跨浏览器添加-当前浏览器是非IE，表单为单据，并且是未修改的单据，则跳转至公共页面 END
//---------------------------------------------------------------------------------


if( !managepage.equals("") && (formid != 7 || !"2".equals(ismode))) {//排除费用报销单  QC29615
%>
<jsp:include page="<%=managepage%>" flush="true">
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="requestlevel" value="<%=requestlevel%>" />

    <jsp:param name="creater" value="<%=creater%>" />
    <jsp:param name="creatertype" value="<%=creatertype%>" />
    <jsp:param name="deleted" value="<%=deleted%>" />
    <jsp:param name="billid" value="<%=billid%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="isreopen" value="<%=isreopen%>" />
    <jsp:param name="isreject" value="<%=isreject%>" />
    <jsp:param name="fromFlowDoc" value="<%=fromFlowDoc%>" />
    <jsp:param name="isremark" value="<%=isremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="docfileid" value="<%=docfileid%>" />
    <jsp:param name="newdocid" value="<%=newdocid%>" />

	<jsp:param name="languageid" value="<%=user.getLanguage()%>" />
   
    <jsp:param name="wfmonitor" value="<%=wfmonitor%>" />
</jsp:include>
<%}else{
      //modify by xhheng @20050315 for 附件上传
      if( operationpage.equals("") ){
        operationpage = "RequestOperation.jsp" ;
        %>
        <form name="frmmain" method="post" action="<%=operationpage%>">
	    <%if (fromPDA.equals("1")) {%>
		<% if(isremark == 1){ %>
		<a href="javascript:document.frmmain.isremark.value='1';document.frmmain.src.value='save';document.frmmain.submit();"><%=submitname%></a>
		<%} else if(isremark == 5) {%>
        <a href="javascript:document.frmmain.src.value='submit';document.frmmain.submit();"><%=SystemEnv.getHtmlLabelName(615,user.getLanguage())%></a>
		<%} else {%>
		<a href="javascript:document.frmmain.src.value='submit';document.frmmain.submit();"><%=submitname%></a>
		<%} 
		 if (isreject.equals("1")) {%>
	   <a href="javascript:document.frmmain.src.value='reject';document.frmmain.submit();" ><%=SystemEnv.getHtmlLabelName(236,user.getLanguage())%></a>
		<%}%>
		<a href="javascript:document.frmmain.src.value='save';document.frmmain.submit();"><%=SystemEnv.getHtmlLabelName(86,user.getLanguage())%></a> 
		<a href="javascript:location.href='/workflow/search/WFSearchResultPDA.jsp'"><%=SystemEnv.getHtmlLabelName(1290,user.getLanguage())%></a>
	    <%}%>
        <%
      }else{
        if(hasfileup.equals("1")){
          %>
            <form name="frmmain" method="post"  action="<%=operationpage%>" >
          <%
		  }else{%>
          <form name="frmmain" method="post" action="<%=operationpage%>">
          <%
            }
       }
			session.setAttribute(userid+"_"+workflowid+"workflowname", workflowname);
			if("2".equals(ismode) && modeid>0){
				//被转发人可以修改表单内容
				int forwardisremark = isremark;
				String reEditbody=Util.null2String((String)session.getAttribute(user.getUID()+"_"+requestid+"reEdit"));
				if(IsCanModify&&reEditbody.equals("1")&&forwardisremark==1){
					forwardisremark=0;
				}				
%>

<%if(isbill.equals("1") && formid==7){//针对费用报销增加财务信息  QC29615%>
    <div id="t_headother"></div>
    <jsp:include page="/workflow/request/BillBudgetExpenseDetailMode.jsp" flush="true">
		<jsp:param name="billid" value="<%=billid%>" />
    <jsp:param name="creater" value="<%=creater%>" />
    </jsp:include>
<%}%>

<jsp:include page="WorkflowManageRequestHtml.jsp" flush="true">
	<jsp:param name="modeid" value="<%=modeid%>" />
	<jsp:param name="userid" value="<%=userid%>" />
    <jsp:param name="languageid" value="<%=user.getLanguage()%>" />
    <jsp:param name="fromFlowDoc" value="<%=fromFlowDoc%>" />
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="requestlevel" value="<%=requestlevel%>" />
    <jsp:param name="creater" value="<%=creater%>" />
    <jsp:param name="creatertype" value="<%=creatertype%>" />
    <jsp:param name="deleted" value="<%=deleted%>" />
    <jsp:param name="billid" value="<%=billid%>" />
	<jsp:param name="isbill" value="<%=isbill%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="isreopen" value="<%=isreopen%>" />
    <jsp:param name="isreject" value="<%=isreject%>" />
    <jsp:param name="isremark" value="<%=forwardisremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="currenttime" value="<%=currenttime%>" />
    <jsp:param name="needcheck" value="<%=needcheck%>" />
	<jsp:param name="topage" value="<%=topage%>" />
	<jsp:param name="desrequestid" value="<%=desrequestid%>" />
	<jsp:param name="newenddate" value="<%=newenddate%>" />
	<jsp:param name="newfromdate" value="<%=newfromdate%>" />
	<jsp:param name="docfileid" value="<%=docfileid%>" />
	<jsp:param name="newdocid" value="<%=newdocid%>" />
    <jsp:param name="IsCanModify" value="<%=IsCanModify%>" />
</jsp:include>
<%
			}else{
	   %>
        <%--@ include file="WorkflowManageRequestBody.jsp" --%>
<jsp:include page="WorkflowManageRequestBodyAction.jsp" flush="true">
	<jsp:param name="userid" value="<%=userid%>" />
    <jsp:param name="languageid" value="<%=user.getLanguage()%>" />
    <jsp:param name="fromFlowDoc" value="<%=fromFlowDoc%>" />
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="requestlevel" value="<%=requestlevel%>" />
    <jsp:param name="creater" value="<%=creater%>" />
    <jsp:param name="creatertype" value="<%=creatertype%>" />
    <jsp:param name="deleted" value="<%=deleted%>" />
    <jsp:param name="billid" value="<%=billid%>" />
	<jsp:param name="isbill" value="<%=isbill%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="isreopen" value="<%=isreopen%>" />
    <jsp:param name="isreject" value="<%=isreject%>" />
    <jsp:param name="isremark" value="<%=isremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="currenttime" value="<%=currenttime%>" />
    <jsp:param name="needcheck" value="<%=needcheck%>" />
	<jsp:param name="topage" value="<%=topage%>" />
	<jsp:param name="newenddate" value="<%=newenddate%>" />
	<jsp:param name="newfromdate" value="<%=newfromdate%>" />
	<jsp:param name="docfileid" value="<%=docfileid%>" />
	<jsp:param name="newdocid" value="<%=newdocid%>" />
</jsp:include>

<%
//add by mackjoe at 2006-06-07 td4491 有明细时才加载
boolean  hasdetailb=false;
if(isbill.equals("0")) {
    RecordSet.executeSql("select count(*) from workflow_formfield  where isdetail='1' and formid="+formid);
}else{
    RecordSet.executeSql("select count(*) from workflow_billfield  where viewtype=1 and billid="+formid);
}
if(RecordSet.next()){
    if(RecordSet.getInt(1)>0) hasdetailb=true;
}
if(hasdetailb){
	if(isbill.equals("0")){
%>
    <%--@ include file="WorkflowManageRequestDetailBody.jsp" --%>
    <jsp:include page="WorkflowManageRequestDetailBody.jsp" flush="true">
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="creater" value="<%=creater%>" />
    <jsp:param name="creatertype" value="<%=creatertype%>" />
    <jsp:param name="billid" value="<%=billid%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="newdocid" value="<%=newdocid%>" />
    <jsp:param name="isremark" value="<%=isremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="currenttime" value="<%=currenttime%>" />
	<jsp:param name="docfileid" value="<%=docfileid%>" />
    <jsp:param name="needcheck" value="<%=needcheck%>" />
    <jsp:param name="isaffirmance" value="<%=isaffirmance%>" />
    <jsp:param name="reEdit" value="<%=reEdit%>" />
	<jsp:param name="isbill" value="<%=isbill%>" />
    </jsp:include>
<%
	}else{
%>
    <jsp:include page="WorkflowManageRequestDetailBodyBill.jsp" flush="true">
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="billid" value="<%=billid%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="newdocid" value="<%=newdocid%>" />
    <jsp:param name="isremark" value="<%=isremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="currenttime" value="<%=currenttime%>" />
	<jsp:param name="docfileid" value="<%=docfileid%>" />
    <jsp:param name="needcheck" value="<%=needcheck%>" />
    <jsp:param name="isaffirmance" value="<%=isaffirmance%>" />
    <jsp:param name="reEdit" value="<%=reEdit%>" />
	<jsp:param name="isbill" value="<%=isbill%>" />
    </jsp:include>
<%
	}
    }
}
%>
	
	<%
		int submit=Util.getIntValue(Util.null2String(request.getParameter("submit")), 0);
		int forward=Util.getIntValue(Util.null2String(request.getParameter("forward")), 0);
	//	System.out.println("num:1|WorkflowManageSign1.jsp");
	%>
    <%--@ include file="WorkflowManageSign.jsp" --%>
    <jsp:include page="WorkflowManageSign1.jsp" flush="true">
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="requestlevel" value="<%=requestlevel%>" />
    <jsp:param name="creater" value="<%=creater%>" />
    <jsp:param name="creatertype" value="<%=creatertype%>" />
    <jsp:param name="deleted" value="<%=deleted%>" />
    <jsp:param name="billid" value="<%=billid%>" />
	<jsp:param name="isbill" value="<%=isbill%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="isreopen" value="<%=isreopen%>" />
    <jsp:param name="isreject" value="<%=isreject%>" />
    <jsp:param name="isremark" value="<%=isremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="currenttime" value="<%=currenttime%>" />
    <jsp:param name="needcheck" value="<%=needcheck%>" />
    <jsp:param name="isOldWf" value="<%=isOldWf%>" />
    <jsp:param name="topage" value="<%=topage%>" />
    <jsp:param name="isworkflowdoc" value="<%=isworkflowdoc%>" />
	<jsp:param name="ismode" value="<%=ismode%>" />
	<jsp:param name="submit" value="<%=submit%>" />
	<jsp:param name="forward" value="<%=forward%>" />
	<jsp:param name="f_weaver_belongto_userid" value="<%=userid%>" />
	<jsp:param name="f_weaver_belongto_usertype" value="<%=usertype%>" />
    </jsp:include>
    <input type="hidden" name="isovertime" value="<%=isovertime%>">
	<input type="hidden" name="needwfback"  id="needwfback" value="1"/>

	<input type="hidden" name="lastOperator"  id="lastOperator" value="<%=lastOperator%>"/>
	<input type="hidden" name="lastOperateDate"  id="lastOperateDate" value="<%=lastOperateDate%>"/>
	<input type="hidden" name="lastOperateTime"  id="lastOperateTime" value="<%=lastOperateTime%>"/>
    <%
    	//当流程为自由流程时，加载自由流程节点的信息，
    	//并在页面上创建div隐藏节点，用于存放自由节点设置信息
    	if( isFree.equals("1") ){
    %>
    	 <!-- div freeNode 为自由流程设置信息-->
	    <div class="freeNode" style="display:none;">
	            <%
	
	                RecordSet.executeSql("select * from("+
	                        "select base.id,node.workflowid,base.nodename,node.nodetype,node.isFormSignature,node.IsPendingForward,"+
	                        "base.operators,base.Signtype,base.floworder from workflow_flownode node,workflow_nodebase base where node.nodeid=base.id ) a "+
	                        "left join ("+
	                        "select rights.nodeid,manage.retract,manage.pigeonhole,rights.isroutedit,rights.istableedit from "+
	                        "workflow_function_manage manage full join workflow_freeright rights on manage.operatortype=rights.nodeid"+
	                        ") b on a.id=b.nodeid where a.workflowid="+workflowid+" and a.id="+nodeid);
	
	                
	
	                if(RecordSet.next()){
	                    currtype=Util.getIntValue(RecordSet.getString("nodetype"),0);
	                    if(currtype==0){//创建节点
	                        init_road=2;
	                        init_frms=1;
	                    }else{
	                        init_road=Util.getIntValue(RecordSet.getString("isroutedit"),0);
	                        init_frms=Util.getIntValue(RecordSet.getString("istableedit"),0);
	                    }
	                }
	
	                int freeNum=0;
	                String cheStr="";
	                //System.out.println("init_road:"+init_road);
	                if(init_road==1||currtype==0){
	                    //当前节点只有加签权限
	                    RecordSet.execute("select * from ("+
	                    "select base.id,base.requestid,base.nodename,node.nodetype,node.isFormSignature,node.IsPendingForward,"+
	                    "base.operators,base.Signtype,base.floworder from workflow_flownode node,workflow_nodebase base where "+
	                    "node.nodeid=base.id and base.startnodeid="+nodeid+" and base.requestid="+requestid+
	                    ") a "+
	                    "left join ("+
	                    "select rights.nodeid,manage.retract,manage.pigeonhole,rights.isroutedit,rights.istableedit from "+
	                    "workflow_function_manage manage full join workflow_freeright rights on manage.operatortype=rights.nodeid"+
	                    ") b on a.id=b.nodeid ");       
	                    
	                   
	
	                    while (RecordSet.next()) {
	                        
	                            String operids=Util.null2String(RecordSet.getString("operators"));
	                            String opernames="";
	                            ArrayList operatorlist=Util.TokenizerString(operids,",");
	                            for(int j=0;j<operatorlist.size();j++){
	                                if(opernames.equals("")){
	                                    opernames=ResourceComInfo.getLastname((String)operatorlist.get(j));
	                                }else{
	                                    opernames+=","+ResourceComInfo.getLastname((String)operatorlist.get(j));
	                                }
	                            }
	                            String nodeDo="";
	                            int pforward=Util.getIntValue(RecordSet.getString("IsPendingForward"),0);
	                            if(pforward==1){
	                                nodeDo=nodeDo+"1,";
	                            }                           
	                            int pigeonhole=Util.getIntValue(RecordSet.getString("pigeonhole"),0);
	                            if(pigeonhole==1){
	                                nodeDo=nodeDo+"2,";
	                            }
	                            int retract=Util.getIntValue(RecordSet.getString("retract"),0);
	                            if(retract==1){
	                                nodeDo=nodeDo+"3,";
	                            }
	                            if(nodeDo.lastIndexOf(",")>-1){
	                                nodeDo = nodeDo.substring(0,nodeDo.lastIndexOf(","));
	                            }
	                %>      
	                        <input type='hidden' name='floworder_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("floworder"),0)%>'/>
	                        <input type='hidden' name='nodeid_<%=freeNum%>' value='<%=Util.null2String(RecordSet.getString("nodeid"))%>'/>
	                        <input type='hidden' name='nodename_<%=freeNum%>' value='<%=Util.null2String(RecordSet.getString("nodename"))%>'/>
	                        <input type='hidden' name='nodetype_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("nodetype"),0)%>'/>
	                        <input type='hidden' name='Signtype_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("Signtype"),0)%>'/>
	                        <input type='hidden' name='operators_<%=freeNum%>' value='<%=operids%>'/>
	                        <input type='hidden' name='operatornames_<%=freeNum%>' value='<%=opernames%>'/>
	                        
	                        <input type='hidden' name='road_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("isroutedit"),0)%>' />
	                        <input type='hidden' name='frms_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("istableedit"),0)%>'/>
	                        <input type='hidden' name='trust_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("isFormSignature"),0)%>'/>
	                        <input type='hidden' name='nodeDo_<%=freeNum%>' value='<%=nodeDo%>'/>
	                <%
	                        cheStr=cheStr+"nodename_"+freeNum+",operators_"+freeNum+",";
	                        freeNum++;  
	                    }
	                }else if(init_road==2){
	                    //当前节点有后续权限
	                    //从当前节点进行遍历
	                    int nextnode=nodeid;
	                    while(nextnode!=-1){
	                        if(nextnode!=nodeid){   //当前节点除外
	                            RecordSet.executeSql("select * from("+
	                            "select base.id,node.workflowid,base.nodename,node.nodetype,node.isFormSignature,node.IsPendingForward,"+
	                            "base.operators,base.Signtype,base.floworder from workflow_flownode node,workflow_nodebase base where node.nodeid=base.id ) a "+
	                            "left join ("+
	                            "select rights.nodeid,manage.retract,manage.pigeonhole,rights.isroutedit,rights.istableedit from "+
	                            "workflow_function_manage manage full join workflow_freeright rights on manage.operatortype=rights.nodeid"+
	                            ") b on a.id=b.nodeid where a.workflowid="+workflowid+" and a.id="+nextnode);
	                            if (RecordSet.next()) {
	                                
	                                int nextnodetype=Util.getIntValue(RecordSet.getString("nodetype"),0);
	                                if(nextnodetype==3){
	                                    //已经是归档节点了
	                                    nextnode=-1;
	                                    break;
	                                }
	                                String operids=Util.null2String(RecordSet.getString("operators"));
	                                String opernames="";
	                                ArrayList operatorlist=Util.TokenizerString(operids,",");
	                                for(int j=0;j<operatorlist.size();j++){
	                                    if(opernames.equals("")){
	                                        opernames=ResourceComInfo.getLastname((String)operatorlist.get(j));
	                                    }else{
	                                        opernames+=","+ResourceComInfo.getLastname((String)operatorlist.get(j));
	                                    }
	                                }
	                                String nodeDo="";
	                                int pforward=Util.getIntValue(RecordSet.getString("IsPendingForward"),0);
	                                if(pforward==1){
	                                    nodeDo=nodeDo+"1,";
	                                }                           
	                                int pigeonhole=Util.getIntValue(RecordSet.getString("pigeonhole"),0);
	                                if(pigeonhole==1){
	                                    nodeDo=nodeDo+"2,";
	                                }
	                                int retract=Util.getIntValue(RecordSet.getString("retract"),0);
	                                if(retract==1){
	                                    nodeDo=nodeDo+"3,";
	                                }
	                                if(nodeDo.lastIndexOf(",")>-1){
	                                    nodeDo = nodeDo.substring(0,nodeDo.lastIndexOf(","));
	                                }
	                            %>
	                                <input type='hidden' name='floworder_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("floworder"),0)%>'/>
	                                <input type='hidden' name='nodename_<%=freeNum%>' value='<%=Util.null2String(RecordSet.getString("nodename"))%>'/>
	                                <input type='hidden' name='nodeid_<%=freeNum%>' value='<%=Util.null2String(RecordSet.getString("nodeid"))%>'/>
	                                <input type='hidden' name='nodetype_<%=freeNum%>' value='<%=nextnodetype%>'/>
	                                <input type='hidden' name='Signtype_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("Signtype"),0)%>'/>
	                                <input type='hidden' name='operators_<%=freeNum%>' value='<%=operids%>'/>
	                                <input type='hidden' name='operatornames_<%=freeNum%>' value='<%=opernames%>'/>
	                                
	                                <input type='hidden' name='road_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("isroutedit"),0)%>' />
	                                <input type='hidden' name='frms_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("istableedit"),0)%>'/>
	                                <input type='hidden' name='trust_<%=freeNum%>' value='<%=Util.getIntValue(RecordSet.getString("isFormSignature"),0)%>'/>
	                                <input type='hidden' name='nodeDo_<%=freeNum%>' value='<%=nodeDo%>'/>                                   
	                            <%
	                                cheStr=cheStr+"nodename_"+freeNum+",operators_"+freeNum+",";
	                                freeNum++;
	                            }
	                        }
	                        //获取下一个节点
	                        RecordSet.executeSql("select destnodeid from workflow_nodelink where  nodeid="+nextnode+" and wfrequestid is null and workflowid = "+workflowid+" and (((select COUNT(1) from workflow_nodebase b where workflow_nodelink.nodeid=b.id and (IsFreeNode is null or IsFreeNode !='1'))>0 and (select COUNT(1) from workflow_nodebase b where workflow_nodelink.destnodeid=b.id and (IsFreeNode is null or IsFreeNode !='1'))>0 ) or ((select COUNT(1) from workflow_nodebase b where workflow_nodelink.nodeid=b.id and IsFreeNode ='1' and requestid="+requestid+")>0  or (select COUNT(1) from workflow_nodebase b where workflow_nodelink.destnodeid=b.id and IsFreeNode ='1' and requestid="+requestid+")>0 ))");
	                        String nextnodeid="";
	                        while (RecordSet.next()) {
	                            nextnodeid=nextnodeid+Util.null2String(RecordSet.getString("destnodeid"))+",";
	                        }
	                        if(!nextnodeid.equals("")){
	                            nextnodeid=nextnodeid.substring(0,nextnodeid.lastIndexOf(","));
	                            String[] nnode=nextnodeid.split(",");
	                            if(nnode.length>1){
	                                for(String nid:nnode){
	                                    RecordSet.executeSql("select COUNT(id) as id from workflow_nodelink where destnodeid="+nid);
	                                    if(RecordSet.next()) {
	                                        if(Util.getIntValue(RecordSet.getString("id"),0)==1){
	                                            nextnode=Util.getIntValue(nid,0);
	                                        }
	                                    }else{//可能数据查询报错了
	                                        nextnode=-1;
	                                    }
	                                }
	                            }else{
	                                nextnode=Util.getIntValue(nextnodeid,0);
	                            }
	                        }else{//没有下一节点，可能已是归档节点了
	                            nextnode=-1;
	                        }
	                    }
	                }
	                if(freeNum>0){
	            %>
	                    <input type='hidden' id='rownum' name='rownum' value='<%=freeNum%>'/>
	                    <input type='hidden' id='indexnum' name='indexnum' value='<%=freeNum%>'/>
	                    <input type='hidden' id='checkfield' name='checkfield' value='<%=cheStr%>'/>
	                    <input type='hidden' name="freeNode" value="1"/>
	                    <input type='hidden' name="freeDuty" value="<%=init_road%>"/>
	            <%
	                }else{
	            %>  
	                    <input type='hidden' name="freeNode" value="0"/>
	            <%
	                }
	            %>
	      </div>
    
     <%	
    	}
     %>
     <input type="hidden" name="f_weaver_belongto_userid" value="<%=request.getParameter("f_weaver_belongto_userid") %>">
	<input type="hidden" name="f_weaver_belongto_usertype" value="<%=request.getParameter("f_weaver_belongto_usertype") %>">
    <input type="hidden" id="docFlags"  value="<%=docFlagss%>" /> 
</form>
<%}
//add by ben for relationgrequests 2006-05-09
int haslinkworkflow=Util.getIntValue(String.valueOf(session.getAttribute("haslinkworkflow")),0);
if(haslinkworkflow==1&&!isrequest.equals("1")){
    session.setAttribute("desrequestid",""+requestid);
	desrequestid=requestid;
}else{
    if(haslinkworkflow==0&&!isrequest.equals("1")){
        session.removeAttribute("desrequestid");
        int linkwfnum=Util.getIntValue(String.valueOf(session.getAttribute("slinkwfnum")),0);
        for(int i=0;i<=linkwfnum;i++){
            session.removeAttribute("resrequestid"+i);
        }
        session.removeAttribute("slinkwfnum");
    }
}

//添加临时授权信息
if(haslinkworkflow == 1){
    RequestShare.addRequestViewRightInfo(request);
}
%>

<%
String custompage = "";
//查询该工作流的表单id，是否是单据（0否，1是），帮助文档id
RecordSet.executeProc("workflow_Workflowbase_SByID",workflowid+"");
if(RecordSet.next()){
	custompage = Util.null2String(RecordSet.getString("custompage"));
}
%>
<%
	if(!custompage.equals("")){
%>
		<jsp:include page="<%=custompage%>" flush="true">
			<jsp:param name="userid" value="<%=userid%>" />
		    <jsp:param name="languageid" value="<%=user.getLanguage()%>" />
		    <jsp:param name="fromFlowDoc" value="<%=fromFlowDoc%>" />
		    <jsp:param name="requestid" value="<%=requestid%>" />
		    <jsp:param name="requestlevel" value="<%=requestlevel%>" />
		    <jsp:param name="creater" value="<%=creater%>" />
		    <jsp:param name="creatertype" value="<%=creatertype%>" />
		    <jsp:param name="deleted" value="<%=deleted%>" />
		    <jsp:param name="billid" value="<%=billid%>" />
			<jsp:param name="isbill" value="<%=isbill%>" />
		    <jsp:param name="workflowid" value="<%=workflowid%>" />
		    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
		    <jsp:param name="formid" value="<%=formid%>" />
		    <jsp:param name="nodeid" value="<%=nodeid%>" />
		    <jsp:param name="nodetype" value="<%=nodetype%>" />
		    <jsp:param name="isreopen" value="<%=isreopen%>" />
		    <jsp:param name="isreject" value="<%=isreject%>" />
		    <jsp:param name="isremark" value="<%=isremark%>" />
			<jsp:param name="currentdate" value="<%=currentdate%>" />
			<jsp:param name="currenttime" value="<%=currenttime%>" />
		    <jsp:param name="needcheck" value="<%=needcheck%>" />
			<jsp:param name="topage" value="<%=topage%>" />
			<jsp:param name="newenddate" value="<%=newenddate%>" />
			<jsp:param name="newfromdate" value="<%=newfromdate%>" />
			<jsp:param name="docfileid" value="<%=docfileid%>" />
			<jsp:param name="newdocid" value="<%=newdocid%>" />
        </jsp:include>
<%		
	}
%>


</td>
		</tr>
		</TABLE>

		<%
		int printmodeid=0;
		int modeprintdes=0;
		RecordSet.executeSql("select printdes from workflow_flownode where workflowid="+workflowid+" and nodeid="+nodeid);
		if(RecordSet.next()){
			modeprintdes=Util.getIntValue(Util.null2String(RecordSet.getString("printdes")),0);
		}
		if(  modeprintdes!=1){
		    RecordSet.executeSql("select id from workflow_nodemode where isprint='1' and workflowid="+workflowid+" and nodeid="+nodeid);
		    if(RecordSet.next()){
		    	printmodeid=RecordSet.getInt("id");
		    }else{
		        RecordSet.executeSql("select id from workflow_formmode where formid="+formid+" and isbill='"+isbill+"' order by isprint desc");
		        while(RecordSet.next()){
		            if(modeid<1){
		            	printmodeid=RecordSet.getInt("id");
		            }
		        }
		    }
		}
		%>
		<!-- 点击打印按钮 存在打印模板时使用以此隐藏打印时的主窗口 -->
		<iframe style="display: none;visibility: hidden;" id="loadprintmodeFrame" src="">
		</iframe>
</body>
</html>
<script language=javascript src="/js/checkData_wev8.js"></script>
<script type="text/javascript" src="/js/swfupload/workflowswfupload_wev8.js"></script>
<jsp:include page="/workflow/request/ManageRequestNoFormIframeForScript.jsp" flush="true">
	<jsp:param name="userid" value="<%=userid%>" />
    <jsp:param name="languageid" value="<%=user.getLanguage()%>" />
    <jsp:param name="fromFlowDoc" value="<%=fromFlowDoc%>" />
    <jsp:param name="requestid" value="<%=requestid%>" />
    <jsp:param name="requestlevel" value="<%=requestlevel%>" />
    <jsp:param name="creater" value="<%=creater%>" />
    <jsp:param name="creatertype" value="<%=creatertype%>" />
    <jsp:param name="deleted" value="<%=deleted%>" />
    <jsp:param name="billid" value="<%=billid%>" />
	<jsp:param name="isbill" value="<%=isbill%>" />
    <jsp:param name="workflowid" value="<%=workflowid%>" />
    <jsp:param name="workflowtype" value="<%=workflowtype%>" />
    <jsp:param name="formid" value="<%=formid%>" />
    <jsp:param name="nodeid" value="<%=nodeid%>" />
    <jsp:param name="nodetype" value="<%=nodetype%>" />
    <jsp:param name="isreopen" value="<%=isreopen%>" />
    <jsp:param name="isreject" value="<%=isreject%>" />
    <jsp:param name="isremark" value="<%=isremark%>" />
	<jsp:param name="currentdate" value="<%=currentdate%>" />
	<jsp:param name="currenttime" value="<%=currenttime%>" />
    <jsp:param name="needcheck" value="<%=needcheck%>" />
	<jsp:param name="topage" value="<%=topage%>" />
	<jsp:param name="newenddate" value="<%=newenddate%>" />
	<jsp:param name="newfromdate" value="<%=newfromdate%>" />
	<jsp:param name="docfileid" value="<%=docfileid%>" />
	<jsp:param name="newdocid" value="<%=newdocid%>" />
</jsp:include>

<jsp:include page="/workflow/request/UserDefinedRequestBrowser.jsp" flush="true">
	<jsp:param name="workflowid" value="<%=workflowid%>" />
</jsp:include>
