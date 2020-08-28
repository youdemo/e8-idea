<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.systeminfo.SystemEnv" %>
<%@ page import="weaver.general.PageIdConst" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea" %>
<%@ taglib uri="/browserTag" prefix="brow" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="dep" class="weaver.hrm.company.DepartmentComInfo" scope="page"/>
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page"/>
<HTML>
<HEAD>
    <LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
    <script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
    <script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
	<style>
		.checkbox {
			display: none
		}
	</style>
</head>
<%
    String imagefilename = "/images/hdReport_wev8.gif";
    String titlename = SystemEnv.getHtmlLabelName(20536, user.getLanguage());
    String needfav = "1";
    String needhelp = "";

    int userid = user.getUID();//获取当前登录用户的ID
	String depval = "";
	String kmval = "";
	String nbddval = "";
    String deptid = Util.null2String(request.getParameter("deptid"));
	rs.executeSql("select departmentname from hrmdepartment where id  ="+userid);
	if(rs.next()){
        depval = rs.getString("departmentname");
    }
    String kmid = Util.null2String(request.getParameter("kmid"));
	rs.executeSql("elect yskmmc from uf_yskmsj where id  ="+userid);
	if(rs.next()){
        kmval = rs.getString("yskmmc");
    }
    String nbdd = Util.null2String(request.getParameter("nbdd"));
    String year = Util.null2String(request.getParameter("year"));
	rs.executeSql("select INTERNAL_ORDER_DESC from uf_nbdd where id ="+nbdd);
	if(rs.next()){
        nbddval = rs.getString("INTERNAL_ORDER_DESC");
    }
	
	String multiIds = Util.null2String(request.getParameter("multiIds"));
	
    String late_pageId = "morningcore_sxinfo_001";

%>
<BODY>
<div id="tabDiv">
			<span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>">
				<%=SystemEnv.getHtmlLabelName(20536, user.getLanguage()) %></span>
</div>

<input type="hidden" _showCol="true" name="pageId" id="pageId" value="<%=late_pageId%>"/>
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>

<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%
    RCMenu += "{" + SystemEnv.getHtmlLabelName(527, user.getLanguage()) + ",javascript:onBtnSearchClick(),_self} ";
    RCMenuHeight += RCMenuHeightStep;
    RCMenu += "{刷新,javascript:onBtnSearchClick(),_self} ";
    RCMenuHeight += RCMenuHeightStep;
	
