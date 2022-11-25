<%--
  User: Dmytro Martyshchuk
  Date: 15/11/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
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
    <title>Page not found</title>
</head>
<body class="bg-body-dark d-flex flex-column min-vh-100">
<div class="container-fluid p-0 d-flex flex-column min-vh-100">
    <!-- Navbar -->
    <jsp:include page="../modules/navbar.jsp"/>
    <div class="container pb-5 h-75">
        <!-- page content -->
        <div class="row d-flex bg-dark flex-row h-100  mt-3" style="border-radius: 2rem">
            <div class="col">
                <jsp:include page="${requestScope['javax.servlet.error.status_code']}.jsp"/>
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
</body>
</html>