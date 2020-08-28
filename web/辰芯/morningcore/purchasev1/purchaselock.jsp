<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.PageIdConst" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.systeminfo.SystemEnv" %>
<%@ page import="weaver.file.Prop" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea" %>
<%@ taglib uri="/browserTag" prefix="brow" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="rci" class="weaver.hrm.resource.ResourceComInfo" scope="page"/>
<jsp:useBean id="cdi" class="weaver.hrm.company.DepartmentComInfo" scope="page"/>
<HTML>
<HEAD>
    <LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
    <script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
    <script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
</head>
<%
    String imagefilename = "/images/hdReport_wev8.gif";
    String titlename = SystemEnv.getHtmlLabelName(20536, user.getLanguage());
    String needfav = "1";
    String needhelp = "";

    int userId = user.getUID();//获取当前登录用户的ID
    String requestname = Util.null2String(request.getParameter("requestname"));
    String sqr = Util.null2String(request.getParameter("sqr"));
    String sqrqfrom = Util.null2String(request.getParameter("sqrqfrom"));
    String sqrqto = Util.null2String(request.getParameter("sqrqto"));
    String sqbm = Util.null2String(request.getParameter("sqbm"));
    String late_pageId = "FINANCE00001_20190823cg1";
%>
<BODY>
<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>">
				<%=SystemEnv.getHtmlLabelName(20536, user.getLanguage()) %></span>
</div>
<input type="hidden" _showCol="false" name="pageId" id="pageId" value="<%=late_pageId%>"/>
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%
//    RCMenu += "{导出,javascript:_xtable_getAllExcel(),_self} ";
//    RCMenuHeight += RCMenuHeightStep;
	RCMenu += "{刷新,javascript:onBtnSearchClick(),_self} ";
    RCMenuHeight += RCMenuHeightStep;
%>
<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<FORM id=report name=report action="" method=post>
    <table id="topTitle" cellpadding="0" cellspacing="0">
        <tr>
            <td></td>
            <td class="rightSearchSpan" style="text-align:right;">
				<input type=button class="e8_btn_top" onclick="remlocking();" value="解锁">
                <span id="advancedSearch" class="advancedSearch">
						<%=SystemEnv.getHtmlLabelName(21995, user.getLanguage())%>
						</span>
                <span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
            </td>
        </tr>
    </table>
    <div class="advancedSearchDiv" id="advancedSearchDiv" style="display:none;">
        <wea:layout type="4col">
            <wea:group context="查询条件">
                <wea:item>流程编号</wea:item>
                <wea:item>
                    <input name="requestname" id="requestname" class="InputStyle" type="text" value="<%=requestname%>"/>
                </wea:item>
                <wea:item>申请人</wea:item>
                <wea:item>
                    <brow:browser viewType="0" name="sqr" browserValue="<%=sqr%>"
                                  browserOnClick=""
                                  browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/resource/ResourceBrowser.jsp?selectedids="
                                  hasInput="true" isSingle="false" hasBrowser="true" isMustInput='1'
                                  completeUrl="/data.jsp" width="165px"
                                  browserSpanValue="">
                    </brow:browser>
                </wea:item>
                <wea:item>申请部门</wea:item>
                <wea:item>
                    <brow:browser viewType="0" name="sqbm" browserValue="<%=sqbm%>"
                                  browserOnClick=""
                                  browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/company/DepartmentBrowser.jsp?selectedids="
                                  hasInput="true" isSingle="false" hasBrowser="true" isMustInput='1'
                                  completeUrl="/data.jsp" width="165px"
                                  browserSpanValue="">
                    </brow:browser>
                </wea:item>
                <wea:item>申请日期</wea:item>
                <wea:item>
                    <button class="calendar" type="button" name="sqrqfromReleBtn_Autogrt"
                            id="sqrqfromReleBtn_Autogrt" onclick="_gdt('sqrqfrom',
                            'sqrqfromReleSpan_Autogrt', 'undefined','undefined');">
                    </button>
                    <span id="sqrqfromReleSpan_Autogrt" name="sqrqfromReleSpan_Autogrt"><%=sqrqfrom%></span>
                    <input class="wuiDateSel" type="hidden" name="sqrqfrom" value="<%=sqrqfrom%>">
                    -&nbsp;&nbsp;
                    <button class="calendar" type="button" name="sqrqtoReleBtn_Autogrt"
                    id="sqrqtoReleBtn_Autogrt" onclick="_gdt('sqrqto',
                    'sqrqtoReleSpan_Autogrt', 'undefined','undefined');">
                    </button>
                    <span id="sqrqtoReleSpan_Autogrt" name="sqrqtoReleSpan_Autogrt"><%=sqrqto%></span>
                    <input class="wuiDateSel" type="hidden" name="sqrqto" value="<%=sqrqto%>">
                </wea:item>
             
            </wea:group>
            <wea:group context="">
                <wea:item type="toolbar">
                    <input type="button" value="<%=SystemEnv.getHtmlLabelName(30947,user.getLanguage())%>"
                           class="e8_btn_submit" onclick="onBtnSearchClick();"/>
                    <input type="button" value="<%=SystemEnv.getHtmlLabelName(31129,user.getLanguage())%>"
                           class="e8_btn_cancel" id="cancel"/>
                </wea:item>
            </wea:group>

            <wea:group context="">
                <wea:item type="toolbar">
                    <input class="e8_btn_submit" type="button" name="submit_1" onClick="onBtnSearchClick()"
                           value="<%=SystemEnv.getHtmlLabelName(197, user.getLanguage()) %>"/>
                    <span class="e8_sep_line">|</span>
                    <input class="e8_btn_cancel" type="button" name="reset" onclick="resetCondtion()"
                           value="<%=SystemEnv.getHtmlLabelName(2022, user.getLanguage()) %>"/>
                    <span class="e8_sep_line">|</span>
                    <input type="button" value="<%=SystemEnv.getHtmlLabelName(31129,user.getLanguage())%>"
                           class="e8_btn_cancel" id="cancel"/>
                </wea:item>
            </wea:group>
        </wea:layout>
    </div>
