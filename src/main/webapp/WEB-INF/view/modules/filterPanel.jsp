<%--
  User: Dmytro Martyshchuk
  Date: 01/11/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="ctl" uri="WEB-INF/tld/configTagLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-3 d-flex justify-content-center bg-dark bg-gradient">
    <div class="text-white w-100">
        <c:set var="servletPath" value="${requestScope['javax.servlet.forward.servlet_path']}"/>
        <c:if test='${(fn:endsWith(servletPath, "/filter"))}'>
            <c:set var="servletPath" value='${fn:substringBefore(servletPath, "/filter")}'/>
        </c:if>
        <form action='<ctl:filterPath servletPath="${servletPath}"/>' method="GET">
            <div class="text-center text-uppercase fw-bolder mt-3 fs-5">filters</div>

            <div class="mt-3 fs-6">
                <div class="text-uppercase">Status</div>
                <div class="form-check">
                    <input class="form-check-input" name="filter" type="radio"
                           id="isNewChecked"
                           value="activeEvents" ${sessionScope.page.containsFilter("activeEvents") ? "checked" : ""}>
                    <label class="form-check-label" for="isNewChecked">Active</label>
                </div>
                <ctl:accessRule contextRules="/events" context="${servletPath}">
                    <div class="form-check">
                        <input class="form-check-input" name="filter" type="radio"
                               id="isFinished"
                               value="finishedEvents" ${sessionScope.page.containsFilter("finishedEvents") ? "checked" : ""}>
                        <label class="form-check-label" for="isFinished">Finished</label>
                    </div>
                </ctl:accessRule>
                <ctl:accessRule contextRules="/events/joined" context="${servletPath}">
                    <div class="form-check">
                        <input class="form-check-input" name="filter" type="radio"
                               id="isPu"
                               value="finishedEvents" ${sessionScope.page.containsFilter("finishedEvents") ? "checked" : ""}>
                        <label class="form-check-label" for="isFinished">Finished</label>
                    </div>
                </ctl:accessRule>
                <ctl:accessRule contextRules="/events/developing /events/canceled" context="${servletPath}">
                    <div class="form-check">
                        <input class="form-check-input" name="filter" type="radio"
                               id="isExpiredChecked"
                               value="expiredEvents" ${sessionScope.page.containsFilter("expiredEvents") ? "checked" : ""}>
                        <label class="form-check-label" for="isExpiredChecked">Expired</label>
                    </div>
                </ctl:accessRule>
            </div>
            <div class="mt-3 fs-6">
                <label class="form-label text-uppercase" for="filterBySpeaker">Speaker</label>
                <select class="form-control speaker-autocomplete w-auto" name="speaker" list="listSpeakers"
                        id="filterBySpeaker"
                        placeholder="Type to search...">
                    <option value='${sessionScope.page.getFilterValue("eventsBySpeaker")}'
                            data-data='${sessionScope.selectedSpeaker}' selected="selected"></option>
                </select>
            </div>
            <div class="mt-3 mb-4 fs-6">
                <label class="form-label text-uppercase" for="filterByPlace">Place</label>
                <select class="form-control events-place-autocomplete" name="place" id="filterByPlace"
                        placeholder="Type to search..." value=${sessionScope.page.getFilterValue("eventsByPlace")}>
                    <option value="${sessionScope.page.getFilterValue("eventsByPlace")}" selected="selected"></option>
                </select>
            </div>
            <div class="text-center text-uppercase fw-bolder mt-3 fs-5">sort by</div>
            <jsp:useBean id="sortItems" type="java.util.Map" class="java.util.HashMap"/>
            <c:set target="${sortItems}" property="time" value="Start time"/>
            <c:set target="${sortItems}" property="reports" value="Number of reports"/>
            <c:set target="${sortItems}" property="participants" value="Number of participants"/>
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
                <button type="submit" class="btn btn-outline-light px-5">Submit</button>
            </div>
            <form action='<ctl:filterPath servletPath="${servletPath}"/>/filter' method="GET">
                <div class="mt-3 mb-4 fs-6 d-flex align-content-center justify-content-center">
                    <button name="clear" class="btn btn-outline-light px-5" value="clear">Clear</button>
                </div>
            </form>

        </form>
    </div>
</div>