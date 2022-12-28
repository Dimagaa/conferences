<%--
  User: Dmytro Martyshchuk
  Date: 17/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<input class="autocompleteItemId" type="hidden" name="itemId"/>
<c:forEach items="${requestScope.options}" var="option">
  <option value="${option}"></option>
</c:forEach>