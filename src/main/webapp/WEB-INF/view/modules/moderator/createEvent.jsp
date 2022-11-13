<%--
  User: Dmytro Martyshchuk
  Date: 18/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>

<div class="row d-flex justify-content-center align-items-center">
    <div class="card col-10 mt-5" style="border-radius: 2rem">
        <div class="card-body">

            <form action="${pageContext.request.contextPath}/events/create?action=save"
                  button-target="saveBtn"
                  method="post"
                  class="needs-validation"
                  novalidate>

                <div class="row mt-4 mb-4">
                    <div class="col-md">
                        <div class="form-floating">
                            <input type="text"
                                   name="title"
                                   class="form-control validation"
                                   for="saveBtn"
                                   id="name"
                                   placeholder="Event head"
                                   value="${sessionScope.event.name}">
                            <label class="form-label" for="name"><fmt:message
                                    key="editEvent.label.eventTitle"/> </label>
                            <div class="invalid-feedback"><fmt:message
                                    key="editEvent.invalidFeedback.eventTitle"/></div>
                        </div>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col-md">
                        <div class="form-floating">
                            <input type="datetime-local"
                                   class="form-control validation date-min"
                                   for="saveBtn"
                                   name="start"
                                   id="startTime"
                                   value="${sessionScope.event.startTime}">
                            <label for="startTime"><fmt:message key="editEvent.label.start"/></label>
                            <div class="invalid-feedback"><fmt:message key="editEvent.invalidFeedback.start"/></div>
                        </div>
                    </div>
                    <div class="col-md">
                        <div class="form-floating">
                            <div class="form-floating">
                                <input type="datetime-local"
                                       class="form-control validation"
                                       for="saveBtn"
                                       name="end"
                                       id="endTime"
                                       value="${sessionScope.event.endTime}">
                                <label for="endTime"><fmt:message key="editEvent.label.end"/></label>
                                <div class="invalid-feedback"><fmt:message key="editEvent.invalidFeedback.end"/></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col-md">
                        <div class="form-floating">
                            <input type="text"
                                   class="form-control autocomplete validation"
                                   for="saveBtn"
                                   list="places"
                                   name="place"
                                   id="place"
                                   placeholder="Place"
                                   value="${sessionScope.event.place}">
                            <label class="form-label" for="place"><fmt:message key="editEvent.label.place"/></label>
                            <div class="invalid-feedback"><fmt:message key="editEvent.invalidFeedback.place"/></div>
                            <datalist class="data" id="places" for="place">

                            </datalist>
                        </div>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col-md">
                        <div>
                            <label for="limit" class="form-label"><fmt:message
                                    key="editEvent.label.numberOfReports"/></label>
                            <input type="range"
                                   name="limit"
                                   class="form-range readable-slider"
                                   min="1" max="15" step="1"
                                   value="${sessionScope.event == null ? 1 : sessionScope.event.limit}"
                                   id="limit">
                            <div class="invalid-feedback">
                                <fmt:message key="editEvent.invalidFeedback.numberOfReports"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col-md">
                        <p class="text-center fs-5 fw-semibold">
                            <fmt:message key="editEvent.subhead.reports"/> <span id="reportsCount">${fn:length(sessionScope.reports)}</span>/<span
                                class="out-slider">${sessionScope.event == null ? 1 : sessionScope.event.limit}</span>
                        </p>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col-md">
                        <ul class="list-group" id="reports">
                            <c:forEach var="report" items="${sessionScope.reports}" varStatus="loop">
                                <li id="report_${loop.index}"
                                    class="btn btn-outline-light list-group-item list-group-item-action d-flex justify-content-between align-items-start">
                                    <div class=" me-auto w-100"
                                         onclick="changeReportModalShow(this, '#changeReport', ${loop.index})">
                                        <div class="fw-bold rep-topic">
                                                ${report.topic}
                                        </div>
                                        <div class="rep-speaker" value="${report.speakerId}">
                                            <span class="rep-speaker-name">${report.speakerName}</span>
                                            <span class="fst-italic text-black-50 rep-speaker-email">${report.speakerEmail}</span>
                                        </div>
                                    </div>
                                    <div class="">
                                        <button type="button" class="btn-close" aria-label="Delete"
                                                onclick="removeReport(${loop.index})"></button>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="row mb-4">
                    <div class="col-md d-flex justify-content-center align-items-center">
                        <div>
                            <button class="btn btn-outline-dark btn-lg" type="button" id="openReportModal"
                                    data-bs-toggle="modal"
                                    data-bs-target="#addReport">
                                <fmt:message key="editReport.button.addReport"/>
                            </button>
                        </div>
                        <div class="ms-3">
                            <button class="btn btn-outline-dark btn-lg px-5" id="saveBtn" type="submit" disabled>
                                <fmt:message key="editEvent.button.save"/>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
            <!-- Add Report Modal-->
            <div class="modal fade" id="addReport" tabindex="-1" aria-labelledby="addReportModal" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form button-target="addBtn" class="needs-validation" id="addReportForm">
                            <div class="modal-header">
                                <h5 class="modal-title"><fmt:message key="editReport.head.addReport"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="row mb-4">
                                    <div class="col-md">
                                        <label for="topic" class="text-center fs-5 w-100"><fmt:message key="editReport.label.topic"/> </label>
                                        <input type="text"
                                               class="form-control form-control-lg p-2 validation"
                                               for="addBtn"
                                               name="topic"
                                               id="topic"
                                               placeholder="Topic of report">
                                        <div class="invalid-feedback"><fmt:message key="editReport.invalidFeedback.topic"/></div>
                                    </div>
                                </div>
                                <div class="row mb-4">
                                    <div class="col-md">
                                        <label for="speaker" class="w-100 text-center fs-5 "><fmt:message key="editReport.label.speaker"/></label>
                                        <select
                                                class="form-select-lg p-2 speaker-autocomplete"
                                                for="addBtn"
                                                id="speaker"
                                                name="speaker"
                                                placeholder="<fmt:message key="filters.placeholder.typeToSearch"/>">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer justify-content-center">
                                <button type="button" class="btn btn-outline-dark btn-lg" data-bs-dismiss="modal">
                                    <fmt:message key="editReport.button.cancel"/>
                                </button>
                                <button type="button" class="btn btn-outline-dark btn-lg" id="addBtn" disabled
                                        onclick='addReport(0, "#addReportForm")'>
                                    <fmt:message key="editReport.button.addReport"/>
                                </button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
            <!-- Change Report Modal-->
            <div class="modal fade" id="changeReport" tabindex="-1" aria-labelledby="changeReportModal"
                 aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form button-target="chBtn" class="needs-validation" id="changeReportForm">
                            <input name="scope" value="session" type="hidden">
                            <input name="reportId" type="hidden">
                            <div class="modal-header">
                                <h5 class="modal-title"><fmt:message key="editReport.head.changeReport"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="row mb-4">
                                    <div class="col-md">
                                        <label for="chTopic" class="text-center fs-5 w-100"><fmt:message key="editReport.label.topic"/></label>
                                        <input type="text"
                                               class="form-control form-control-lg p-2 validation"
                                               for="chBtn"
                                               name="topic"
                                               id="chTopic"
                                               placeholder="Topic of report">
                                        <div class="invalid-feedback"><fmt:message key="editReport.invalidFeedback.topic"/></div>
                                    </div>
`                                </div>
                                <div class="row mb-4">
                                    <div class="col-md">
                                        <label for="chSpeaker" class="w-100 text-center fs-5 "><fmt:message key="editReport.label.speaker"/></label>
                                        <select
                                                class="form-select-lg p-2 speaker-autocomplete"
                                                for="chBtn"
                                                id="chSpeaker"
                                                name="speaker"
                                                placeholder="<fmt:message key="filters.placeholder.typeToSearch"/>">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer justify-content-center">
                                <button type="button" class="btn btn-outline-dark btn-lg" data-bs-dismiss="modal">
                                    <fmt:message key="editReport.button.cancel"/>
                                </button>
                                <button type="button" class="btn btn-outline-dark btn-lg" id="chBtn"
                                        onclick='updateReport(0, "#changeReportForm")'>
                                    <fmt:message key="editReport.button.changeReport"/>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>