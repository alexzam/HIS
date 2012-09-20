<%@ page import="az.his.DBManager" %>
<%@ page import="az.his.DBUtil" %>
<%@ page import="az.his.persist.User" %>
<%@ page import="java.util.List" %>
<%@ page import="az.his.AuthUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%!
    private static String pathRoot = null;
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
        if (user.getId() == AuthUtil.getUid())
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
    <script type="text/javascript" src="js/ext/ext-dev.js"></script>
    <script type="text/javascript" src="js/ext-conf.js"></script>
    <script type="text/javascript">
        Ext.require('alexzam.his.AccountScreen');

        Ext.onReady(function ()
        {
            Ext.create('alexzam.his.AccountScreen', {
                rootUrl:'<%=pathRoot%>',
                uid: <%=AuthUtil.getUid()%>,
                userRadioOptions:[
                    {boxLabel:'<%=userMe.getName()%> (Я)', inputValue:'0', checked:true}
                    <% for (User user : usersNotMe) { %>
                    ,
                    {boxLabel:'<%=user.getName()%>', inputValue:'<%=user.getId()%>'}
                    <% } %>
                ]
            });
        });
    </script>
</head>
<body>
</body>
</html>