</FORM>
<%
    
    String backfields = " x.djbh,x.requestid,x.sqr,x.sqrq,x.sqbm,x.hzzfje,x.ydfkr,(select GFMC from uf_khzsjxjgyjm where id=x.gysmc) as gysmc,(select isnull(sum(bcfke),0.00) from formtable_main_265 where  re_ID = x.requestid ) as yfje ";//bcfke
    //  requestid in(select requestid from workflow_requestbase where currentnodetype >=0)and
    String fromSql = " from moc_fktzd_view  x ";//sql查询的表
    String sqlWhere = " 1=1 and x.lockstatus = 0 ";//where条件   ???待定
    
    if (!"".equals(requestname)) {
        sqlWhere += " and x.djbh like '%" + requestname + "%'";
    }
    if (!"".equals(sqr)) {
        sqlWhere += " and x.sqr = " + sqr;
    }
    if (!"".equals(sqbm)) {
        sqlWhere += " and x.sqbm = " + sqbm;
    }
    if(!"".equals(sqrqfrom)){
        sqlWhere += " and x.sqrq >='" + sqrqfrom + "' ";        
    }
    if(!"".equals(sqrqto)){
        sqlWhere += " and x.sqrq <='" + sqrqto + "' ";
     }

   // out.println("select " + backfields + fromSql + "where" + sqlWhere);
    String orderby = " x.sqrq ";//排序关键字
    String tableString = "";
    tableString = " <table tabletype=\"checkbox\" pagesize=\"" + PageIdConst.getPageSize(late_pageId, user.getUID(), PageIdConst.HRM) + "\" pageId=\"" + late_pageId + "\">" +
            "	   <sql backfields=\"" + backfields + "\" sqlform=\"" + fromSql + "\" sqlwhere=\"" + Util.toHtmlForSplitPage(sqlWhere) + "\"  sqlorderby=\"" + orderby + "\" " +
            " sqlprimarykey=\"requestid\" sqlsortway=\"asc\" sqlisdistinct=\"false\"/>" +
            "	<head>";
    tableString += "   <col width=\"10%\" text=\"流程编号\" column=\"djbh\" orderkey=\"djbh\"  linkvaluecolumn=\"requestid\" linkkey=\"requestid\" href=\"/workflow/request/ViewRequest.jsp\" target=\"_fullwindow\" />" +
            "   <col width=\"10%\" text=\"申请人\" column=\"sqr\" orderkey=\"sqr\"  transmethod=\"weaver.hrm.resource.ResourceComInfo.getLastname\" />" +
            "	<col width=\"10%\" text=\"申请日期\" column=\"sqrq\" orderkey=\"sqrq\" />" +
            "	<col width=\"12%\" text=\"申请部门\" column=\"sqbm\" orderkey=\"sqbm\"  transmethod=\"weaver.hrm.company.DepartmentComInfo.getDepartmentname\" />" +
            "	<col width=\"10%\" text=\"约定付款日\" column=\"ydfkr\" orderkey=\"ydfkr\" />" +
			"	<col width=\"10%\" text=\"供应商名称\" column=\"gysmc\" orderkey=\"gysmc\" />" +
			"	<col width=\"10%\" text=\"总付款金额\" column=\"hzzfje\" orderkey=\"hzzfje\" />" +
	    "	<col width=\"10%\" text=\"已付款金额\" column=\"yfje\" orderkey=\"yfje\" />" +
            "	</head>" +
            " </table>";
