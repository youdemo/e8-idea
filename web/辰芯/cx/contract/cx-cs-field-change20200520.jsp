<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="morningcore.contract.bean.ChangeFieldBean"%>
<%@ page import="morningcore.contract.dao.ChangeFieldDao"%>
<%@ page import="morningcore.contract.util.ValueTransMethod"%>
<%@ page import="morningcore.contract.util.EditTransMethod"%>
<%@ page import="morningcore.contract.util.BrowserInfoUtil"%>
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
	String customid = Util.null2String(request.getParameter("customid"));
	String fieldid = Util.null2String(request.getParameter("fieldid"));
	String seqno =  Util.null2String(request.getParameter("seqno"));
	String sfxj =  Util.null2String(request.getParameter("sfxj"));
	String issuccess =  Util.null2String(request.getParameter("issuccess"));
	String khmc = "";
	
	ChangeFieldDao cfd = new ChangeFieldDao();
	ChangeFieldBean cfb = cfd.getChangeFieldBean(fieldid);
	String fieldshowname = cfb.getShowname();
	String fieldname = cfb.getFieldname();
	String fieldvalue = "";
	ValueTransMethod vtm = new ValueTransMethod();
	EditTransMethod etm = new EditTransMethod();
	BrowserInfoUtil biu = new BrowserInfoUtil();
	String sql = "select "+fieldname+",khmc from uf_khzsjkhjm where id="+customid;
	rs.executeSql(sql);
	if(rs.next()){
		khmc = Util.null2String(rs.getString("khmc"));
		fieldvalue =  Util.null2String(rs.getString(fieldname));
	}
	String fieldValue2="";
	int count =0;
	sql = "select count(1) as count from  uf_change_detail  where seqno='"+seqno+"'";
	rs.executeSql(sql);
	if(rs.next()){
		count = rs.getInt("count");
	}
	if(count >0){
		sql = "select newvalue,oldvalue from  uf_change_detail  where seqno='"+seqno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			fieldValue2 = Util.null2String(rs.getString("newvalue")).replaceAll("<br>","\n");
			if("1".equals(sfxj)){
				fieldvalue = Util.null2String(rs.getString("oldvalue"));
				fieldValue2 = Util.null2String(rs.getString("newvalue"));
			}
		}
	}
	 
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
		<FORM id=report name=report action="/cx/contract/save-field-change.jsp" method=post>
			<input type="hidden" name="customid" value="<%=customid%>">
			<input type="hidden" name="fieldid" value="<%=fieldid%>">
			<input type="hidden" name="seqno" value="<%=seqno%>">
			<input type="hidden" name="fieldValue3" id="fieldValue3" value="<%=fieldValue2%>">

			
		
		<div >
				<wea:layout type="2col">
				<wea:group context="修改客户字段">
				<wea:item>客户名称</wea:item>
				<wea:item>
				<%=khmc%>
                 <input type="hidden" name="customname" id="customname" class="InputStyle" type="text" value="<%=khmc%>" />
                </wea:item>
                <wea:item>字段名称</wea:item>
				<wea:item>
				<%=fieldshowname%>
                 <input type="hidden" name="fieldname" id="fieldname" class="InputStyle" type="text" value="<%=fieldshowname%>"  />
             
                </wea:item>
				<wea:item>原值</wea:item>
				<wea:item>
               	<%=vtm.doTrans(fieldid,fieldvalue)%>
            	 <input type="hidden" name="oldvalue" id="oldvalue" value="<%=fieldvalue%>">
                </wea:item>
                <wea:item>新值</wea:item>
	            <%
					if("1".equals(sfxj)){
				%>
					<wea:item>
	                <%=vtm.doTrans(fieldid,fieldValue2)%>
	            	 <input type="hidden" name="newalue" id="newalue" value="<%=fieldValue2%>">
	                </wea:item>
				<%
					}else{
				if("1".equals(cfb.getFieldtype())){
							String showfieldname=cfb.getFieldname();
							String isMustInput="1";
						
							String buttontype=cfb.getButtontype();
							if("2".equals(buttontype)){
			%>
				<wea:item>
					    <button type="button" class=Calendar id="select<%=showfieldname%>" onclick="onshowPlanDate1('<%=showfieldname%>','select<%=showfieldname%>Span','<%=isMustInput%>')"></BUTTON>
                        <SPAN id="select<%=showfieldname%>Span" >
				
					
					   <%=fieldValue2%>
			
						</SPAN>
                        <INPUT type="hidden" name="<%=showfieldname%>" id="<%=showfieldname%>" value="<%=fieldValue2%>">

				</wea:item>
			<%	
							}else if("3".equals(buttontype)){
			%>
				<wea:item>
					<BUTTON type="button" class=Clock onClick="onShowTime(<%=showfieldname%>span,<%=showfieldname%>)"></BUTTON>
					<SPAN id="<%=showfieldname%>span">
				
					   <%=fieldValue2%>
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
							if("4".equals(cfb.getFieldtype())){
					%>
								<wea:item>
								<textarea class="Inputstyle" temptype="1" viewtype="0" temptitle="<%=fieldshowname%>" id="<%=fieldname%>" name="<%=fieldname%>" rows="4" onchange="checkinput2('<%=fieldname%>','<%=fieldname%>span',this.getAttribute('viewtype'));checkLength('<%=fieldname%>','0','<%=fieldshowname%>','文本长度不能超过','1个中文字符等于3个长度');" cols="40" onpropertychange="" _listener="" oninput="" onfocus="" onclick="" ondblclick="" onmouseover="" onmouseout="" style="width:80%;word-break:break-all;word-wrap:break-word" onafterpaste=""></textarea>
								<span id="<%=fieldname%>span"></span>
								</wea:item>								
					<%
							}else if("4".equals(cfb.getFieldtype())){

							}else{
							
								fieldValue2 = etm.doChangeEditTrans(cfb,fieldValue2);
							
						
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
		var filedtype='<%=cfb.getFieldtype()%>';
		var sfxj = '<%=sfxj%>';
		//var fieldValue = "无奥术大师多 222  Asdasd 222";
		var fieldValue = jQuery("#fieldValue3").val();
		var fieldname = '<%=cfb.getFieldname()%>';
		if(filedtype == '4'&& sfxj == "0"){
			jQuery("#"+fieldname).val(fieldValue);
		}
		var issuccess = '<%=issuccess%>';
		if(issuccess == "1"){
			window.top.Dialog.alert("保存成功");
		}
	});
  function saveprjinfo(){
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