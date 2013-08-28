<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Казна</title>

    <jsp:include page="include/headIncludes.jsp" />

    <script type="text/javascript">
        Ext.conf = {
            rootUrl: '${pageContext.request.contextPath}/',
            userArr: [
                {id:${me.getId()}, name:'${me.getName()}'}
                <c:forEach items="${users}" var="u">
                ,{id:${u.getId()}, name:'${u.getName()}'}
                </c:forEach>
            ]
        };
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