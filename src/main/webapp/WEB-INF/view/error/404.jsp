<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>
<div class="d-flex justify-content-center align-items-center h-75 p-5">
    <div class="text-white-50 w-50 h-50 text-center">
        <div class="fs-1">404 <i class="bi bi-layout-wtf text-danger"></i></div>
        <div class="fs-5"><fmt:message key="error.404.title"/></div>
    </div>
</div>