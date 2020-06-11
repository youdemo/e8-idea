
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="tczc.wp.UpdateBgypRecordAction" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/browserTag" prefix="brow"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />

<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page" />

<HTML>
<HEAD>
<LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
<script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
<script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
	<script type="text/javascript">	
				
		function onCheckup1(){
			if(window.confirm('是否更新上月末记录？')){
				document.report.multiIds.value = "1";
				document.report.submit();
			}
		}
				
		function onBtnSearchClick() {
			report.submit();
		}
					
	</script>
</head>
<%
String imagefilename = "/images/hdReport_wev8.gif";
String titlename =SystemEnv.getHtmlLabelName(20536,user.getLanguage());
String needfav ="1";
String needhelp ="";
boolean isShow = false;

int userid = user.getUID();

String multiIds = Util.null2String(request.getParameter("multiIds"));
String year = Util.null2String(request.getParameter("year"));
String version = Util.null2String(request.getParameter("version"));
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
String nowdate = sf.format(new Date());
String nowyear = nowdate.substring(0,4);
if("".equals(year)){
	year = nowyear;
}


%>

<BODY>

<div id="tabDiv">
	<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
</div>
<div id="dialog">
 <div id='colShow'></div>
</div>
<input type="hidden" name="pageId" id="pageId" value="<%= PageIdConst.HRM_ONLINEUSER %>"/>
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%

	RCMenu += "{刷新,javascript:onBtnSearchClick(),_self} " ;
	RCMenuHeight += RCMenuHeightStep ;
	RCMenu += "{启用,javascript:onCheckup1(),_self} " ;
	RCMenuHeight += RCMenuHeightStep ;

%>
	<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
	<FORM id=report name=report action=/tczc/wp/updatewprecord.jsp method=post>
		<input type="hidden" name="multiIds" value="-1">
		<table id="topTitle" cellpadding="0" cellspacing="0">
			<tr>

			</tr>
		</table>
			<%
		if("1".equals(multiIds)){	
			boolean isOver = true;
			UpdateBgypRecordAction aa = new UpdateBgypRecordAction();
			aa.execute();
			if(isOver){
				out.println("<font size=\"5px\" color=\"red\">执行完成，更新成功!</font>");
			}else{
				out.println("<font size=\"5px\" color=\"red\">执行完成，更新失败</font>");
			}
		}
		
			
	%>

<wea:layout type="2col">
<wea:group context="更新记录" attributes="test">



	<wea:item>&nbsp;&nbsp;</wea:item>
		<wea:item>
		<table width="30%">
			<tr>

			</tr>
			<tr>
				<td><input name="hi1" type="button" value="更新" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;  max-width: 100px;width:100px;height:30px;font-size:15px;" onClick="onCheckup()"></td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<!--<td><input name="hi" type="button" value="刷新" class="e8_btn_top" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;  max-width: 100px;width:80px;height:30px;font-size:15px;" onClick="onBtnSearchClick1()"></td>-->
			</tr>
		</table>
			<script type="text/javascript">	
				
				function onCheckup(){

					 top.Dialog.confirm('是否更新上月末记录？',function(){
       					 document.report.multiIds.value = "1";
						document.report.submit();
   					 });

					//if(window.confirm('是否启用预算版本？')){
					//	document.report.multiIds.value = "1";
					//	document.report.submit();
				   // }
				}
								
				function onBtnSearchClick1() {
					report.submit();
				}
					
			</script>
		
			 </wea:item>
		


</wea:group>
</wea:layout>	
			
	</FORM>

</BODY>
<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
    <SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
    <SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>

</HTML>
	

