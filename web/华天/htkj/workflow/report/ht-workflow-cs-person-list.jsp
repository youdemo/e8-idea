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
    String out_pageId = "htwfcsry01";
	String beginDate = Util.null2String(request.getParameter("beginDate"));
	String endDate = Util.null2String(request.getParameter("endDate"));
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	String nowdate = sf.format(new Date());
	if("".equals(beginDate) && "".equals(endDate)){
		beginDate = nowdate;
		endDate = nowdate;
	}
	String lcid = Util.null2String(request.getParameter("lcid"));
	String lcidmc = "";
	if(!"".equals(lcid)) {
		String sql = "select lcmc from v_workflow_csmt where lcid='" + lcid + "'";
		rs.execute(sql);
		if(rs.next()){
			lcidmc = Util.null2String(rs.getString("lcmc"));
		}
	}
	String showdate = beginDate+"~"+endDate;
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
		//RCMenu += "{导出,javascript:_xtable_getAllExcel(),_self} " ;
		RCMenuHeight += RCMenuHeightStep ;
		%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
		<FORM id=report name=report action="/htkj/workflow/report/ht-workflow-cs-person-list.jsp" method=post>
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
				
                    <wea:item>归档日期</wea:item>
					<wea:item>
							<button type="button" class=Calendar id="selectBeginDate" onclick="onshowPlanDate('beginDate','selectBeginDateSpan')"></BUTTON>
								<SPAN id=selectBeginDateSpan ><%=beginDate%></SPAN>
								<INPUT type="hidden" name="beginDate" id="beginDate" value="<%=beginDate%>">
							&nbsp;-&nbsp;
							<button type="button" class=Calendar id="selectEndDate" onclick="onshowPlanDate('endDate','endDateSpan')"></BUTTON>
								<SPAN id=endDateSpan><%=endDate%></SPAN>
								<INPUT type="hidden" name="endDate" id="endDate" value="<%=endDate%>">
					</wea:item>

					<wea:item>流程</wea:item>
					<wea:item>
						<brow:browser name='lcid'
									  viewType='0'
									  browserValue='<%=lcid%>'
									  isMustInput='1'
									  browserSpanValue='<%=lcidmc%>'
									  hasInput='true'
									  linkUrl=''
									  completeUrl=''
									  width='60%'
									  isSingle='true'
									  hasAdd='false'
									  browserUrl='/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.lc_cs_lx'>
						</brow:browser>
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
		String backfields = "logid,workflowid,'"+showdate+"' as lastoperatedate,operator,cssl,( select workflowname from workflow_base where id=t.workflowid) as workflowname";
		String fromSql  =  " from (" +
				"   select max(logid) as logid,a.workflowid,b.operator,count(1) as cssl" +
				"  from workflow_requestbase a, workflow_requestlog b" +
				" where a.requestid = b.requestid" +
				"   and a.currentnodetype >= 3  " +
				"    and a.workflowid in (select distinct lcid from uf_workflow_csmt)";
		if(!"".equals(lcid)){
			fromSql+=" and a.workflowid ='"+lcid+"'";
		}
		if(!"".equals(beginDate)){
			fromSql+=" and a.lastoperatedate >='"+beginDate+"' ";
		}
		if(!"".equals(endDate)) {
			fromSql += " and a.lastoperatedate &lt;='" + endDate + "' ";
		}
		fromSql	+="   and b.nodeid in (select distinct jdid from uf_workflow_csmt)" +
				"   and b.logtype in ('0','2','3')" +
				"   and f_ht_checkiscs(b.requestid,b.nodeid,b.operatedate,b.operatetime,b.logid)='1'" +
				"   group by a.workflowid,b.operator ) t";
		String sqlWhere =  " 1=1 ";
		



		//out.print("select "+backfields+fromSql+" where "+sqlWhere);
		String orderby =  " cssl desc,workflowid asc,operator asc "  ;
		String tableString = "";
		String operateString= "";
		  tableString =" <table tabletype=\"none\" pagesize=\""+ PageIdConst.getPageSize(out_pageId,user.getUID(),PageIdConst.HRM)+"\" pageId=\""+out_pageId+"\" >"+         
				   "	   <sql backfields=\""+backfields+"\" sqlform=\""+fromSql+"\" sqlwhere=\""+Util.toHtmlForSplitPage(sqlWhere)+"\"  sqlorderby=\""+orderby+"\"  sqlprimarykey=\"logid\" sqlsortway=\"desc\" sqlisdistinct=\"false\" />"+
		operateString+
		"			<head>";
				tableString +="<col width=\"20%\" text=\"流程名称\" column=\"workflowname\" orderkey=\"workflowname\" />"+
						"<col width=\"20%\" text=\"归档时间\" column=\"lastoperatedate\" orderkey=\"lastoperatedate\" />"+
						"<col width=\"20%\" text=\"员工姓名\" column=\"operator\" orderkey=\"operator\" transmethod='weaver.proj.util.ProjectTransUtil.getResourceNamesWithLink'/>"+
						"<col width=\"20%\" text=\"节点超时次数\" column=\"cssl\" orderkey=\"cssl\" />"+
						"<col width=\"20%\" text=\"相关流程\" column=\"operator\" orderkey=\"operator\" transmethod=\"htkj.workflow.report.CsUtil.csTransView\" otherpara=\"column:lastoperatedate\"/>"+
						"</head>"+
		 "</table>";
	//showExpExcel="false"
	%>
	<wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run"  />
	<script type="text/javascript">
		 function onBtnSearchClick() {
			 var beginDate_val = jQuery("#beginDate").val();
			 var endDate_val = jQuery("#endDate").val();
			 if(beginDate_val==""||endDate_val==""){
				 alert("开始和结束日期不能为空");
				 return;
			 }
			report.submit();
		}

		function refersh() {
  			window.location.reload();
  		}

  		function showrequest(ryid,lastoperatedate){
			var title = "";
			var url = "";

				title = "";
				url="/htkj/workflow/report/ht-workflow-cs-person-request-Url.jsp?ryid="+ryid+"&lastoperatedate="+lastoperatedate;


				if(window.top.Dialog){
					diag_vote = new window.top.Dialog();
				} else {
					diag_vote = new Dialog();
				};
				diag_vote.currentWindow = window;

				diag_vote.maxiumnable = true;
				diag_vote.Width = 1000;
				diag_vote.Height = 600;
				diag_vote.Model = true;
				diag_vote.Title = title;
				diag_vote.URL = url;
				diag_vote.show("");

        }


	  
   </script>
		<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
	
</BODY>
</HTML>