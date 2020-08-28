<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/browserTag" prefix="brow"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page" />

<%
Integer lg=(Integer)user.getLanguage();
weaver.general.AccountType.langId.set(lg);
%>
<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="AccountType" class="weaver.general.AccountType" scope="page" />
<jsp:useBean id="LicenseCheckLogin" class="weaver.login.LicenseCheckLogin" scope="page" />
<HTML>
	<HEAD>
		<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
		<script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
		<script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
		<script type='text/javascript' src='/js/jquery-autocomplete/lib/jquery.bgiframe.min_wev8.js'></script>
<script type='text/javascript' src='/js/jquery-autocomplete/jquery.autocomplete_wev8.js'></script>
<script type='text/javascript' src='/js/jquery-autocomplete/browser_wev8.js'></script>
<link rel="stylesheet" type="text/css" href="/js/jquery-autocomplete/jquery.autocomplete_wev8.css" />
<link rel="stylesheet" type="text/css" href="/js/jquery-autocomplete/browser_wev8.css" />
		<SCRIPT language="JavaScript" src="/js/weaver_wev8.js"></SCRIPT>
		<link rel="stylesheet" href="/css/ecology8/request/requestTopMenu_wev8.css" type="text/css" />
		<link rel="stylesheet" href="/wui/theme/ecology8/jquery/js/zDialog_e8_wev8.css" type="text/css" />
		<style>
		.checkbox {
			display: none
		}
		</style>
	</head>
	<%
	int language =user.getLanguage();
	Calendar now = Calendar.getInstance();
	
	int userid = user.getUID();
	String imagefilename = "/images/hdReport_wev8.gif";
	String titlename =SystemEnv.getHtmlLabelName(20536,user.getLanguage());
	String needfav ="1";
	String needhelp ="";
	boolean flagaccount = weaver.general.GCONST.getMOREACCOUNTLANDING();
    String out_pageId = "";
	String status = Util.null2String(request.getParameter("status"));
	String beginDate = Util.null2String(request.getParameter("beginDate"));
	String endDate = Util.null2String(request.getParameter("endDate"));

	%>
	<BODY>
		<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
		</div>
		<div id="dialog">
			<div id='colShow'></div>
		</div>
	    <input type="hidden" name="pageId" id="pageId" value="<%=out_pageId%>"/>
		<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
		<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
		<%
		RCMenu += "{刷新,javascript:refersh(),_self} " ;
		RCMenu += "{导出,javascript:_xtable_getAllExcel(),_self} " ;
		RCMenu += "{导入,javascript:daoru(),_self} " ;
		RCMenuHeight += RCMenuHeightStep ;
		%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
		<FORM id=report name=report action="/weixin/person/wx-person-list.jsp" method=post>
			<table id="topTitle" cellpadding="0" cellspacing="0">
				<tr>
					<td></td>
					<td class="rightSearchSpan" style="text-align:right;">
					<span id="advancedSearch" class="advancedSearch"><%=SystemEnv.getHtmlLabelName(21995,user.getLanguage())%></span>
						<span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
				</tr>
			</table>
			
			<% // 查询条件 %>
			<div class="advancedSearchDiv" id="advancedSearchDiv" style="display:none;">
				<wea:layout type="4col">
				<wea:group context="查询条件">
				
                    <wea:item>录入日期</wea:item>
					<wea:item>
							<button type="button" class=Calendar id="selectBeginDate" onclick="onshowPlanDate('beginDate','selectBeginDateSpan')"></BUTTON>
								<SPAN id=selectBeginDateSpan ><%=beginDate%></SPAN>
								<INPUT type="hidden" name="beginDate" id="beginDate" value="<%=beginDate%>">
							&nbsp;-&nbsp;
							<button type="button" class=Calendar id="selectEndDate" onclick="onshowPlanDate('endDate','endDateSpan')"></BUTTON>
								<SPAN id=endDateSpan><%=endDate%></SPAN>
								<INPUT type="hidden" name="endDate" id="endDate" value="<%=endDate%>">
					</wea:item>
					



				</wea:group>
				<wea:group context="">
				<wea:item type="toolbar">
				<input type="button" value="<%=SystemEnv.getHtmlLabelName(30947,user.getLanguage())%>" class="e8_btn_submit" onclick="onBtnSearchClick();"/>
				<input type="button" value="<%=SystemEnv.getHtmlLabelName(31129,user.getLanguage())%>" class="e8_btn_cancel" id="cancel"/>
				</wea:item>
				</wea:group>
				</wea:layout>
			</div>
		</FORM>
		<%
		String backfields = "id,IDNo,Name,ApplyDate,Country,Ethnic,(select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_RZXXB' and a.fieldname='Gender' and c.selectvalue=t.Gender) as Gender,(select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_RZXXB' and a.fieldname='Marital' and c.selectvalue=t.Marital) as Marital,BirthDate,(select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_RZXXB' and a.fieldname='Htype' and c.selectvalue=t.Htype) as Htype,Registered,Province,MailingName,Graduate,GraduationDate,Major,Tel,Contacts,ContactsTel,Education,FlowNo,Experience,(select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_RZXXB' and a.fieldname='status' and c.selectvalue=t.status) as status,EmployeeNo,BusinessUnit,Supervisor,JobType,JobDesc,WorkingBlock,HireDate,DateStarted,DatePayStarts,EmploymentStatus,DispatchCompany,SDate";
		String fromSql  =  " from uf_RZXXB t";
		String sqlWhere =  " 1=1 ";
		
		if(!"".equals(status)){
			sqlWhere+=" and status ='"+status+"' ";
			
		}
		if(!"".equals(beginDate)){
			sqlWhere+=" and ApplyDate >='"+beginDate+"' ";
			
		}
		if(!"".equals(endDate)){
			sqlWhere+=" and ApplyDate <='"+endDate+"' ";
			
		}

		//out.print("select "+backfields+fromSql+" where "+sqlWhere);
		String orderby =  " id desc "  ;
		String tableString = "";
		String operateString= "";
		  tableString =" <table tabletype=\"none\" pagesize=\""+ PageIdConst.getPageSize(out_pageId,user.getUID(),PageIdConst.HRM)+"\" pageId=\""+out_pageId+"\" >"+         
				   "	   <sql backfields=\""+backfields+"\" sqlform=\""+fromSql+"\" sqlwhere=\""+Util.toHtmlForSplitPage(sqlWhere)+"\"  sqlorderby=\""+orderby+"\"  sqlprimarykey=\"id\" sqlsortway=\"desc\" sqlisdistinct=\"false\" />"+
		operateString+
		"			<head>";
				tableString +="<col width=\"120px\" text=\"身份证号\" column=\"IDNo\" orderkey=\"IDNo\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"姓名\" column=\"Name\" orderkey=\"Name\" linkvaluecolumn=\"id\" linkkey=\"billid\" href=\"/formmode/view/AddFormMode.jsp?type=0&amp;modeId=521&amp;formId=-163&amp;opentype=0&amp;viewfrom=fromsearchlist\" target=\"_fullwindow\" />"+ 
				"<col width=\"120px\" text=\"录入日期\" column=\"ApplyDate\" orderkey=\"ApplyDate\"  />"+ 
				"<col width=\"120px\" text=\"国籍\" column=\"Country\" orderkey=\"Country\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"名族\" column=\"Ethnic\" orderkey=\"Ethnic\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"性别\" column=\"Gender\" orderkey=\"Gender\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"婚姻状况\" column=\"Marital\" orderkey=\"Marital\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"出生日期\" column=\"BirthDate\" orderkey=\"BirthDate\"  />"+ 
				"<col width=\"120px\" text=\"户口类型\" column=\"Htype\" orderkey=\"Htype\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"地址\" column=\"Registered\" orderkey=\"Registered\"  />"+ 
				"<col width=\"120px\" text=\"省份\" column=\"Province\" orderkey=\"Province\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"银行卡名字\" column=\"MailingName\" orderkey=\"MailingName\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"毕业学校\" column=\"Graduate\" orderkey=\"Graduate\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"毕业日期\" column=\"GraduationDate\" orderkey=\"GraduationDate\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"专业\" column=\"Major\" orderkey=\"Major\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"本人联系电话\" column=\"Tel\" orderkey=\"Tel\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"联系人\" column=\"Contacts\" orderkey=\"Contacts\"  />"+ 
				"<col width=\"120px\" text=\"联系电话\" column=\"ContactsTel\" orderkey=\"ContactsTel\"   />"+ 
				"<col width=\"120px\" text=\"学历\" column=\"Education\" orderkey=\"Education\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"流程编号\" column=\"FlowNo\" orderkey=\"FlowNo\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"工作经历\" column=\"Experience\" orderkey=\"Experience\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"状态\" column=\"status\" orderkey=\"status\"   />"+ 
				"<col width=\"120px\" text=\"工号\" column=\"EmployeeNo\" orderkey=\"EmployeeNo\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"公司代码\" column=\"BusinessUnit\" orderkey=\"BusinessUnit\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"上级主管\" column=\"Supervisor\" orderkey=\"Supervisor\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"职位代码\" column=\"JobType\" orderkey=\"JobType\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"职位名称\" column=\"JobDesc\" orderkey=\"JobDesc\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"工作厂区\" column=\"WorkingBlock\" orderkey=\"WorkingBlock\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"入职日期\" column=\"HireDate\" orderkey=\"HireDate\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"开始工作日期\" column=\"DateStarted\" orderkey=\"DateStarted\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"发薪开始日期\" column=\"DatePayStarts\" orderkey=\"DatePayStarts\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"用工类型\" column=\"EmploymentStatus\" orderkey=\"EmploymentStatus\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"外包公司\" column=\"DispatchCompany\" orderkey=\"DispatchCompany\"  hide=\"true\" />"+ 
				"<col width=\"120px\" text=\"试用期\" column=\"SDate\" orderkey=\"SDate\"  hide=\"true\" />"+ 
						
						
						"</head>"+
		 "</table>";
	//showExpExcel="false"
	%>
	<wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run"  showExpExcel="false"/>
	<script type="text/javascript">
		 function onBtnSearchClick() {
			report.submit();
		}

		function refersh() {
  			window.location.reload();
  		}

		function daoru() {
		var title = "导入";
		var url = "/systeminfo/BrowserMain.jsp?url=/weixin/person/ExcelIn.jsp";
		// var url = "/systeminfo/BrowserMain.jsp?url=/seahonor/crm/ModeForward.jsp?typex=custom,,";

		var diag_vote = new window.top.Dialog();
		diag_vote.currentWindow = window;
		diag_vote.Width = 500;
		diag_vote.Height = 300;
		diag_vote.Modal = true;
		diag_vote.Title = title;
		diag_vote.URL = url;
		diag_vote.isIframe=false;
		diag_vote.CancelEvent=function(){diag_vote.close();window.location.reload();};
		diag_vote.show();
		

	}
	  
   </script>
		<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
	
</BODY>
</HTML>