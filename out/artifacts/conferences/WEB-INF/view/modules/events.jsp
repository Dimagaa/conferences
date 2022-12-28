<%--
  User: Dmytro Martyshchuk
  Date: 18/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>

<!-- filter panel -->

<div class="col">
    <div class="row mb-4">
        <!-- Insert events here-->
        <c:forEach var="event" items="${requestScope.events}">
            <c:set var="event" value="${event}" scope="request"/>
            <c:choose>
                <c:when test="${event.status == 'DEVELOPING'}"><c:set var="statusClass"
                                                                      value="event-developing"/></c:when>
                <c:when test="${event.status == 'CANCELED'}"><c:set var="statusClass" value="event-canceled"/></c:when>
                <c:otherwise><c:set var="statusClass" value="event-active"/></c:otherwise>
            </c:choose>
            <div class="col-6 mb-3 ${statusClass}">
                <div class="card ${event.isExpired() ? "event-expired" : ""}" type="button" data-bs-toggle="modal"
                     data-bs-target="#event-card-modal_${event.id}">
                    <div class="card-header event-header d-flex justify-content-between">
                        <h5 class="text-truncate fw-bold text-center">
                                ${event.title}
                        </h5>
                        <div>
                            <ctl:accessRule roleRules="SPEAKER" role="${sessionScope.role}">
                                <span class='ms-1 bi ${event.hasProposedReport() ? "badge-hasProposed" : ""}'
                                      data-bs-toggle='tooltip' data-bs-placement='top'
                                      data-bs-title='<fmt:message key="event.tooltip.hasProposed"/>>'></span>
                                <span class='ms-1 bi ${event.isReportCompleted() ? "" : "badge-uncompleted"}'
                                      data-bs-toggle='tooltip' data-bs-placement='top'
                                      data-bs-title='<fmt:message key="event.tooltip.uncompleted"/>'></span>
                            </ctl:accessRule>
                            <ctl:accessRule roleRules="MODERATOR" role="${sessionScope.role}"
                                            contextRules="DEVELOPING" context="${event.status}">
                                 <span class='ms-1 bi ${event.proposedReports.size() > 0 ? "badge-hasProposed" : ""}'
                                       data-bs-toggle='tooltip' data-bs-placement='top'
                                       data-bs-title='<fmt:message key="event.tooltip.hasProposed"/> '></span>
                                <span class='ms-1 bi ${event.isReadyToPublish() ? "badge-completed" : ""}'
                                      data-bs-toggle='tooltip' data-bs-placement='top'
                                      data-bs-title='<fmt:message key="event.tooltip.completed"/>'></span>
                            </ctl:accessRule>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="event-scroll mb-4 " style="height: 180px">
                            <div class="list-group">
                                <c:forEach var="report" items="${event.reports}">
                                    <div class="list-group-item border-0">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h6 class="mb-0"> ${report.topic}</h6>
                                        </div>
                                        <p class="mb-2 text-black-50 fst-italic">${report.speakerName}</p>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="border-top fst-italic">
                            <div class="row mt-2">
                                <div class="col col-7">
                                    <p><fmt:message key="event.text.when"/> <fmt:formatDate type="both" dateStyle="medium" timeStyle="short"
                                                             value="${event.start}"/></p>
                                </div>
                                <div class="col text-center">
                                    <div class="text-black-50"><span
                                            class="bi bi-hourglass-split me-2"></span>${event.duration}</div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col col-6">
                                    <p><fmt:message key="event.text.where"/> ${event.place}</p>
                                </div>
                                <div class="col text-center">
                                    <div class="text-black-50"><span
                                            class="bi bi-people me-2 align-content-end"></span>${event.participantsCount}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- modals -->
            <div class="modal fade ${statusClass} event-card-modal" id="event-card-modal_${event.id}" tabindex="-1"
                 aria-labelledby="event-card-modalLabel" aria-hidden="true">
                <div class="modal-dialog modal-xl modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header event-header">
                            <h5 class="modal-title" id="event-card">${event.title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="card-body">
                                <div class="event-scroll mb-4 " style="height: 30rem">
                                    <div class="accordion mt-1 ms-1 me-2" id="reportAccordion">
                                        <c:forEach var="report" items="${event.reports}">
                                            <div class="accordion-item">
                                                <div class="accordion-header" id="heading${report.id}">
                                                    <div class="accordion-button collapsed"
                                                         data-bs-toggle="collapse"
                                                         data-bs-target="#collapse${report.id}"
                                                         aria-expanded="false" aria-controls="flush-collapseOne">
                                                        <div class="d-flex justify-content-between w-100">
                                                            <div class="row p-2 ms-2 w-100">
                                                                <div class="col col-9">
                                                                    <div class="mb-0"> ${report.topic}</div>
                                                                    <div class="text-black-50 fst-italic">${report.speakerName}</div>
                                                                </div>
                                                                <div class="col col-3">
                                                                    <ctl:accessRule contextRules="DEVELOPING"
                                                                                    context="${event.status}">
                                                                        <ctl:reportAction report="${report}"/>
                                                                    </ctl:accessRule>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <ctl:accessRule roleRules="MODERATOR"
                                                                role="${sessionScope.role}"
                                                                contextRules="DEVELOPING" context="${event.status}">
                                                    <div id="collapse${report.id}" class="accordion-collapse collapse"
                                                         aria-labelledby="heading${report.id}"
                                                         data-bs-parent="#reportAcordion">
                                                        <div class="accordion-body">
                                                            <ctl:reportMenu report="${report}"/>
                                                        </div>
                                                    </div>
                                                </ctl:accessRule>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <ctl:accessRule roleRules="MODERATOR SPEAKER" role="${sessionScope.role}">
                                        <c:if test="${event.proposedReports.size() > 0}">
                                            <div class="mt-5 d-flex justify-content-center">
                                                <h6><fmt:message key="event.subhead.proposedReports"/></h6>
                                            </div>
                                            <c:forEach var="proposedRep" items="${event.proposedReports}">
                                                <c:set var="accessable"
                                                       value="${sessionScope.role != 'SPEAKER' || proposedRep.speakerId == sessionScope.userId}"/>
                                                <c:if test="${accessable}">
                                                    <div class="d-flex justify-content-between mb-2">
                                                        <div class="row p-2 ms-2 me-5 w-100 border border-opacity-50 ">
                                                            <div class="col col-10">
                                                                <div class="mb-0"> ${proposedRep.topic}</div>
                                                                <div class="text-black-50 fst-italic">${proposedRep.speakerName}</div>
                                                            </div>
                                                            <div class="col col-2">
                                                                <ctl:accessRule roleRules="MODERATOR"
                                                                                role="${sessionScope.role}">
                                                                    <button type="button"
                                                                            class="btn btn-lg btn-outline-danger ms-2 float-end"
                                                                            aria-label="Close"
                                                                            onclick="deletePropose(${proposedRep.id})">
                                                                        <i class="bi bi-x-lg"></i>
                                                                    </button>
                                                                    <button type="button"
                                                                            class="btn btn-lg btn-outline-success float-end"
                                                                            aria-label="offerReport"
                                                                            onclick="acceptReport(${proposedRep.id})">
                                                                        <i class="bi bi-check-lg"></i>
                                                                    </button>
                                                                </ctl:accessRule>
                                                                <ctl:accessRule roleRules="SPEAKER"
                                                                                role="${sessionScope.role}">
                                                                    <button type="button"
                                                                            class="btn btn-lg btn-outline-secondary ms-2 float-end"
                                                                            aria-label="Close"
                                                                            onclick="deletePropose(${proposedRep.id})">
                                                                        <i class="bi bi-x-lg"></i>
                                                                    </button>
                                                                </ctl:accessRule>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                    </ctl:accessRule>
                                    <ctl:accessRule roleRules="SPEAKER" role="${sessionScope.role}"
                                                    contextRules="false" context="${event.isReportCompleted()}">
                                        <div class="d-flex justify-content-center align-items-center mt-5 mb-5">
                                            <button type="button" class="btn btn-md btn-outline-dark offer-rep-btn"
                                                    id="offerReport" value="${event.id}">
                                                <i class="bi bi-plus-circle text-primary fs-5"></i>
                                                <fmt:message key="event.button.offerReport"/>
                                            </button>
                                        </div>
                                    </ctl:accessRule>
                                </div>
                                <div class="border-top fst-italic">
                                    <div class="row mt-2">
                                        <div class="col col-6">
                                            <p><fmt:message key="event.text.when"/> <fmt:formatDate type="both" dateStyle="medium" timeStyle="short"
                                                                     value="${event.start}"/></p>
                                        </div>
                                        <div class="col text-center">
                                            <p class="text-black-50"><fmt:message key="event.text.duration"/> ${event.duration}</p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col col-6">
                                            <p><fmt:message key="event.text.where"/> ${event.place}</p>
                                        </div>
                                        <div class="col text-center">
                                            <p class="text-black-50"><fmt:message key="event.text.participants"/> ${event.participantsCount}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="mb-3 d-flex justify-content-center btn-group-lg">
                            <c:set var="actionUrl" value="${pageContext.request.contextPath}/events/action"/>
                            <ctl:accessRule roleRules="MODERATOR" role="${sessionScope.role}">
                                <ctl:accessRule contextRules="ACTIVE" context="${event.status}">
                                    <ctl:accessRule contextRules="false" context="${event.isExpired()}">
                                        <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                                onclick='doEventAction("${actionUrl}", "cancel", ${event.id})'>
                                            <fmt:message key="event.button.cancelEvent"/>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/events/change" method="GET">
                                            <button type="submit" name="eventId" value="${event.id}"
                                                    class="btn btn-lg btn-outline-dark me-2">
                                                <fmt:message key="event.button.change"/>
                                            </button>
                                        </form>
                                    </ctl:accessRule>
                                    <ctl:accessRule contextRules="true" context="${event.isExpired()}">
                                        <button type="button" class="btn btn-lg btn-outline-dark "
                                                onclick='doEventAction("${actionUrl}" ,"delete", ${event.id})'>
                                            <fmt:message key="event.button.delete"/>
                                        </button>
                                    </ctl:accessRule>
                                </ctl:accessRule>
                                <ctl:accessRule contextRules="DEVELOPING" context="${event.status}">
                                    <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                            onclick='doEventAction("${actionUrl}", "publish", ${event.id})'
                                        ${event.isReadyToPublish() ? "" : "disabled"}>
                                        <fmt:message key="event.button.publish"/>
                                    </button>
                                </ctl:accessRule>
                                <ctl:accessRule contextRules="CANCELED" context="${event.status}">
                                    <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                            onclick='doEventAction("${actionUrl}", "restore", ${event.id})'>
                                        <fmt:message key="event.button.restore"/>
                                    </button>
                                </ctl:accessRule>
                                <ctl:accessRule contextRules="DEVELOPING CANCELED" context="${event.status}">
                                    <form action="${pageContext.request.contextPath}/events/change" method="GET">
                                        <button type="submit" name="eventId" value="${event.id}"
                                                class="btn btn-lg btn-outline-dark me-2">
                                            <fmt:message key="event.button.change"/>
                                        </button>
                                    </form>
                                    <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                            onclick='doEventAction("${actionUrl}" ,"delete", ${event.id})'>
                                        <fmt:message key="event.button.delete"/>
                                    </button>
                                </ctl:accessRule>
                            </ctl:accessRule>
                            <ctl:accessRule roleRules="USER" role="${sessionScope.role}"
                                            contextRules="ACTIVE" context="${event.status}">
                                <c:if test="${event.isJoined()}">
                                    <button type="button"
                                            class="btn btn-lg btn-outline-dark me-2"
                                            onclick="leaveEvent(${event.id}, ${sessionScope.userId})">
                                        <fmt:message key="event.button.leave"/>
                                    </button>
                                </c:if>
                                <c:if test="${!event.isJoined()}">
                                    <button type="button"
                                            class="btn btn-lg btn-outline-dark me-2"
                                            onclick="joinEvent(${event.id}, ${sessionScope.userId})">
                                        <fmt:message key="event.button.join"/>
                                    </button>
                                </c:if>
                            </ctl:accessRule>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <!-- templates -->
    <!-- Offer report template -->
    <form class="me-2 d-none w-50"
          id="offerReportForm">
        <div class="input-group ms-1">
            <input type="hidden" name="speakerId" value="${sessionScope.userId}">
            <input type="hidden" name="eventId">
            <input type="text"
                   class="form-control validation"
                   aria-label="topic"
                   name="topic"
                   id="topic"
                   placeholder="<fmt:message key="editReport.label.topic"/>">
            <button type="button" class="btn btn-lg btn-outline-success offer-rep-save-btn"
                    id="offerBtn"
                    aria-label="offerReport"
                    disabled>
                <i class="bi bi-check-lg"></i>
            </button>
            <button type="button" class="btn btn-lg btn-outline-danger  offer-rep-close-btn" aria-label="Close">
                <i class="bi bi-x-lg"></i>
            </button>
            <div class="invalid-feedback"><fmt:message key="editReport.invalidFeedback.topic"/> </div>
        </div>
    </form>

    <!-- pagination -->
    <c:if test="${sessionScope.page.pagesCount > 0}">
        <jsp:include page="pagination.jsp"/>
    </c:if>
</div>

