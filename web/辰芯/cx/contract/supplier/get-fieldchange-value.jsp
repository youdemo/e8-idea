<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="weaver.general.Util"%>
<%@page import="java.util.*"%>
<%@page import="weaver.general.BaseBean"%>
<%@page import="org.json.JSONObject"%>
<%@page import="morningcore.contract.util.ValueTransMethod"%>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%
ValueTransMethod vtm = new ValueTransMethod();
String seqno = Util.null2String(request.getParameter("seqno"));
String fieldid = Util.null2String(request.getParameter("fieldid"));
String newvalue = "";
String oldvalue = "";
String result = "";
String sql = "select newvalue,oldvalue from uf_change_detail where seqno='"+seqno+"'";
rs.executeSql(sql);
if(rs.next()){
 	newvalue = vtm.doTrans2(fieldid,Util.null2String(rs.getString("newvalue"))).replaceAll("<br>"," ").replaceAll("\n"," ").replaceAll("\r\n"," ");
 	oldvalue =  vtm.doTrans2(fieldid,Util.null2String(rs.getString("oldvalue"))).replaceAll("<br>"," ").replaceAll("\n"," ").replaceAll("\r\n"," ");
	if(newvalue.length()>300){
		newvalue = newvalue.substring(0,300);
	}
	if(oldvalue.length()>300){
		oldvalue = oldvalue.substring(0,300);
	}
	result=oldvalue+"@@@"+newvalue;
}else{
	result = "null";
}
out.print(result);
%>

