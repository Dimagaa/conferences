package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger("test");
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        logger.debug("Session is open");
        System.out.println("Hello Servlet1");

        String login = (String) session.getAttribute("login");

        try {
            UserService userService = new UserService("mysql");
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

}