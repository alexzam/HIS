<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="colorScheme" value="${sessionScope.user.getSysParameter('ui.colorScheme').val}" />

<c:if test="${colorScheme == 'C'}">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ext/resources/css/ext-all.css"/>
</c:if>
<c:if test="${colorScheme == 'G'}">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ext/resources/css/ext-all-gray.css"/>
</c:if>
<c:if test="${colorScheme == 'A'}">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ext/resources/css/ext-all-access.css"/>
</c:if>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ext/ext-dev.js"></script>

<script>
var rootUrl="${pageContext.request.contextPath}/";
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/ext-conf.js"></script>