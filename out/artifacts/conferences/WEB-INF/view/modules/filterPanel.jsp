<%--
  User: Dmytro Martyshchuk
  Date: 01/11/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="ctl" uri="WEB-INF/tld/configTagLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>

<div class="col-3 d-flex justify-content-center bg-dark bg-gradient">
    <div class="text-white w-100">
        <c:set var="servletPath" value="${requestScope['javax.servlet.forward.servlet_path']}"/>
        <c:if test='${(fn:endsWith(servletPath, "/filter"))}'>
            <c:set var="servletPath" value='${fn:substringBefore(servletPath, "/filter")}'/>
        </c:if>
        <form action='<ctl:filterPath servletPath="${servletPath}"/>' method="GET">
            <div class="text-center text-uppercase fw-bolder mt-3 fs-5"><fmt:message key="filters.head.filters"/></div>

            <div class="mt-3 fs-6">
                <ctl:accessRule contextRules="false" context="${servletPath == '/events/passed'}">
                    <div class="text-uppercase"><fmt:message key="filters.subhead.status"/></div>
                    <c:set var="selectedStatus" value='${sessionScope.page.getFilterValue("eventsByStatus")}'/>
                </ctl:accessRule>
                <ctl:accessRule contextRules="/events /events/joined" context="${servletPath}">
                    <div class="form-check">
                        <input class="form-check-input"
                               name="filterByStatus"
                               type="radio"
                               id="isNewChecked"
                               value="ACTIVE" ${selectedStatus == 'ACTIVE' ? "checked" : ""}>
                        <label class="form-check-label" for="isNewChecked"><fmt:message
                                key="filters.radio.active"/></label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input"
                               name="filterByStatus"
                               type="radio"
                               id="isFinished"
                               value="COMPLETED" ${selectedStatus == 'COMPLETED' ? "checked" : ""}>
                        <label class="form-check-label" for="isFinished"><fmt:message
                                key="filters.radio.finished"/></label>
                    </div>
                </ctl:accessRule>
                <ctl:accessRule roleRules="SPEAKER" role="${sessionScope.role}"
                                contextRules="/events/joined" context="${servletPath}">
                    <div class="form-check">
                        <input class="form-check-input"
                               name="filterByStatus"
                               type="radio"
                               id="isDeveloping"
                               value="finishedEvents" ${selectedStatus == 'DEVELOPING' ? "checked" : ""}>
                        <label class="form-check-label" for="isDeveloping"><fmt:message
                                key="filters.radio.developing"/></label>
                    </div>
                </ctl:accessRule>
                <ctl:accessRule contextRules="/events/developing /events/canceled" context="${servletPath}">
                    <div class="form-check">
                        <input class="form-check-input"
                               name="expiredEvents"
                               type="radio"
                               id="isExpiredChecked"
                               value="expiredEvents" ${sessionScope.page.containsFilter("expiredEvents") ? "checked" : ""}>
                        <label class="form-check-label" for="isExpiredChecked"><fmt:message
                                key="filters.radio.expired"/></label>
                    </div>
                </ctl:accessRule>
            </div>
            <div class="mt-3 fs-6">
                <label class="form-label text-uppercase" for="filterBySpeaker"><fmt:message
                        key="filters.subhead.speaker"/></label>
                <select class="form-control speaker-autocomplete w-auto"
                        name="speaker"
                        list="listSpeakers"
                        id="filterBySpeaker"
                        placeholder="<fmt:message key="filters.placeholder.typeToSearch"/>">
                    <option value='${sessionScope.page.getFilterValue("eventsBySpeaker")}'
                            data-data='${sessionScope.selectedSpeaker}' selected="selected"></option>
                </select>
            </div>
            <div class="mt-3 mb-4 fs-6">
                <label class="form-label text-uppercase" for="filterByPlace"><fmt:message
                        key="filters.subhead.place"/></label>
                <select class="form-control events-place-autocomplete"
                        name="place" id="filterByPlace"
                        placeholder="<fmt:message key="filters.placeholder.typeToSearch"/>"
                        value=${sessionScope.page.getFilterValue("eventsByPlace")}>
                    <option value="${sessionScope.page.getFilterValue("eventsByPlace")}" selected="selected"></option>
                </select>
            </div>
            <div class="text-center text-uppercase fw-bolder mt-3 fs-5"><fmt:message key="filters.head.sortBy"/></div>
            <jsp:useBean id="sortItems" type="java.util.Map" class="java.util.HashMap"/>
            <c:set target="${sortItems}" property="time"><fmt:message key="filters.option.startTime"/></c:set>
            <c:set target="${sortItems}" property="reports"><fmt:message key="filters.option.numberOfReports"/></c:set>
            <c:set target="${sortItems}" property="participants"><fmt:message
                    key="filters.option.numberOfParticipants"/></c:set>
            <div class="mb-5 mt-3 fs-6 input-group">
                <label class="form-label text-uppercase" for="sortBy"></label>
                <select class="form-control simple-select" name="sortBy" id="sortBy">
                    <ctl:renderOptions items="${sortItems}" selectedItems="${sessionScope.page.getSorters()}"/>
                </select>
                <c:set var="order" value='${sessionScope.page.getFirstSort().getValue()}'/>
                <input type="text" value='${order}' class="btn-check" name="orderBtn" id="order"
                       onclick="toggleSort(this)">
                <label class="btn  btn-outline-light ms-2" style="--bs-btn-focus-shadow-rgb: transparent" for="order"><i
                        class='bi ${order.equals("ASC") ? "bi-sort-down-alt" : "bi-sort-up-alt"}'></i></label>
            </div>
            <div class="mt-3 mb-4 fs-6 d-flex align-content-center justify-content-center">
                <button type="submit" class="btn btn-outline-light px-5"><fmt:message
                        key="filters.button.submit"/></button>
            </div>
            <form action='<ctl:filterPath servletPath="${servletPath}"/>/filter' method="GET">
                <div class="mt-3 mb-4 fs-6 d-flex align-content-center justify-content-center">
                    <button name="clear" class="btn btn-outline-light px-5" value="clear"><fmt:message
                            key="filters.button.clear"/></button>
                </div>
            </form>

        </form>
    </div>
</div>