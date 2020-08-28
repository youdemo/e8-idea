<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ page import="weaver.general.Util" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea" %>
<%@ taglib uri="/browserTag" prefix="brow" %>
<%@ page import="weaver.general.PageIdConst" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.systeminfo.SystemEnv" %>
<%@ page import="weaver.file.Prop" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page"/>
<jsp:useBean id="DepartmentComInfo" class="weaver.hrm.company.DepartmentComInfo" scope="page"/>

<HTML><HEAD>
<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
<LINK href="/wui/theme/ecology8/jquery/js/e8_zDialog_btn_wev8.css" type=text/css rel=STYLESHEET>
<script language="javascript" src="../../js/weaver_wev8.js"></script>
</HEAD>
<%
String imagefilename = "/images/hdMaintenance_wev8.gif";
String titlename = SystemEnv.getHtmlLabelName(16579,user.getLanguage());
String needfav ="1";
String needhelp ="";
String dialog = Util.null2String(request.getParameter("dialog"));
String isclose = Util.null2String(request.getParameter("isclose"));
String ids = Util.null2String(request.getParameter("ids"));

 String late_pageId = "FINANCE00002cg2";
%>
<script language="javascript">
if("<%=isclose%>"==1){
	var dialog = parent.getDialog(window);
	var parentWin = parent.getParentWindow(window);
	parentWin.location="purchase.jsp";
	parentWin.closeDialog();	
}
</script>


<body scroll="no">
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>

<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%
RCMenu += "{提交,javascript:submitData(),_self} " ;
RCMenuHeight += RCMenuHeightStep;
RCMenu += "{"+SystemEnv.getHtmlLabelName(201,user.getLanguage())+",javascript:btn_cancle(),_top} " ;
RCMenuHeight += RCMenuHeightStep;
%>
<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<div class="zDialog_div_content">
<jsp:include page="/systeminfo/commonTabHead.jsp"></jsp:include>
<FORM id=weaver action="financeOper.jsp" method=post style="width:100%;">
  <input type="hidden" name="method" value="more">
  <input type="hidden" name="dialog" value="<%=dialog%>">
  <input type="hidden" name="ids" value="<%=ids%>">
	<table id="topTitle" cellpadding="0" cellspacing="0">
		<tr>
			<td></td>
			<td class="rightSearchSpan" style="text-align:right; width:500px!important">
			<input type=button class="e8_btn_top" onclick="submitData();"  id = "tjjy" value="提交">
			<input type=button class="e8_btn_top" onclick="btn_cancle();" value="取消">
				
				<span title="<%=SystemEnv.getHtmlLabelName(81804,user.getLanguage())%>" class="cornerMenu"></span>
			</td>
		</tr>
	</table>  
<iframe id="checkType" src="" style="display: none"></iframe>
</FORM>
    <%
    
    String backfields = " x.djbh,x.requestid,x.sqr,x.sqrq,x.sqbm,x.hzzfje,(select isnull(sum(bcfke),0.00) from formtable_main_265 where  re_ID = x.requestid ) as yfje  ";
    //  requestid in(select requestid from workflow_requestbase where currentnodetype >=0)and
    String fromSql = " from moc_fktzd_view  x ";//sql查询的表
    String sqlWhere = " 1=1 and x.requestid in ("+ids+"0)";//where条件



//  out.println("select " + backfields + fromSql + "where" + sqlWhere);
    String orderby = " x.sqrq ";//排序关键字
    String tableString = "";
    tableString = " <table tabletype=\"none\" pagesize=\"" + PageIdConst.getPageSize(late_pageId, user.getUID(), PageIdConst.HRM) + "\" pageId=\"" + late_pageId + "\">" +
            "	   <sql backfields=\"" + backfields + "\" sqlform=\"" + fromSql + "\" sqlwhere=\"" + Util.toHtmlForSplitPage(sqlWhere) + "\"  sqlorderby=\"" + orderby + "\" " +
            " sqlprimarykey=\"requestid\" sqlsortway=\"asc\" sqlisdistinct=\"false\"/>" +
            "	<head>";
    tableString += "   <col width=\"10%\" text=\"流程编号\" column=\"djbh\" orderkey=\"djbh\"  linkvaluecolumn=\"requestid\" linkkey=\"requestid\" href=\"/workflow/request/ViewRequest.jsp\" target=\"_fullwindow\" />" +
            "   <col width=\"10%\" text=\"申请人\" column=\"sqr\" orderkey=\"sqr\"  transmethod=\"weaver.hrm.resource.ResourceComInfo.getLastname\" />" +
            "	<col width=\"10%\" text=\"申请日期\" column=\"sqrq\" orderkey=\"sqrq\" />" +
            "	<col width=\"12%\" text=\"申请部门\" column=\"sqbm\" orderkey=\"sqbm\"  transmethod=\"weaver.hrm.company.DepartmentComInfo.getDepartmentname\" />" +
            "	<col width=\"10%\" text=\"总付款金额\" column=\"hzzfje\" orderkey=\"hzzfje\" />" +
	    "	<col width=\"10%\" text=\"已付款金额\" column=\"yfje\" orderkey=\"yfje\" />" +
            "	</head>" +
            " </table>";
%>

    <wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run" showExpExcel="false"/>

		

<%if("1".equals(dialog)){ %>
<jsp:include page="/systeminfo/commonTabFoot.jsp"></jsp:include>  
</div>
<div id="zDialog_div_bottom" class="zDialog_div_bottom">
	<wea:layout needImportDefaultJsAndCss="false">
		<wea:group context=""  attributes="{\"groupDisplay\":\"none\"}">
			<wea:item type="toolbar">
		    	<input type="button" value="<%=SystemEnv.getHtmlLabelName(309,user.getLanguage())%>" id="zd_btn_cancle"  class="zd_btn_cancle" onclick="dialog.closeByHand()">
			</wea:item>
		</wea:group>
	</wea:layout>
</div>
<%} %>
<script language="javascript">


if("<%=dialog%>"==1){	
	var dialog = parent.getDialog(window);
	var parentWin = parent.getParentWindow(window);
	function btn_cancle(){
		parentWin.closeDialog();
	}
}
if("<%=isclose%>"==1){
	var dialog = parent.getDialog(window);
	var parentWin = parent.getParentWindow(window);
	parentWin.location="purchase.jsp";
	parentWin.closeDialog();	
}


function submitData(){
	jQuery("#tjjy").hide();
	submitForm();
}
function onReturn(){
	location="purchase.jsp";
}


//提交表单
function submitForm(){
    weaver.submit();
}
</script>
</BODY>
</HTML>
