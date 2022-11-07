<%--
  User: Dmytro Martyshchuk
  Date: 18/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>

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
                    <h5 class="card-header event-header text-center fw-bold border-0 text-black text-truncate">${event.title}</h5>
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
                                <div class="col col-6">
                                    <p>When: <fmt:formatDate type="both" dateStyle="medium" timeStyle="short"
                                                             value="${event.start}"/></p>
                                </div>
                                <div class="col text-center">
                                    <div class="text-black-50"><span
                                            class="bi bi-hourglass-split me-2"></span>${event.duration}</div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col col-6">
                                    <p>Where: ${event.place}</p>
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
            <div class="modal fade ${statusClass}" id="event-card-modal_${event.id}" tabindex="-1"
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
                                                    <button class="accordion-button collapsed report-accordion-button"
                                                            type="button"
                                                            data-bs-toggle="collapse"
                                                            data-bs-target="#collapse${report.id}"
                                                            aria-expanded="false" aria-controls="flush-collapseOne">
                                                        <span class="mb-0 fs-6 speaker-option">${report.topic}</span>
                                                        <ctl:accessRule roleRules="MODERATOR"
                                                                        role="${sessionScope.role}"
                                                                        contextRules="UNDETAILED"
                                                                        context="${report.status}">
                                                            <c:if test="${report.proposedSpeakers.size() > 0}">
                                                                <span class="bi bi-person-plus has-request-icon"
                                                                      data-bs-toggle='tooltip' data-bs-placement='left'
                                                                      data-bs-title='Has requests for a report'></span>
                                                            </c:if>
                                                        </ctl:accessRule>

                                                        <ctl:speaker name="${report.speakerName}"
                                                                     status="${report.status}"/>
                                                    </button>
                                                </div>
                                                <ctl:accessRule roleRules="MODERATOR SPEAKER"
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
                                    <ctl:accessRule roleRules="SPEAKER" role="${sessionScope.role}"
                                                    contextRules="false" context="${event.isReportCompleted()}">
                                        <div class="d-flex justify-content-center align-items-center mt-3">
                                            <button type="button" class="btn btn-md btn-outline-dark"><i
                                                    class="bi bi-plus-circle text-primary fs-5"></i> Offer report
                                            </button>
                                        </div>
                                    </ctl:accessRule>


                                </div>
                                <div class="border-top fst-italic">
                                    <div class="row mt-2">
                                        <div class="col col-6">
                                            <p>When: <fmt:formatDate type="both" dateStyle="medium" timeStyle="short"
                                                                     value="${event.start}"/></p>
                                        </div>
                                        <div class="col text-center">
                                            <p class="text-black-50">Duration: ${event.duration}</p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col col-6">
                                            <p>Where: ${event.place}</p>
                                        </div>
                                        <div class="col text-center">
                                            <p class="text-black-50">Participants: ${event.participantsCount}</p>
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
                                            Cancel Event
                                        </button>
                                        <form action="${pageContext.request.contextPath}/events/change" method="GET">
                                            <button type="submit" name="eventId" value="${event.id}"
                                                    class="btn btn-lg btn-outline-dark me-2">Change
                                            </button>
                                        </form>
                                    </ctl:accessRule>
                                    <ctl:accessRule contextRules="true" context="${event.isExpired()}">
                                        <button type="button" class="btn btn-lg btn-outline-dark "
                                                onclick='doEventAction("${actionUrl}" ,"delete", ${event.id})'>
                                            Delete
                                        </button>
                                    </ctl:accessRule>
                                </ctl:accessRule>
                                <ctl:accessRule contextRules="DEVELOPING" context="${event.status}">
                                    <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                            onclick='doEventAction("${actionUrl}", "publish", ${event.id})'
                                        ${event.isReadyToPublish() ? "" : "disabled"}>
                                        Publish
                                    </button>
                                </ctl:accessRule>
                                <ctl:accessRule contextRules="CANCELED" context="${event.status}">
                                    <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                            onclick='doEventAction("${actionUrl}", "restore", ${event.id})'>
                                        Restore
                                    </button>
                                </ctl:accessRule>
                                <ctl:accessRule contextRules="DEVELOPING CANCELED" context="${event.status}">
                                    <form action="${pageContext.request.contextPath}/events/change" method="GET">
                                        <button type="submit" name="eventId" value="${event.id}"
                                                class="btn btn-lg btn-outline-dark me-2">Change
                                        </button>
                                    </form>
                                    <button type="button" class="btn btn-lg btn-outline-dark me-2"
                                            onclick='doEventAction("${actionUrl}" ,"delete", ${event.id})'>
                                        Delete
                                    </button>
                                </ctl:accessRule>
                            </ctl:accessRule>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <!-- pagination -->
    <c:if test="${sessionScope.page.pagesCount > 0}">
        <jsp:include page="pagination.jsp"/>
    </c:if>
</div>

