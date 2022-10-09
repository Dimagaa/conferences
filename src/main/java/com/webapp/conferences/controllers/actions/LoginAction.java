package com.webapp.conferences.controllers.actions;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class LoginAction extends Action{

    public LoginAction(HttpServletRequest req, HttpServletResponse resp) {
        super(req, resp);
    }

    @Override
    public String execute() {
        logger.trace("Do login action");
        if(request.getMethod().equals(GET_METHOD)) {
            HttpSession session = request.getSession();
            return session.getAttribute("user") == null ? "/WEB-INF/view/login.jsp" : "";
        }
        return doLogin();
    }

    private String doLogin() {
        final UserService userService = (UserService) request.getServletContext().getAttribute("user_service");
        final HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try {
            if(userService.validate(login, password)) {
                User user = userService.getUser(login).orElseThrow(() -> new DaoException("User not found: " + login));
                session.setAttribute("user", user);
                return request.getContextPath() + "/events";
            }
        } catch (DaoException e) {
            logger.error("Can't validate User", e);
        }
        return "/WEB-INF/view/error.jsp";
    }
}
