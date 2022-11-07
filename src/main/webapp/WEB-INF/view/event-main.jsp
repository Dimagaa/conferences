<%--
  User: Dmytro Martyshchuk
  Date: 08/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>
<!DOCTYPE html>
<html lang="en" data-contextPath="${pageContext.request.contextPath}">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.14.0/css/selectize.bootstrap5.min.css"
          integrity="sha512-gPfqgXe/pl1EpOS++KmVkF5Ca6C/Kj/4K/bt+n9nt6FkkeralgnL2907thbVEmYhacdqChfgw8XTlyCh2be4+A=="
          crossorigin="anonymous"
          referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Profile</title>
</head>
<body class="bg-body-dark d-flex flex-column min-vh-100">
<div class="container-fluid p-0 d-flex flex-column min-vh-100">
    <!-- Navbar -->
    <jsp:include page="modules/navbar.jsp"/>
    <div class="container pb-5">
        <!-- Search -->
        <div class="row justify-content-center">
            <div class="col mt-4 mb-4">
                <form class="form-control">
                    <input class="form-control" type="text" placeholder="Search" aria-label="Search">
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
                        <p class="text-white-50 text-uppercase fs-1 fw-bolder text-center">Events</p>
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
                                       href="<c:url value="/events"/>">All Events</a>
                                </li>
                                <ctl:accessRule roleRules="USER SPEAKER" role="${sessionScope.role}">
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/joined") ? "active" : ""}" href="<c:url value="/events/joined"/>">Joined</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link  ${path.trim().equals("/events/passed") ? "active" : ""}" href="<c:url value="/events/passed"/>">Passed</a>
                                    </li>
                                </ctl:accessRule>
                                <ctl:accessRule roleRules="SPEAKER MODERATOR" role="${sessionScope.role}">
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/developing") ? "active" : ""}"
                                           href='<c:url value="/events/developing"/>'>Developing</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/canceled") ? "active" : ""}"
                                           href="<c:url value="/events/canceled"/>">Canceled</a>
                                    </li>
                                </ctl:accessRule>
                                <ctl:accessRule roleRules="MODERATOR" role="${sessionScope.role}">
                                    <li class="nav-item">
                                        <a class="nav-link ${path.trim().equals("/events/create") ? "active" : ""}"
                                           href="<c:url value="/events/create"/>">Create</a>
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