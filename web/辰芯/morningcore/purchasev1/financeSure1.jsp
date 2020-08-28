<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ page import="weaver.general.Util" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
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


%>
<script language="javascript">
if("<%=isclose%>"==1){
	var dialog = parent.getDialog(window);
	var parentWin = parent.getParentWindow(window);
	parentWin.location="purchase.jsp";
	parentWin.closeDialog();	
}
</script>

<%

 String fromSql = " select x.djbh,x.requestid,x.sqr,x.sqrq,x.sqbm,x.hzzfje,(select isnull(sum(bcfke),0.00) from formtable_main_265 where  re_ID = x.requestid ) as yfje  from moc_fktzd_view x  where x.requestid in (" + ids +"0)" ;
		   
rs.executeSql(fromSql );
//out.print("select x.* " + fromSql + " where x.requestid in(" + ids +"0)");
String sqr = "";
String sqbm = "";
String zje = "";
String lcNo = "";
String sg = "";
if(rs.next()){
   sqr = Util.null2String(rs.getString("sqr"));	
   sqbm = Util.null2String(rs.getString("sqbm"));	
   zje = Util.null2String(rs.getString("hzzfje"));	
   lcNo = Util.null2String(rs.getString("djbh")); 
   sg = Util.null2String(rs.getString("yfje")); 	
}

%>
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
  <input type="hidden" name="method" value="one">
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
		<wea:layout type="twoCol">
		    <wea:group context='填写内容'>
				<wea:item>申请人信息</wea:item>
				<wea:item>姓名: <%=ResourceComInfo.getLastname(sqr)%> &nbsp;&nbsp;&nbsp; 所属部门：<%=DepartmentComInfo.getDepartmentname(sqbm)%> </wea:item>
				<wea:item>流程编号</wea:item><wea:item><%=lcNo%> </wea:item>
		    	<wea:item>付款情况</wea:item><wea:item>总付款金额： <%=zje%> ， 已付款金额 ： <%=sg%> </wea:item>
		    	<wea:item>本次付款金额</wea:item>
		    	<wea:item>
		    		<wea:required id="descimage" required="true" value="">
			    		<input class=Inputstyle maxLength=150 size=50 id="nowt" name="desc" onchange='checkinput("desc","descimage");checkOne(this)' value="" onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}">
			    	</wea:required>
		    	</wea:item>
		   
		    </wea:group>
		</wea:layout>		
<iframe id="checkType" src="" style="display: none"></iframe>
</FORM>
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
function checkOne(inp){
    var val = $(inp).val();
    // if (/^[-]?\d*?\d{1,2}$/.test(val) == true) {
    if (/^\d{0,8}\.{0,1}(\d{1,2})?$/.test(val) == true) {
    } else {
        $(inp).val("");
    }
    var ste = <%=zje%>  -  <%=sg%>;
    if(val > ste || val < 0){
      $(inp).val(ste);
    }
}

function submitData(){
	jQuery("#tjjy").hide();
	if (check_form(weaver,'desc')){
		weaver.submit();
	}
	
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
