package com.webapp.conferences.controllers.filters;

import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;

        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        final UserService userService = UserService.getInstance();

        final String path = req.getServletPath();

        final HttpSession session = req.getSession();

        if(session.getAttribute("login") != null && session.getAttribute("password") != null) {
            User.ROLE role = (User.ROLE) session.getAttribute("role");
            moveToMenu(req, res, role);
        } else {
            try {
                if(userService.validation(login, password)) {
                    req.getSession().setAttribute("login", login);
                    req.getSession().setAttribute("password", password);
                    req.getSession().setAttribute("role", userService.getUser(login).get().getRole());
                    moveToMenu(req, res, userService.getUser(login).get().getRole());
                } else {
                    req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, res);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void moveToMenu(HttpServletRequest req, HttpServletResponse res, User.ROLE role) throws ServletException, IOException {
        switch (role) {
            case USER:
            case SPEAKER:
            case MODERATOR:
                req.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(req, res);
                break;
        }
    }
}
