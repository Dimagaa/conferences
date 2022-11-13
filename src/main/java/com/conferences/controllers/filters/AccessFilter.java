package com.conferences.controllers.filters;


import com.conferences.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class AccessFilter implements Filter {

    private Map<String, List<User.Role>> accessRule;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        accessRule = new HashMap<>();
        accessRule.put("/events/developing", List.of(User.Role.SPEAKER, User.Role.MODERATOR));
        accessRule.put("/events/canceled", List.of(User.Role.SPEAKER, User.Role.MODERATOR));
        accessRule.put("/events/create", List.of(User.Role.MODERATOR));
        accessRule.put("/events/change", List.of(User.Role.MODERATOR));
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final HttpSession session = req.getSession();

        final String path = req.getServletPath();
        final User.Role role = (User.Role) session.getAttribute("role");

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
