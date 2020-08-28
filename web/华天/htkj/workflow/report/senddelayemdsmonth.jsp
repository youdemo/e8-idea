<%@ page import="htkj.workflow.report.SendWorkFLowDelayReportMailMonth" %><%--
  Created by IntelliJ IDEA.
  User: tangj
  Date: 2020/8/3
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    SendWorkFLowDelayReportMailMonth sf = new SendWorkFLowDelayReportMailMonth();
    sf.execute();
%>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
