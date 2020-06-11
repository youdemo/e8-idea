<%@ page import="weaver.general.Util" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<%
	String fplx = Util.null2String(request.getParameter("fplx"));//发票类型
	String je = Util.null2String(request.getParameter("je"));// 实际报销金额
	String jxs = "0";
	String sql = "select round("+je+"/(1+sl)*sl,2) as jxs from uf_slgx where fplx='"+fplx+"'";
	rs.execute(sql);
	if(rs.next()){
		jxs = Util.null2String(rs.getString("jxs"));
	}

	out.print(jxs);
%>