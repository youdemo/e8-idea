
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="weaver.general.BaseBean"%>
<%@ page import="zx.hr.sysn.PersonInfoConfirm"%>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea"%>
<%@ taglib uri="/browserTag" prefix="brow"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />

<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page" />
<%
int userid = user.getUID();
BaseBean log = new BaseBean();
String loginip = user.getLoginip();
String xm = Util.null2String(request.getParameter("xm"));
String gh = Util.null2String(request.getParameter("gh"));
String bm = Util.null2String(request.getParameter("bm"));
String zjhm = Util.null2String(request.getParameter("zjhm"));
PersonInfoConfirm pic = new PersonInfoConfirm();
log.writeLog("开始 人员信息确认");
String result = pic.infoConfirm(xm,gh,bm,zjhm,userid+"",loginip,"24476,24477,-1");
out.print(result);



%>
	

