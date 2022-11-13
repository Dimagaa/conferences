package com.conferences.controllers.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Locale extends HttpServlet {

    private  final Logger logger = LogManager.getLogger("Global");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String locale = req.getParameter("locale");
        session.setAttribute("locale", locale);
        logger.info("Set locale " + locale);
    }
}
