<%@ page import="az.his.persist.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<User> users = User.getAll();
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>HIS Login</title>
    <link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body style="text-align:center;">
<% for (User user : users) { %>
<div class="login_user">
    <a href="login?mode=in&uid=<%=user.getId()%>"><%=user.getName()%>
    </a>
</div>
<% } %>
</body>
</html>