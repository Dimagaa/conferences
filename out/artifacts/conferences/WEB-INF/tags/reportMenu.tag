<%@ tag pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="report" required="true" rtexprvalue="true" type="com.conferences.dto.ReportDto" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>
<h6 class="text-center"><fmt:message key="report.status.${report.status.name().toLowerCase()}"/></h6>
<ul class="list-group">
    <ctl:accessRule contextRules="UNDETAILED" context="${report.status}">
        <c:forEach var="speaker" items="${report.proposedSpeakers}">
            <li id="reportRequest${report.id}"
                class="list-group-item list-group-item-action d-flex justify-content-between align-items-start">
                <div class=" me-auto w-100">
                    <div class="rep-speaker" value="${speaker.id}">
                        <span class="rep-speaker-name">${speaker.firstName} ${speaker.lastName}</span>
                        <span class="fst-italic text-black-50 rep-speaker-email">${speaker.login}</span>
                    </div>
                </div>
                <button type="button"
                        class="btn btn-lg btn-outline-success ms-2 float-end"
                        aria-label="acceptReport"
                        onclick="acceptSpeaker(${report.id}, ${speaker.id})">
                    <i class="bi bi-check-lg"></i>
                </button>
                <button type="button"
                        class="btn btn-lg btn-outline-danger ms-2 float-end"
                        aria-label="rejectReport"
                        onclick="rejectSpeaker(${report.id}, ${speaker.id})">
                    <i class="bi bi-x-lg"></i>
                </button>
            </li>
        </c:forEach>
    </ctl:accessRule>
    <ctl:accessRule contextRules="CONSOLIDATED UNCONFIRMED" context="${report.status}">
        <li id="reportRequest${report.id}"
            class="list-group-item list-group-item-action d-flex justify-content-between align-items-start">
            <div class=" me-auto w-100">
                <div class="rep-speaker" value="${report.id}">
                    <span class="rep-speaker-name">${report.speakerName}</span>
                    <span class="fst-italic text-black-50 rep-speaker-email">${report.speakerEmail}</span>
                </div>
            </div>
            <div class="btn-group" role="group">
                <form id="dismissReport">
                    <input name="reportId" type="hidden" value="${report.id}">
                    <input type="hidden" name="topic" value="${report.topic}">
                    <input type="hidden" name="speaker" value="0">
                    <button type="button" class="btn btn-outline-danger"
                            onclick="updateReport(${requestScope.event.id}, '#dismissReport')">
                        <fmt:message key="report.button.dismiss"/>
                    </button>
                </form>
            </div>
        </li>
    </ctl:accessRule>
</ul>
