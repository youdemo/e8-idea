<%--
  Created by IntelliJ IDEA.
  User: tangj
  Date: 2020/3/23
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="net.sf.json.*"%>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.*" %>
<%@ page import="weaver.general.*" %>
<%@ page import="weaver.file.*" %>
<%@ page import="weaver.hrm.*" %>
<%@ page import="weaver.mobile.plugin.ecology.service.*" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="ps" class="weaver.mobile.plugin.ecology.service.PluginServiceImpl" scope="page" />

<%
    User user = HrmUserVarify.getUser (request , response) ;
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
    String urlpara = "MWHRSSO#"+workcode+"#"+nowTime;
    String key = Util.getEncrypt(urlpara);
    String paramstr=workcode+","+nowTime;
    String url="http://hr.maxwell-gp.com.cn:8091/maxwell/portal/index?ssotoken=8Q2cfSLvabPoT42H&paramstr="+paramstr+"&key="+key;
    //response.sendRedirect(url);


%>
<html>
<head>
    <script>
            var url = "<%=url%>";
            window.open(url,"_self");
    </script>
</head>
<body>

</body>
</html>


