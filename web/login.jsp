<%@ page import="az.his.DBManager" %>
<%@ page import="az.his.DBUtil" %>
<%@ page import="az.his.persist.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //    DBManager dbman = DBUtil.getDBManFromReq(request);
//    List<User> users = User.getAll(dbman);
    List<User> users = new LinkedList<User>();
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>HIS Login</title>
    <link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body class="login">
<% for (User user : users) { %>
<div class="login_user">
    <a href="login?mode=in&uid=<%=user.getId()%>"><%=user.getName()%>
    </a>
</div>
<% } %>

<div style="margin-top:100px;font-style:italic;">
    HIS v.<%=getServletConfig().getServletContext().getInitParameter("product.version")%>
</div>
</body>
</html>