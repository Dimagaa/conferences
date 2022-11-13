<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>
<%@ attribute name="report" required="true" rtexprvalue="true" type="com.conferences.dto.ReportDto" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>
<ctl:accessRule roleRules="SPEAKER" role="${sessionScope.role}">
    <ctl:accessRule contextRules="UNCONFIRMED" context="${report.status}">
        <c:if test="${report.speakerId == sessionScope.userId}">
            <button type="button"
                    class="btn btn-lg btn-outline-danger ms-2 float-end"
                    aria-label="rejectReport"
                    onclick="rejectReport(${report.id})">
                <i class="bi bi-x-lg"></i>
            </button>
            <button type="button"
                    class="btn btn-lg btn-outline-success float-end"
                    aria-label="acceptReport"
                    onclick="acceptReport(${report.id})">
                <i class="bi bi-check-lg"></i>
            </button>
        </c:if>
    </ctl:accessRule>
    <ctl:accessRule contextRules="UNDETAILED" context="${report.status}">
        <c:if test="${!report.requested}">
            <button type="button" class="btn btn-outline-dark btn-lg float-end"
            onclick="proposeSpeaker(${report.id})">
            <fmt:message key="report.button.proposeYourself"/>
            <button>
        </c:if>
        <c:if test="${report.requested}">
            <button type="button"
                    class="btn btn-outline-danger float-end"
                    aria-label="Close"
                    onclick="rejectSpeaker(${report.id}, ${sessionScope.userId})">
                <fmt:message key="report.button.cancel"/>
            </button>
            <button class="btn btn-outline-success me-2 float-end" disabled>
                <fmt:message key="report.button.requested"/>
            </button>
        </c:if>
    </ctl:accessRule>
    <ctl:accessRule contextRules="CONSOLIDATED" context="${report.status}">
        <c:if test="${report.speakerId == sessionScope.userId}">
            <button class="btn btn-outline-success me-2 float-end" disabled>
            <fmt:message key="report.button.joined"/>
            </button>
        </c:if>
    </ctl:accessRule>
</ctl:accessRule>
<ctl:accessRule roleRules="MODERATOR" role="${sessionScope.role}">
    <ctl:accessRule contextRules="CONSOLIDATED" context="${report.status}">
        <i class="bi bi-check2-all fs-4 text-success float-end"
           data-bs-toggle='tooltip'
           data-bs-placement='top'
           data-bs-title='<fmt:message key="report.tooltip.consolidated"/>'></i>
    </ctl:accessRule>
    <ctl:accessRule contextRules="UNDETAILED" context="${report.status}">
        <c:if test="${report.proposedSpeakers.size() > 0}">
            <i class="bi bi-person-plus fs-4  has-request-icon"
               data-bs-toggle='tooltip'
               data-bs-placement='top'
               data-bs-title='<fmt:message key="report.tooltip.hasRequests"/>'></i>
        </c:if>
        <c:if test="${report.proposedSpeakers.size() < 1}">
            <i class="bi bi-exclamation-triangle fs-4  text-danger float-end"
               data-bs-toggle='tooltip'
               data-bs-placement='top'
               data-bs-title='<fmt:message key="report.tooltip.undetailed"/>'></i>
        </c:if>
    </ctl:accessRule>
    <ctl:accessRule contextRules="UNCONFIRMED" context="${report.status}">
        <i class='bi bi-exclamation-diamond text-warning fs-4 float-end'
           data-bs-toggle='tooltip'
           data-bs-placement='top'
           data-bs-title='<fmt:message key="report.tooltip.unconfirmed"/>'></i>
    </ctl:accessRule>
</ctl:accessRule>
