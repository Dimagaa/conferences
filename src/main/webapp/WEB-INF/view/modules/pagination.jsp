<%--
  User: Dmytro Martyshchuk
  Date: 14/10/2022
  E-mail: Di.mart.ap@gmail.com
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="servletPath" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<nav aria-label="Page navigation" class="mt-5">
    <ul class="pagination justify-content-center">
        <li class="page-item ${sessionScope.page.pageNumber == 1 ? "disabled" : ""}">
            <a class="page-link"
               href="${servletPath}?page=${sessionScope.page.pageNumber-1}"
               aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
        <c:forEach var="i" begin="1" end="${sessionScope.page.pagesCount}">
            <c:if test="${sessionScope.page.pageNumber == i}">
                <li class="page-item active"><a class="page-link">${i}</a></li>
            </c:if>
            <c:if test="${sessionScope.page.pageNumber != i}">
                <li class="page-item"><a class="page-link" href="${servletPath}?page=${i}">${i}</a></li>
            </c:if>
        </c:forEach>

        <li class="page-item ${sessionScope.page.pageNumber == sessionScope.page.pagesCount ? "disabled" : ""}">
            <a class="page-link" href="${servletPath}?page=${sessionScope.page.pageNumber+1}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>