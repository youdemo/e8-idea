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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String nowTime = sdf.format(new Date());
    String urlpara = "DPLXSSO#"+workcode+"#"+nowTime;
    String key = Util.getEncrypt(urlpara);
    String paramstr=workcode+","+nowTime;
    String url="https://cloud.drapho.com/custom-sso-token/tokensso/baselogin?paramstr="+paramstr+"&key="+key;
    response.sendRedirect(url);


%>

