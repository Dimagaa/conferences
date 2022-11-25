package com.conferences.controllers.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  <code>HttpServlet</code> support <code>doGet</code> method.
 *  Allows to manage local setting
 */
public class Locale extends HttpServlet {

    private  final Logger logger = LogManager.getLogger("Global");

    /**
     * Gets locale from {@link HttpServletRequest} and sets it as {@link HttpSession}
     * attribute.
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String locale = req.getParameter("locale");
        session.setAttribute("locale", locale);
        logger.info("Set locale " + locale);
    }
}
