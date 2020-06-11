<%--
  Created by IntelliJ IDEA.
  User: tangj
  Date: 2020/3/23
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="jh.erp.sso.ERPSSO" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>

<%
    int userid = user.getUID();
    RecordSet rs = new RecordSet();
    String workcode = "";
    String sql = "select workcode from hrmresource where id="+userid;
    rs.execute(sql);
    if(rs.next()){
        workcode = Util.null2String(rs.getString("workcode"));
    }
    ERPSSO sso = new ERPSSO();
    String url=sso.erpsso(workcode,"wtoptst","DRAPHO");
    //out.print(url);
    response.sendRedirect(url);


%>

