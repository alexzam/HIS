<%@ page import="az.his.DBManager" %>
<%@ page import="az.his.DBUtil" %>
<%@ page import="az.his.filters.AuthFilter" %>
<%@ page import="az.his.persist.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%!
    private static String pathRoot = null;
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
%>
<%
    if (pathRoot == null)
    {
        pathRoot = request.getServletContext().getInitParameter("path.root");
    }

    DBManager dbman = DBUtil.getDBManFromReq(request);
    List<User> usersNotMe = User.getAll(dbman);
    User userMe = null;
    for (User user : usersNotMe)
    {
        if (user.getId() == AuthFilter.getUid(session))
        {
            userMe = user;
            usersNotMe.remove(user);
            break;
        }
    }
    if (userMe == null) throw new ServletException("Where is users table?!");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Казна</title>

    <link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <script type="text/javascript" src="js/ext/ext-core-dev.js"></script>
    <script type="text/javascript" src="js/ext-conf.js"></script>
    <script type="text/javascript">
        Ext.require('alexzam.his.AccountScreen');

        Ext.create('alexzam.his.AccountScreen', {
            pathRoot:'<%=pathRoot%>',
            //transStoreUrl: 'account-data',
            //catStoreUrl: 'trcategory-data'
            uid: <%=AuthFilter.getUid(session)%>,
            userRadioOptions:[
                {boxLabel:'<%=userMe.getName()%> (Я)', inputValue:'0', checked:true}
                <% for (User user : usersNotMe) { %>
                ,
                {boxLabel:'<%=user.getName()%>', inputValue:'<%=user.getId()%>'}
                <% } %>
            ] // TODO Enrich by name:actor
        });
    </script>
</head>
<body>
</body>
</html>