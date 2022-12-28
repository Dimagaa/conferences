<%--
  User: Dmytro Martyshchuk
  Date: 11/09/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctl" uri="/WEB-INF/tld/confTagLib.tld" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link id="contextPath" data-contextPath="${pageContext.request.contextPath}">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">
    <title><fmt:message key="forgotPwd.text.head"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/success-checkmark.css">
</head>
<body>
<section class="vh-100">
    <div class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card bg-dark bg-gradient text-white shadow-lg" style="border-radius: 2rem">
                    <div class="cart-body p-5 text-center">
                        <h2 class="fw-bold bb-2"><fmt:message key="forgotPwd.text.head"/></h2>
                        <p class="text-white-50 mb-2 fs-5"><fmt:message key="forgotPwd.text.subhead"/></p>
                        <form class="needs-validation log" button-target="login-btn" novalidate>
                            <div class="input-group col-12 mb-4">
                                <input type="email"
                                       name="email"
                                       class="form-control form-control-lg validation"
                                       for="login-btn"
                                       id="typeEmailX"
                                       placeholder="<fmt:message key="login.placeholder.login"/>"
                                       aria-label="Email address">
                                <div class="invalid-feedback fs-5"><fmt:message
                                        key="login.invalidFeedback.login"/></div>
                            </div>
                            <button type="button" class="btn btn-outline-light btn-lg px-5 mt-3" id="login-btn"
                                    onclick="recoverAccess()" disabled>
                                <fmt:message key="forgotPwd.button"/>
                            </button>
                            <div class="alert-success d-none">
                                <div class="checkmark-wrapper">
                                    <span class="checkmark"></span>
                                </div>
                                <div class="message-success mt-2 fs-6" style="display:none">
                                    <fmt:message key="forgotPwd.text.successMsg"/>
                                </div>
                            </div>

                        </form>
                    </div>
                    <div class="d-flex justify-content-center mb-4 text-white-50 fs-5">
                        <ctl:langSelector locale="${locale}"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8"
        crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</body>
</html>