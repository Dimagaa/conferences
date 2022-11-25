package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.model.User;
import com.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * {@code HttpServlet} support {@code doGet} and {@code doPost} methods
 * Provides login functional for client.
 */
public class Login extends HttpServlet {

    final private Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String url = session.getAttribute("userId") == null ? "/WEB-INF/view/login.jsp" : "/events";
        req.getRequestDispatcher(url).forward(req, resp);
    }

    /**
     * Gets credentials from {@link HttpServletRequest}.
     * If it valid users properties will set in the {@link HttpSession},
     * otherwise client gets error message. If DaoException occurs
     * will send internal server error(500).
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
        UserService userService = (UserService) req.getServletContext().getAttribute("user_service");
        JsonObjectBuilder respBuilder = Json.createObjectBuilder();

        HttpSession session = req.getSession();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            Optional<User> opUser = Optional.ofNullable(userService.authenticatedUser(login, password));
            if(opUser.isPresent()) {
                User user =  opUser.get();
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getFirstName() + " " + user.getLastName());
                session.setAttribute("role", user.getRole());
                respBuilder.add("redirect", req.getContextPath() + "/events");
                doResponse(resp, respBuilder.build());
                return;
            }
            respBuilder.add("error", "User and password do not match!");
            doResponse(resp, respBuilder.build());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Can't validate User", e);
        }
    }

    private void doResponse(HttpServletResponse resp, JsonObject result) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        writer.print(result);
    }

}
