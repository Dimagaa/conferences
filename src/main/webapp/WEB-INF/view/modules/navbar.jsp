<%--
  User: Dmytro Martyshchuk
  Date: 09/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-dark bg-dark bg-gradient" aria-label="Header navbar">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/events">ConferencesWeb</a>
    <div class="navbar-user ms-auto me-3">
      <div class="nav-item dropdown">
                <span class="bi bi-person nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown"
                      aria-expanded="false"></span>
        <ul class="dropdown-menu dropdown-menu-end">

          <li class="dropdown-header fs-5">${sessionScope.userName}</li>
          <li class="dropdown-item">Notifications</li>
          <li class="dropdown-item">Edit profile</li>
          <li  class="dropdown-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
              Log Out
            </a>

          </li>
        </ul>
      </div>
    </div>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar"
            aria-controls="#navbar" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbar">
      <ul class="navbar-nav me-auto mb-2">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="#">My profile</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" aria-current="page" href="#">Events</a>
          <a class="nav-link" aria-current="page" href="#">Users</a>
          <a class="nav-link" aria-current="page" href="#">Schedule</a>
        </li>
      </ul>
    </div>
  </div>
</nav>
