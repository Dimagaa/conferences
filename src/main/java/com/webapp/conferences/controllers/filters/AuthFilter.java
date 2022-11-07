package com.webapp.conferences.controllers.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter
public class AuthFilter implements Filter {

    private List<String> excludedUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String config = filterConfig.getInitParameter("excludedUrls");
        if(config != null) {
            excludedUrls = Arrays.asList(config.split(","));
        }
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final HttpSession session = req.getSession();

        final String path = req.getServletPath();
        final Long userId = (Long) session.getAttribute("userId");

        if(!excludedUrls.contains(path)) {
            if(userId == null) {
                req.getRequestDispatcher("/login").forward(req, res);
                return;
            }
        }
        filterChain.doFilter(req, res);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }


}
