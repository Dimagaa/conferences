<%--
  User: Dmytro Martyshchuk
  Date: 09/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctl" uri="WEB-INF/tld/configTagLib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>
<nav class="navbar navbar-dark bg-dark bg-gradient" aria-label="Header navbar">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/events">ConferencesWeb</a>

        <div class="ms-auto d-flex">
            <div class="navbar-user me-3">
               <ctl:langSelector locale="${locale}"/>
            </div>
            <div class="navbar-user me-3">
                <div class="nav-item dropdown">
                <span class="bi bi-person nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown"
                      aria-expanded="false"></span>
                    <ul class="dropdown-menu dropdown-menu-end">

                        <li class="dropdown-header fs-5">${sessionScope.userName}</li>
                        <li class="dropdown-item"><fmt:message key="navbar.dropdown.notifications"/> </li>
                        <li class="dropdown-item"><fmt:message key="navbar.dropdown.editProfile"/></li>
                        <li class="dropdown-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                                <fmt:message key="navbar.dropdown.logout"/>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar"
                aria-controls="#navbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbar">
            <ul class="navbar-nav me-auto mb-2">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="#"><fmt:message key="navbar.link.events"/></a>
                    <a class="nav-link" aria-current="page" href="#"><fmt:message key="navbar.link.users"/></a>
                </li>
            </ul>
        </div>
    </div>
</nav>
