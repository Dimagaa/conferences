<%--
  User: Dmytro Martyshchuk
  Date: 24/12/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><fmt:message key="email.subject.changedPwd"/> </title>
</head>
<body style="background: #212529 linear-gradient(180deg, rgba(255, 255, 255, 0.15), rgba(255, 255, 255, 0)) no-repeat fixed">
<div style="padding: 30px">
    <h1 style="text-align: center; color: white">ConferencesWeb</h1>
    <div style="background-color: white; font-weight: 530; font-size: large; margin: 10px 100px;padding: 10px 10px 10px;text-align: center">
        <div style="padding: 10px; margin-top: 10px"><fmt:message key="email.resetPwdSuccess.message.part1"/></div>
        <div style="padding: 10px; margin-top: 10px"><fmt:message key="email.resetPwdSuccess.message.part2"/></div>
    </div>
</div>
</body>
</html>
