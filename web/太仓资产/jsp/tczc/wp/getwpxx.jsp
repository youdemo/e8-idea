<%@ page import="weaver.general.Util" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<%
	String xzid = Util.null2String(request.getParameter("xzid"));
	xzid = xzid.substring(xzid.lastIndexOf("_")+1);

	String sql = "";
	JSONObject jo = new JSONObject();
	String wpid = "";
	String wpmc = "";
	String lm = "";
	String flmc = "";
	String xh = "";//型号
	String tst = "";//库存数量
	String sl = "";//税率
	String dj = "";//单价
	sql = "select id,wpmc,lm,(select flmc from uf_wpflk where id=a.lm) as flmc,xh,tst,sl,dj from uf_bgypk a where id="+xzid;
    rs.execute(sql);
    if(rs.next()){
		wpid = Util.null2String(rs.getString("id"));
		wpmc = Util.null2String(rs.getString("wpmc"));
		lm = Util.null2String(rs.getString("lm"));
		flmc = Util.null2String(rs.getString("flmc"));
		xh = Util.null2String(rs.getString("xh"));
		tst = Util.null2String(rs.getString("tst"));
		sl = Util.null2String(rs.getString("sl"));
		dj = Util.null2String(rs.getString("dj"));
	}
	jo.put("wpid",wpid);
	jo.put("wpmc",wpmc);
	jo.put("lm",lm);
	jo.put("flmc",flmc);

	jo.put("xh",xh);
	jo.put("tst",tst);
	jo.put("sl",sl);
	jo.put("dj",dj);

	out.print(jo.toString());
%>