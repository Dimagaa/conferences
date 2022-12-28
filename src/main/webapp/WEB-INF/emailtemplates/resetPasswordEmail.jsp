<%--
  User: Dmytro Martyshchuk
  Date: 11/09/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${sessionScope.locale == null ? pageContext.response.locale : sessionScope.locale}"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="lang"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><fmt:message key="email.subject.resetPwd"/> </title>
</head>
<body>


<h1 style="text-align: center">ConferencesWeb</h1>
<h3 style="text-align: center; font-weight: bold"><fmt:message key="email.subject.resetPwd"/></h3>
<div style="border-top: solid gainsboro 0.5rem; font-weight: 530; font-size: large">
    <p><fmt:message key="email.welcome"/> ${requestScope.get("userName")},</p>
    <div><fmt:message key="email.forgotPwd.title"/></div>
    <div><fmt:message key="email.forgotPwd.message.part1"/></div>
    <div style="margin-top: 1rem"><fmt:message key="email.forgotPwd.message.part2"/></div>
    <table style="border: 0; padding: 0; height: 38px; margin: 0.5rem">
        <tbody>
        <tr>
            <td style="width:164px;padding-top:2px;border-radius:3px;text-decoration:none; font-weight:600; background: linear-gradient(189deg, rgba(37,36,36,0.79), rgb(32,34,37)); text-align: center">
                <a href="${requestScope.get("link")}"
                   style="font-weight:600;font-size:15px;text-decoration:none;color:#ffffff">
                    <fmt:message key="email.subject.resetPwd"/>
                </a>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="mt-3"><fmt:message key="email.forgotPwd.message.part3"/></div>
    <div>${requestScope.get("link")}</div>
</div>
</body>
</html>