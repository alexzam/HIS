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

        Ext.require('alexzam.his.model.manage.model.Accounts');
        Ext.require('alexzam.his.AccountScreen');
        Ext.require('Ext.data.Store');

        Ext.onReady(function () {
            Ext.create('Ext.data.Store', {
                     model: 'alexzam.his.model.manage.model.Accounts',
                     storeId:'accounts',
                     data: [
                        <c:forEach items="${accounts}" var="acc">
                            {id:'${acc.getId()}', name:'${acc.getName()}',val:0},
                        </c:forEach>
                     ]
                 });

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