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
            rootUrl: '${pageContext.request.contextPath}/'
        };
        Ext.require('alexzam.his.AccountListScreen');

        Ext.onReady(function() {
            Ext.create('alexzam.his.AccountListScreen');
        });
    </script>
</head>
<body>
</body>
</html>