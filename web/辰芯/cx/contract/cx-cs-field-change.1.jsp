<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="hsproject.dao.*" %>
<%@ page import="hsproject.bean.*" %>
<%@ page import="hsproject.util.*" %>
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
	String imagefilename = "/images/hdReport_wev8.gif";
	String titlename =SystemEnv.getHtmlLabelName(20536,user.getLanguage());
	String prjid = Util.null2String(request.getParameter("prjid"));
	String prjtype = Util.null2String(request.getParameter("prjtype"));
	String fieldid = Util.null2String(request.getParameter("fieldid"));
	String seqno =  Util.null2String(request.getParameter("seqno"));
	String sfxj =  Util.null2String(request.getParameter("sfxj"));
	String issuccess =  Util.null2String(request.getParameter("issuccess"));
	ProjectInfoDao pid = new ProjectInfoDao();
	String prjname=pid.getPrjName(prjid);
	ProjectFieldDao pfd = new ProjectFieldDao();
	ProjectFieldBean pfb = pfd.getPrjFieldBean(fieldid);
	String fieldname = pfb.getFieldname();
	String fieldshowname = pfb.getShowname();
	String fieldvalue = pid.getFieldValue(prjid,fieldid);
	ValueTransMethod vtm = new ValueTransMethod();
	EditTransMethod etm = new EditTransMethod();
	BrowserInfoUtil biu = new BrowserInfoUtil();
	String ismust = pfb.getIsmust();
	String fieldValue2="";
	PrjChangeDetailDao pcdd = new PrjChangeDetailDao();
	String isexist=pcdd.checkChangeExist(seqno);
	if("1".equals(isexist)){
		PrjChangeDetailBean pcdb = pcdd.getChangeInfo(seqno);
		prjname = pcdb.getName();
		fieldshowname = pcdb.getFieldname();
		fieldvalue = pcdb.getOldvalue();
		fieldValue2 = pcdb.getNewvalue();
	}
	//判断操作
	 
	 
	%>

	<BODY>
		<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
		</div>
		<div id="dialog">
			<div id='colShow'></div>
		</div>
		
		<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
		<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
		<%
		RCMenu += "{刷新,javascript:refersh(),_self} " ;
		RCMenuHeight += RCMenuHeightStep ;
		%>
		<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
		<FORM id=report name=report action="/hsproject/project/aciton/save-prjfield-change.jsp" method=post>
			<input type="hidden" name="prjid" value="<%=prjid%>">
			<input type="hidden" name="fieldid" value="<%=fieldid%>">
			<input type="hidden" name="prjtype" value="<%=prjtype%>">
			<input type="hidden" name="seqno" value="<%=seqno%>">

			
		
		<div >
				<wea:layout type="2col">
				<wea:group context="修改项目字段">
				<wea:item>项目名称</wea:item>
				<wea:item>
				<%=prjname%>
                 <input type="hidden" name="prjname" id="prjname" class="InputStyle" type="text" value="<%=prjname%>" />
                </wea:item>
                <wea:item>字段名称</wea:item>
				<wea:item>
				<%=fieldshowname%>
                 <input type="hidden" name="fieldname" id="fieldname" class="InputStyle" type="text" value="<%=fieldshowname%>"  />
             
                </wea:item>
				<wea:item>原值</wea:item>
				<wea:item>
                <%=vtm.doTrans(fieldid,fieldvalue,"0")%>
            	 <input type="hidden" name="oldvalue" id="oldvalue" value="<%=fieldvalue%>">
                </wea:item>
                <wea:item>新值</wea:item>
				<%

					if("1".equals(sfxj)){
				%>
					<wea:item>
	                <%=vtm.doTrans(fieldid,fieldValue2,"0")%>
	            	 <input type="hidden" name="newalue" id="newalue" value="<%=fieldValue2%>">
	                </wea:item>
				<%
					}else{
						if(!"1".equals(pfb.getIsreadonly())&&"1".equals(pfb.getFieldtype())){
							String showfieldname=pfb.getFieldname();
							String isMustInput="1";
						
							if("1".equals(pfb.getIsmust())){
								isMustInput = "2";
							}
							if(!"0".equals(pfb.getIscommon())){
								showfieldname="idef_"+pfb.getFieldname();
							}
							String buttontype=pfb.getButtontype();
							if("2".equals(buttontype)){
			%>
				<wea:item>
					    <button type="button" class=Calendar id="select<%=showfieldname%>" onclick="onshowPlanDate1('<%=showfieldname%>','select<%=showfieldname%>Span','<%=isMustInput%>')"></BUTTON>
                        <SPAN id="select<%=showfieldname%>Span" >
						<%
					if("2".equals(isMustInput)&&"".equals(fieldValue2)){
			%>
						<IMG src='/images/BacoError_wev8.gif' align=absMiddle>
			<%									
					}else{
			%>
					   <%=fieldValue2%>
			<%

					}
			%>
						</SPAN>
                        <INPUT type="hidden" name="<%=showfieldname%>" id="<%=showfieldname%>" value="<%=fieldValue2%>">

				</wea:item>
			<%	
							}else if("3".equals(buttontype)){
			%>
				<wea:item>
					<BUTTON type="button" class=Clock onClick="onShowTime(<%=showfieldname%>span,<%=showfieldname%>)"></BUTTON>
					<SPAN id="<%=showfieldname%>span">
				<%
					if("2".equals(isMustInput)&&"".equals(fieldValue2)){
			%>
						<IMG src='/images/BacoError_wev8.gif' align=absMiddle>
			<%
					}else{
			%>
					   <%=fieldValue2%>
			<%

					}
			%>
					</span>
					<input type="hidden" name="<%=showfieldname%>" id="<%=showfieldname%>" value="<%=fieldValue2%>" />   
				</wea:item>
			<%	
							}else{
					%>
								<wea:item>
								<brow:browser name='<%=showfieldname%>'
								viewType='0'
								browserValue='<%=fieldValue2%>'
								isMustInput='<%=isMustInput%>'
								browserSpanValue="<%=biu.getBrowserShowValue(fieldValue2,buttontype)%>"
								hasInput='true'
								linkUrl='<%=biu.getLinkUrl(buttontype)%>' 
								completeUrl='<%=biu.getCompleteUrl(buttontype)%>'
								width='20%'
								isSingle='<%=biu.getIsSingle(buttontype)%>'
								hasAdd='false'
								browserUrl='<%=biu.getBrowserUrl(buttontype)%>'>
								</brow:browser>
								<input type=hidden name="<%=showfieldname%>_name" value="">
								</wea:item>
					<%
							
							}
						}else{
							if("4".equals(pfb.getFieldtype())){
					%>
								
					<%
							}else{
							if("prjtype".equals(pfb.getFieldname())){
								fieldValue2 = new ProjectTypeDao().getTypeName(prjtype);
							}else{
								fieldValue2 = etm.doProjectEditTrans(pfb,fieldValue2);
							//log.writeLog("aa"+fieldValue);
							}
						
				%>
						
						<wea:item><%=fieldValue2%>	    	
						</wea:item>		
				<%
							}
					 }
					}
			%>		
				
             

				</wea:group>
				<wea:group context="">
				<wea:item type="toolbar">
				<%
					if(!"1".equals(sfxj)){
				%>
				<input type="button" value="保存" class="e8_btn_top_first" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100px;" onclick="saveprjinfo();"/>
				<%
					}
				%>
				</wea:item>
				</wea:group>
				</wea:layout>
		<div>
		</FORM>
	<script type="text/javascript">
	 jQuery(document).ready(function () {
		var issuccess = '<%=issuccess%>';
		if(issuccess == "1"){
			window.top.Dialog.alert("保存成功");
		}
	});
  function saveprjinfo(){
  	var ismust="<%=ismust%>";
  		var chkFields = '<%=fieldname%>';
  	if(ismust == "1"){
  		if(!check_form(report,chkFields)) return false;
  	}
  	report.submit();
  }

    
	   function refersh() {
  			window.location.reload();
  		}
  		function onshowPlanDate1(inputname,spanname,isMustInput){
		var returnvalue;
			var oncleaingFun = function(){
				if(isMustInput== "2"){
				$ele4p(spanname).innerHTML = "<IMG src='/images/BacoError_wev8.gif' align=absMiddle>"; 
				}else{
				$ele4p(spanname).innerHTML = ""; 	
				}
				$ele4p(inputname).value = '';
				}
			WdatePicker({lang:languageStr,el:spanname,onpicked:function(dp){
				returnvalue = dp.cal.getDateStr();	
				$dp.$(spanname).innerHTML = returnvalue;
				$dp.$(inputname).value = returnvalue;},oncleared:oncleaingFun});

		var hidename = $ele4p(inputname).value;
			if(hidename != ""){
				$ele4p(inputname).value = hidename; 
				$ele4p(spanname).innerHTML = hidename;
			}else{
				if("2".equals(isMustInput)){
				$ele4p(spanname).innerHTML = "<IMG src='/images/BacoError_wev8.gif' align=absMiddle>"; 
				}else{
				$ele4p(spanname).innerHTML = ""; 	
				}
			}
    }
 
	</script>
	<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/selectDateTime_wev8.js"></script>
	<SCRIPT language="javascript" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
</BODY>
</HTML>