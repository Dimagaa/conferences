package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


@WebServlet(name = "SignUpServlet", value = "/SignUp")
public class SignUpServlet extends HttpServlet {

    private final String EMAIL = "email";
    private final String FIRST_NAME = "firstName";
    private final String LAST_NAME = "lastName";
    private final String PASSWORD1 = "password";
    private final String PASSWORD2 = "confirmPassword";
    private final UserService users = new UserService("mysql");

    public SignUpServlet() throws DaoException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/signUp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String login = request.getParameter(EMAIL);
        final String firstName = request.getParameter(FIRST_NAME);
        final String lastName = request.getParameter(LAST_NAME);
        final String password = request.getParameter(PASSWORD1);
        final String confirmPassword = request.getParameter(PASSWORD2);

        if(password.equals(confirmPassword)) {
            try {
                users.addUser(login, password, firstName, lastName, User.ROLE.USER);
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }

        request.getSession().setAttribute("login", login);
        request.getSession().setAttribute("password", password);
        try {
            request.getSession().setAttribute("role", users.getUser(login).get());
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

}
