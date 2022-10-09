package com.webapp.conferences.controllers.actions;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUpAction extends Action {

    public SignUpAction(HttpServletRequest req, HttpServletResponse resp) {
        super(req, resp);
    }
    @Override
    public String execute() {
        if(request.getMethod().equals(GET_METHOD)) {
            HttpSession session = request.getSession();
            return session.getAttribute("user") == null ? "/WEB-INF/view/signup.jsp" : "";
        }

        return doSignUp();
    }

    private String doSignUp() {
        final UserService userService = (UserService) request.getServletContext().getAttribute("user_service");

        final String email = request.getParameter("email");
        final String firstName = request.getParameter("first_name");
        final String lastName = request.getParameter("last_name");
        final String password = request.getParameter("password");

        try {
            User user = userService.createUser(email, firstName, lastName, password);
            request.getSession().setAttribute("user", user);
            return request.getContextPath() + "/events";
        } catch (DaoException e) {
            logger.error("Can't create user", e);
        }

        return "/conferences_war_exploded/view/error.jsp";
    }
}
