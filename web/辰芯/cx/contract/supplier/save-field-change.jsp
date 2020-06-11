<%@ page import="weaver.general.Util" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="morningcore.contract.bean.ChangeFieldBean"%>
<%@ page import="morningcore.contract.dao.ChangeFieldDao"%>
<%@ page import="morningcore.contract.util.InsertUtil"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", -10);
%>
	
<%
	int userid = user.getUID();
	
    String customid = Util.null2String(request.getParameter("customid"));
    String fieldid = Util.null2String(request.getParameter("fieldid"));
    String seqno = Util.null2String(request.getParameter("seqno"));
	String fieldname = Util.null2String(request.getParameter("fieldname"));
	String oldvalue = Util.null2String(request.getParameter("oldvalue"));
	ChangeFieldDao cfd = new ChangeFieldDao();
	ChangeFieldBean cfb = cfd.getChangeFieldBean(fieldid);
	String showfieldname=cfb.getFieldname();
	String newvalue = Util.null2String(request.getParameter(showfieldname)).replaceAll("\n","<br>").replaceAll("\r\n","<br>");
	Map<String, String> map = new HashMap<String, String>();
	map.put("changetype","1");
	map.put("customid",customid);
	map.put("bgzd",fieldid);
	map.put("seqno",seqno);
	map.put("fieldname",fieldname);
	map.put("oldvalue",oldvalue);
	map.put("newvalue",newvalue);
	InsertUtil iu = new InsertUtil();
	int count =0;
	String sql = "select count(1) as count from  uf_change_detail  where seqno='"+seqno+"'";
	rs.executeSql(sql);
	if(rs.next()){
		count = rs.getInt("count");
	}
	if(count >0){
		iu.updateGen(map, "uf_change_detail","seqno",seqno);
	}else{
		iu.insert(map, "uf_change_detail");
	} 
	
	response.sendRedirect("/cx/contract/supplier/cx-cs-field-change.jsp?customid="+customid+"&fieldid="+fieldid+"&seqno="+seqno+"&sfxj=0&issuccess=1");

%>	