package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.dao.DAOException;
import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("login");

        UserService userService = UserService.getInstance();

        try {
            User user = userService.getUser(login).orElseThrow(DAOException::new);

        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

    }

}