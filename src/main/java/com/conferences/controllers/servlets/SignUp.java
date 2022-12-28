package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.model.User;
import com.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * {@code HttpServlet} supports {@code doGet} {@code doPost} methods
 * Provides sig up functional
 */
public class SignUp extends HttpServlet {

    final private Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String url =  session.getAttribute("userId") == null ? "/WEB-INF/view/signup.jsp" : "/events";
        req.getRequestDispatcher(url).forward(req, resp);
    }


    /**
     * Gets and hands parameters from {@link HttpServletRequest}.
     * If {@link DaoException} occurred send internal server error(500)
     *
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws IOException  if an input or output error occurs while the servlet is handling the HTTP request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final UserService userService = (UserService) req.getServletContext().getAttribute("user_service");

        String email = req.getParameter("email");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String password = req.getParameter("password");
        String locale = (String) req.getSession().getAttribute("locale");
        locale = locale == null ? "en_US" : locale;
        try {
            User user = userService.createUser(email, firstName, lastName, password, locale);
            req.getSession().setAttribute("userId", user.getId());
            req.getSession().setAttribute("userName", user.getFirstName() + " " + user.getLastName());
            req.getSession().setAttribute("role", user.getRole());
            resp.sendRedirect(req.getContextPath() + "/events");
        } catch (DaoException e) {
            logger.error("Can't create user", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
