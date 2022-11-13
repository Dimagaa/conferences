<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="locale" rtexprvalue="true" %>

<div class="nav-item dropdown">
    <span class="bi globe-lang nav-link dropdown-toggle"
          role="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"></span>
    <ul class="dropdown-menu dropdown-menu-end">
        <li class="dropdown-item lang-option " value="en-GB">English<span
                class="bi ${locale == "en-GB" ? "bi-check2" : ""}"></span></li>
        <li class="dropdown-item lang-option" value="uk-UA">Українська<span
                class="bi ${locale == "uk-UA" ? "bi-check2" : ""}"></span></li>
    </ul>
</div>