package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * <code>HttpServlet</code> support <code>doGet</code> method.
 * Allows to manage local setting
 */
public class Locale extends HttpServlet {

    private final Logger logger = LogManager.getLogger("Global");

    /**
     * Gets locale from {@link HttpServletRequest} and sets it as {@link HttpSession}
     * attribute.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("user_service");
        HttpSession session = req.getSession();
        String locale = req.getParameter("locale");
        session.setAttribute("locale", locale);
        long userId = (long) Optional.ofNullable(req.getSession().getAttribute("userId")).orElse(0L);
        if (userId == 0) {
            return;
        }
        try {
            userService.setLocale(userId, locale);
        } catch (DaoException e) {
            logger.error("A database access error occurs", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
