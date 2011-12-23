<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>HIS Login</title>
    <link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body class="login">
<%--@elvariable id="users" type="java.util.List"--%>
<c:forEach items="${users}" var="u">
    <div class="login_user">
        <a href="login?mode=in&uid=${u.getId()}">${u.getName()}</a>
    </div>
</c:forEach>

<div style="margin-top:100px;font-style:italic;">
    HIS v.<%=getServletConfig().getServletContext().getInitParameter("product.version")%>
</div>
</body>
</html>