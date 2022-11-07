package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet
public class SignUp extends HttpServlet {

    final private Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String url =  session.getAttribute("userId") == null ? "/WEB-INF/view/signup.jsp" : "/events";
        req.getRequestDispatcher(url).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final UserService userService = (UserService) req.getServletContext().getAttribute("user_service");

        String email = req.getParameter("email");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String password = req.getParameter("password");
        String url;
        try {
            User user = userService.createUser(email, firstName, lastName, password);
            req.getSession().setAttribute("userId", user.getId());
            req.getSession().setAttribute("userName", user.getFirstName() + " " + user.getLastName());
            req.getSession().setAttribute("role", user.getRole());
            url = req.getContextPath() + "/events";
        } catch (DaoException e) {
            logger.error("Can't create user", e);
            url = req.getContextPath() + "/error";
        }

        resp.sendRedirect(url);
    }
}
