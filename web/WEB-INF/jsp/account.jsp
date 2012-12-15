<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
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

        Ext.onReady(function () {
            Ext.create('alexzam.his.AccountScreen', {
                rootUrl:'${pageContext.request.contextPath}/',
                uid: ${me.getId()},
                userRadioOptions:[
                    {boxLabel:'${me.getName()} (Я)', inputValue:'0', checked:true}
                    <c:forEach items="${users}" var="u">
                    ,
                    {boxLabel:'${u.getName()}', inputValue:'${u.getId()}'}
                    </c:forEach>
                ]
            });
        });
    </script>
</head>
<body>
</body>
</html>