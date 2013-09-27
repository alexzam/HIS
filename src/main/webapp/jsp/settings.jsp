<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Настройки</title>

    <jsp:include page="include/headIncludes.jsp" />

    <script type="text/javascript">
        Ext.require('alexzam.his.SettingsScreen');

        Ext.conf = {
            rootUrl: '${pageContext.request.contextPath}/',
            params: {
                colorScheme: '${colorScheme}'
            }
        };

        Ext.onReady(function () {
            Ext.create('alexzam.his.SettingsScreen');
        });
    </script>
</head>
<body></body>
</html>
