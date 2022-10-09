package com.webapp.conferences.controllers.filters;

import com.webapp.conferences.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthFilter implements Filter {

    private List<String> excludedUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludedUrls = Arrays.asList(filterConfig.getInitParameter("excludedUrls").split(","));
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;
        final HttpSession session = req.getSession();

        final String path = req.getServletPath();
        final User user = (User) session.getAttribute("user");

        if(!excludedUrls.contains(path)) {
            if(user == null) {
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
