<%@ tag pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="status" required="true" rtexprvalue="true" %>

<c:if test="${sessionScope.role == 'MODERATOR'}">
    <c:if test="${status == 'UNCONFIRMED'}">
        <c:set var="statusIcon" value="<i class='bi bi-exclamation-triangle text-warning'></i>"/>
        <c:set var="tooltip" value="data-bs-toggle='tooltip' data-bs-placement='right' data-bs-title='Participate not confirmed yet'"/>
    </c:if>
</c:if>
<span class="mb-2 text-black-50 fst-italic speaker-option" ${tooltip}>${name} ${statusIcon}</span>