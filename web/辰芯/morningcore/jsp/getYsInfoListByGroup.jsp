<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="weaver.systeminfo.SystemEnv" %>
<%@ page import="weaver.general.PageIdConst" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea" %>
<%@ taglib uri="/browserTag" prefix="brow" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="dep" class="weaver.hrm.company.DepartmentComInfo" scope="page"/>
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

    int userid = user.getUID();//获取当前登录用户的ID
    String xmmcid = Util.null2String(request.getParameter("xmmcid"));
    String xmmc = "";
    String deptid = Util.null2String(request.getParameter("deptid"));
    String kmbh = Util.null2String(request.getParameter("kmbh"));
    String year = Util.null2String(request.getParameter("year"));
    String tyfkkm = Util.null2String(request.getParameter("tyfkkm"));
    String sql = "select INTERNAL_ORDER_DESC from uf_nbdd where id = " + xmmcid;
    rs.executeSql(sql);
    if (rs.next()) {
        xmmc = Util.null2String(rs.getString("INTERNAL_ORDER_DESC"));
    }
    String late_pageId = "morningcore_reportYs_listbygroup002";

    //权限限制 1.系统管理员可见所有 2.部门经理可见部门 3.项目经理可见项目
    String str = "";
    int ismanager = 0;
    rs.executeSql("select count(1) as ismanager from HrmRoleMembers where roleid=2 and resourceid ="+userid);
    if(rs.next()){
        ismanager = rs.getInt("ismanager");
    }
    if(ismanager<=0 && userid !=372){
        str = " and (rjbm in (select distinct bm from Matrixtable_1002 where bmjl="+userid+") or xm in (select id from uf_nbdd where xmjl="+userid+")) ";
    }
%>
<BODY>
<jsp:include page="/systeminfo/commonTabHead.jsp">
    <jsp:param name="mouldID" value="workflow"/>
    //指定图标id
    <jsp:param name="navName" value="统一费控预算数据总览"/>
    //指定显示的名称
</jsp:include>
<div id="tabDiv">
            <span class="toggleLeft" id="toggleLeft" title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>">
                <%=SystemEnv.getHtmlLabelName(20536, user.getLanguage()) %></span>
</div>
<input type="hidden" _showCol="false" name="pageId" id="pageId" value="<%=late_pageId%>"/>
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>

<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%
    RCMenu += "{" + SystemEnv.getHtmlLabelName(527, user.getLanguage()) + ",javascript:onBtnSearchClick(),_self} ";
    RCMenuHeight += RCMenuHeightStep;
    // RCMenu += "{刷新,javascript:onBtnSearchClick(),_self} ";
    // RCMenuHeight += RCMenuHeightStep;
%>
<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<FORM id=report name=report action="" method=post>
    <table id="topTitle" cellpadding="0" cellspacing="0">
        <tr>
            <td></td>
            <td class="rightSearchSpan" style="text-align:right;">
                <!--input type=button class="e8_btn_top" onclick="openDialog();" value=""-->

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
                <wea:item>项目名称</wea:item>               
                <wea:item>
                    <brow:browser viewType="0"  name="xmmcid" browserValue="<%=xmmcid%>"
                        browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.uf_nbdd"
                        hasInput="true"  hasBrowser = "true" isMustInput='1' isSingle="false" width="120px" linkUrl="" 
                        browserSpanValue="<%=xmmc%>"> 
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
    String backfields = " t.*,(select isnull(sum(isnull(tzje,0.00)),0.00) as hjje from uf_ystzsj where SUBSTRING(tzrq,0,5)=t.nf and zcxm=t.xm and kmmc=t.tyfkkm and fybm=t.rjbm) as tzje ";
    //表单中的字段
    String fromSql = " from get_YsInfoByGroup_view t"; // sql查询的表
    String sqlWhere = " 1=1 "+str; // where条件
    if (!"".equals(xmmcid)) {
        sqlWhere += " and xm=" + xmmcid;
    }
    if (!"".equals(deptid)) {
        sqlWhere += " and rjbm=" + deptid;
    }
    if (!"".equals(year)) {
        sqlWhere += " and nf=" + year;
    }
    //out.println("select "+ backfields + fromSql +" where"+ sqlWhere);
    String orderby = " rno ";//排序关键字
    String tableString = "";

    tableString = " <table tabletype=\"checkbox\" pagesize=\"" + PageIdConst.getPageSize(late_pageId, user.getUID(), PageIdConst.HRM) + "\" pageId=\"" + late_pageId + "\">" +
            "      <sql backfields=\"" + backfields + "\" sqlform=\"" + fromSql + "\" sqlwhere=\"" + Util.toHtmlForSplitPage(sqlWhere) + "\"  sqlorderby=\"" + orderby + "\" " +
            " sqlprimarykey=\"rno\" sqlsortway=\"asc\" sqlisdistinct=\"false\"/>" +
            "   <head>";
    tableString += "           <col width=\"15%\"  text=\"项目名称\" column=\"xmmc\" orderkey=\"xmmc\" />" +
            "           <col width=\"12%\" text=\"部门名称\" column=\"bmmc\" orderkey=\"bmmc\" />" +
            "           <col width=\"13%\" text=\"统一预算科目\" column=\"tyfkmc\" orderkey=\"tyfkmc\" />" +
            "           <col width=\"10%\" text=\"预算年度\" column=\"nf\" orderkey=\"nf\" />" +
            "           <col width=\"10%\" text=\"总预算\" column=\"zgys\" orderkey=\"zgys\" />" +
            "           <col width=\"10%\" text=\"冻结预算\" column=\"djys\" orderkey=\"djys\" />" +
            "           <col width=\"10%\" text=\"扣减预算\" column=\"ykys\" orderkey=\"ykys\" />" +
            "           <col width=\"10%\" text=\"调整金额\" column=\"tzje\" orderkey=\"tzje\" />" +
            "           <col width=\"10%\" text=\"可用预算\" column=\"kyys\" orderkey=\"kyys\" />" +
            "   </head>" +
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