%>
<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<FORM id=report name=report action="" method=post>
<input type="hidden" name="multiIds" value="">
    <table id="topTitle" cellpadding="0" cellspacing="0">
        <tr>
            <td></td>
            <td class="rightSearchSpan" style="text-align:right;">
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
                <wea:item>项目</wea:item>               
                <wea:item>
                    <brow:browser viewType="0"  name="nbdd" browserValue="<%=nbdd%>"
                        browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.uf_nbdd"
                        hasInput="true"  hasBrowser = "true" isMustInput='1' isSingle="false" width="120px" linkUrl="" 
                        browserSpanValue="<%=nbddval%>"> 
                    </brow:browser>
                </wea:item>
                <wea:item>部门</wea:item>
                <wea:item>
                    <brow:browser viewType="0" id="deptid" name="deptid" browserValue="<%=deptid%>" 
                        browserUrl="/systeminfo/BrowserMain.jsp?url=/hrm/company/DepartmentBrowser.jsp?" 
                        hasInput="true" isSingle="true" hasBrowser = "true" isMustInput='1' 
                        completeUrl="/data.jsp" width="120px" 
                        linkUrl="" 
                        browserSpanValue="<%=dep.getDepartmentname(deptid)%>"> 
                    </brow:browser>
                </wea:item>     
				<wea:item>科目</wea:item>
				<wea:item>
                    <brow:browser viewType="0"  name="kmid" browserValue="<%=kmid%>"
                        browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.uf_yskm"
                        hasInput="true"  hasBrowser = "true" isMustInput='1' isSingle="false" width="120px" linkUrl="" 
                        browserSpanValue="<%=kmval%>"> 
                    </brow:browser>
                </wea:item>
                <wea:item>年份</wea:item>
                <wea:item>
                    <brow:browser name='year' viewType='0' browserValue='<%=year%>' isMustInput='1' browserSpanValue='<%=year%>' hasInput='true'
                        linkUrl=''
                        completeUrl='/data.jsp?type=178'
                        width='60%'
                        isSingle='true'
                        hasAdd='false'
                        browserUrl='/systeminfo/BrowserMain.jsp?url=/workflow/field/Workflow_FieldYearBrowser.jsp?resourceids=#id#'>
                    </brow:browser>
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
                    <input class="e8_btn_submit" type="submit" name="submit_1" onClick="onBtnSearchClick()"
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
	
    String backfields = " id,xm,(select INTERNAL_ORDER_DESC from uf_nbdd where id =xm )as xmmc,rjbm,(select departmentname from hrmdepartment where id = rjbm)as bmmc,fykm,(select yskmmc from uf_yskmsj where id = fykm) as kmmc,jtsm ,nf,hjje,[dbo].[get_sxje](id,0) as djje ,[dbo].[get_sxje](id,1) as kjje,(hjje-[dbo].[get_sxje](id,0)-[dbo].[get_sxje](id,1)) as kyje  ";
	
    //表单中的字段
    String fromSql = " from uf_yssjdr "; // sql查询的表
    String sqlWhere = "  jtsm <> '无' and zt=0 " ; // where条件 
	
	if (!"".equals(deptid)) {
        sqlWhere += " and rjbm=" + deptid;
    }
	if (!"".equals(kmid)) {
        sqlWhere += " and fykm=" + kmid;
    }
	if (!"".equals(nbdd)) {
        sqlWhere += " and xm=" + nbdd;
    }
	if (!"".equals(year)) {
        sqlWhere += " and nf=" + year;
    }	

    // out.println("select "+ backfields + fromSql +" where"+ sqlWhere);
    String orderby = " nf,id ";//排序关键字
    String tableString = "";
    tableString = " <table tabletype=\"checkbox\" pagesize=\"" + PageIdConst.getPageSize(late_pageId, user.getUID(), PageIdConst.HRM) + "\" pageId=\"" + late_pageId + "\">" +
            "	   <sql backfields=\"" + backfields + "\" sqlform=\"" + fromSql + "\" sqlwhere=\"" + Util.toHtmlForSplitPage(sqlWhere) + "\"  sqlorderby=\"" + orderby + "\" " +
            " sqlprimarykey=\"id\" sqlsortway=\"desc\" sqlisdistinct=\"false\"/>" +
            "	<head>";
    tableString += "   <col width=\"20%\"  text=\"项目名称\" column=\"xmmc\" orderkey=\"xmmc\" />" +
            "		    <col width=\"20%\" text=\"部门名称\" column=\"bmmc\" orderkey=\"bmmc\"/>" +
			"		    <col width=\"13%\" text=\"科目名称\" column=\"kmmc\" orderkey=\"kmmc\"/>" +
            "		    <col width=\"15%\" text=\"事项名称\" column=\"jtsm\" orderkey=\"jtsm\" />" +
            "		    <col width=\"15%\" text=\"事项年度\" column=\"nf\" orderkey=\"nf\" />" +
			"		    <col width=\"15%\" text=\"事项总金额\" column=\"hjje\" orderkey=\"hjje\" />" +
			"		    <col width=\"13%\" text=\"事项冻结金额\" column=\"djje\" orderkey=\"djje\" />" +
			"		    <col width=\"13%\" text=\"事项扣减金额\" column=\"kjje\" orderkey=\"kjje\" />" +
			"		    <col width=\"13%\" text=\"事项可用金额\" column=\"kyje\" orderkey=\"kyje\" />" +
            "	</head>" +
            " </table>";
%>
<wea:SplitPageTag isShowTopInfo="false" tableString="<%=tableString%>" mode="run"/>
<script type="text/javascript">
    function onBtnSearchClick () {
        report.submit();
    }
	
	
	
</script>
<SCRIPT language="javascript" src="/js/datetime_wev8.js"></script>
<SCRIPT language="javascript" defer="defer" src="/js/JSDateTime/WdatePicker_wev8.js"></script>
<script type="text/javascript" src="/js/selectDateTime_wev8.js"></script>
</BODY>
</HTML>
