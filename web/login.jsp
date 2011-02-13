<%@ page import="az.his.ejb.ContentManager" %>
<%@ page import="az.his.persist.User" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private static ContentManager cm;

    static {
        try {
            cm = (ContentManager) (new InitialContext()).lookup("java:module/ContMan");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
%>
<%
    List<User> users = cm.getAllUsers();
%>
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