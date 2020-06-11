<%@ page import="weaver.general.Util" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<%
    String djhs = Util.null2String(request.getParameter("djhs"));
	String type = Util.null2String(request.getParameter("type"));// 1 采购单
	String sql = "";
	String rqids = "";
	String flag = "";
	if("1".equals(type)){//采购单 formtable_main_208
		String djharr[] = djhs.split(",");
		for(String djh:djharr){
			if("".equals(djh)){
				continue;
			}
			String rqid = "";
			sql = "select max(requestid) as rqid from formtable_main_208 where erpdh='"+djh+"'";
			rs.execute(sql);
			if(rs.next()){
				rqid = Util.null2String(rs.getString("rqid"));
			}
			rqids = rqids+flag+rqid;
			flag = ",";
		}
	}else if("2".equals(type)) { //formtable_main_198
		String djharr[] = djhs.split(",");
		for (String djh : djharr) {
			if ("".equals(djh)) {
				continue;
			}
			String rqid = "";
			sql = "select max(requestid) as rqid from formtable_main_198 where erpdh='"+djhs+"'";
			rs.execute(sql);
			if (rs.next()) {
				rqid = Util.null2String(rs.getString("rqid"));
			}
			rqids = rqids + flag + rqid;
			flag = ",";
		}
	}

	out.print(rqids);
%>