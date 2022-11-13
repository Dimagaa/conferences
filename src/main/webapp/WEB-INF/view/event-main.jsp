<%--
  User: Dmytro Martyshchuk
  Date: 08/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>
<!DOCTYPE html>
<html lang="en" data-contextPath="${pageContext.request.contextPath}">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link id="contextPath" data-contextPath="${pageContext.request.contextPath}">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.14.0/css/selectize.bootstrap5.min.css"
          integrity="sha512-gPfqgXe/pl1EpOS++KmVkF5Ca6C/Kj/4K/bt+n9nt6FkkeralgnL2907thbVEmYhacdqChfgw8XTlyCh2be4+A=="
          crossorigin="anonymous"
          referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title><fmt:message key="events.text.head"/> </title>
</head>
<body class="bg-body-dark d-flex flex-column min-vh-100">
<div class="container-fluid p-0 d-flex flex-column min-vh-100">
    <!-- Navbar -->
    <jsp:include page="modules/navbar.jsp"/>
    <div class="container pb-5">
        <!-- Search -->
        <div class="row justify-content-center">
            <c:set var="servletPath" value="${requestScope['javax.servlet.forward.servlet_path']}"/>
            <c:if test='${(fn:endsWith(servletPath, "/filter"))}'>
                <c:set var="servletPath" value='${fn:substringBefore(servletPath, "/filter")}'/>
            </c:if>
            <div class="col mt-4 mb-4">
                <form class="form-control" action='<ctl:filterPath servletPath="${servletPath}"/>' method="GET">
                    <input type="submit" hidden aria-label="submit">
                    <input class="form-control autocomplete"
                           list="suggestion"
                           name="search"
                           type="text" placeholder="<fmt:message key="events.placeholder.search"/>"
                           aria-label="Search"
                           id="search">
                    <datalist class="data" id="suggestion" for="search">

                    </datalist>
                </form>
            </div>
        </div>
        <!-- page content -->
        <div class="row d-flex bg-dark flex-row min-vh-100" style="border-radius: 2rem">
            <div class="col">
                <!-- page header -->
                <div class="row mt-5 d-flex justify-content-start align-items-center">
                    <!-- Page title -->
                    <div class="col-3">
                        <p class="text-white-50 text-uppercase fs-1 fw-bolder text-center"><fmt:message key="events.text.head"/></p>
                    </div>
                    <!-- Pills nav-bar  -->
                    <div class="col-7">
                        <div class="w-100">
                            <ul class="nav nav-pills nav-fill">
                                <!-- navigation items-->
                                <c:set var="path"
                                       value=" ${requestScope['javax.servlet.forward.servlet_path'].replace(\"/filter\", \"\")}"/>
                                <li class="nav-item">
                                    <a class="nav-link ${path.trim().equals("/events") ? "active" : ""}"
                                       href="<c:url value="/events"/>"><fmt:message key="events.navLink.allEvents"/></a>
                                </li>
                                <ctl:accessRule roleRules="USER SPEAKER" role="${sessionScope.role}">
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/joined") ? "active" : ""}"
                                           href="<c:url value="/events/joined"/>"><fmt:message key="events.navLink.joined"/></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link  ${path.trim().equals("/events/passed") ? "active" : ""}"
                                           href="<c:url value="/events/passed"/>"><fmt:message key="events.navLink.passed"/></a>
                                    </li>
                                </ctl:accessRule>
                                <ctl:accessRule roleRules="SPEAKER MODERATOR" role="${sessionScope.role}">
                                    <li class="nav-item d-flex align-items-center">
                                        <a class="nav-link ${path.trim().equals("/events/developing") ? "active" : ""}"
                                           href='<c:url value="/events/developing"/>'><fmt:message key="events.navLink.developing"/>
                                            <c:if test="${sessionScope.role == 'SPEAKER'}">
                                                <span class="ms-1 badge rounded-pill badge-count text-center">${requestScope.countProposed > 0 ? requestScope.countProposed : ""}</span>
                                            </c:if>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/canceled") ? "active" : ""}"
                                           href="<c:url value="/events/canceled"/>"><fmt:message key="events.navLink.canceled"/></a>
                                    </li>
                                </ctl:accessRule>
                                <ctl:accessRule roleRules="MODERATOR" role="${sessionScope.role}">
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/create") ? "active" : ""}"
                                           href="<c:url value="/events/create"/>"><fmt:message key="events.navLink.create"/></a>
                                    </li>
                                </ctl:accessRule>

                            </ul>
                        </div>
                    </div>
                </div>

                <!-- main Content -->
                <div class="row mt-5">
                    <c:set var="path" value="${requestScope['javax.servlet.forward.servlet_path']}" scope="page"/>
                    <c:choose>
                        <c:when test='${path.equals("/events/create")}'>
                            <jsp:include page="modules/moderator/createEvent.jsp"/>
                        </c:when>
                        <c:when test='${path.equals("/events/change")}'>
                            <jsp:include page="modules/moderator/changeEvent.jsp"/>
                        </c:when>
                        <c:otherwise>
                            <!-- filter panel -->
                            <jsp:include page="modules/filterPanel.jsp"/>
                            <!-- events -->
                            <jsp:include page="modules/events.jsp"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <!-- Footer -->
    <footer class="navbar fixed-bottom bg-dark bg-gradient mt-5">
        <div class="container-fluid justify-content-start">
            <div class="col-5 navbar-text text-white-50">
                Designed and built with <a class="text-white-50" href="https://getbootstrap.com/">Bootstrap</a>
            </div>
            <div class="col  text-white-50">
                ©Dmytro Martyshchuk
            </div>
        </div>
    </footer>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8"
        crossorigin="anonymous"></script>
<script
        src="https://code.jquery.com/jquery-3.6.1.min.js"
        integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ="
        crossorigin="anonymous"></script>

<script
        src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"
        integrity="sha256-lSjKY0/srUM9BE3dPm+c4fBo1dky2v27Gdjm2uoZaL0="
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.14.0/js/standalone/selectize.min.js"
        integrity="sha512-EZjqrrnoamnlzX1VpHSu7mCYXpbVJdqeJBbpPIHdfH/AtF8Lp/jKrCzYqzKSuVjxriFkEWR0I3qoT2evvrvRmw=="
        crossorigin="anonymous"
        referrerpolicy="no-referrer"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
<script src="${pageContext.request.contextPath}/js/autocomplete.js"></script>
<script src="${pageContext.request.contextPath}/js/eventActions.js"></script>
</body>
</html>