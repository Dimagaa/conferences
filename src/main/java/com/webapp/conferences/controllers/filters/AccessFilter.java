package com.webapp.conferences.controllers.filters;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static com.webapp.conferences.model.User.*;
import static com.webapp.conferences.model.User.Role.*;

public class AccessFilter implements Filter {

    private Map<String, List<Role>> accessRule;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        accessRule = new HashMap<>();
        accessRule.put("/events/developing", List.of(SPEAKER, MODERATOR));
        accessRule.put("/events/canceled", List.of(SPEAKER, MODERATOR));
        accessRule.put("/events/create", List.of(MODERATOR));
        accessRule.put("/events/change", List.of(MODERATOR));
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final HttpSession session = req.getSession();

        final String path = req.getServletPath();
        final Role role = (Role) session.getAttribute("role");

        if(accessRule.containsKey(path) && !accessRule.get(path).contains(role)) {
            req.getRequestDispatcher("/").forward(req, res);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