%>
<wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run" showExpExcel="false"/>
<script type="text/javascript">
    function onBtnSearchClick() {
        report.submit();
    }
    var diag_vote;
    function calOneDialog(){
	var ids = _xtable_CheckedCheckboxId()
        if (ids === '') {
            Dialog.alert('<%=SystemEnv.getHtmlLabelName(19689,user.getLanguage())%>')
            return false
        }
        var strs = new Array(); //定义一数组 
	strs = ids.split(",");
	if(strs.length > 2){
	    Dialog.alert('这个是单笔付款申请处理！')
            return false
	}

	var title = "单笔付款处理";
	var url = "/morningcore/purchase/financeSure1.jsp?dialog=1&ids=" + ids;
	diag_vote = new window.top.Dialog();
	diag_vote.currentWindow = window;
	diag_vote.Width = 700;
	diag_vote.Height = 350;
	diag_vote.Modal = true;
	diag_vote.Title = title;
	diag_vote.URL = url;
	diag_vote.isIframe=false;
	diag_vote.show();
    }
    function calMoreDialog(){
	var ids = _xtable_CheckedCheckboxId()
        if (ids === '') {
            Dialog.alert('<%=SystemEnv.getHtmlLabelName(19689,user.getLanguage())%>')
            return false
        }

	var title = "批量付款处理";
	var url = "/morningcore/purchase/financeSure2.jsp?dialog=1&ids=" + ids;
	diag_vote = new window.top.Dialog();
	diag_vote.currentWindow = window;
	diag_vote.Width = 1400;
	diag_vote.Height = 700;
	diag_vote.Modal = true;
	diag_vote.Title = title;
	diag_vote.URL = url;
	diag_vote.isIframe=false;
	diag_vote.show();
    }
	
	//解锁
	function remlocking(){
		var ids = _xtable_CheckedCheckboxId()
        if (ids === '') {
            Dialog.alert('<%=SystemEnv.getHtmlLabelName(19689,user.getLanguage())%>')
            return false
        }
         //异步修改数据库值
        var tbsj = {
            url: "/morningcore/financev1/updatestatus.jsp?version="+new Date()+"&red="+ids+"&type=1", 
            type: 'get',
            success: function (res) {
                var text = res.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
                location = location;
            }
        };
        
        jQuery.ajax(tbsj);

		
    }


    function closeDialog(){
	diag_vote.close();
    }
</script>
<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
<SCRIPT language="javascript" defer="defer" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
<script type="text/javascript" src="/js/selectDateTime_wev8.js"></script>
</BODY>
</HTML>
