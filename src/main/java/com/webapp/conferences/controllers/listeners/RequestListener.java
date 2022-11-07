package com.webapp.conferences.controllers.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class RequestListener implements ServletRequestListener {

    private final Logger logger = LogManager.getLogger("Global");

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
        logger.info(String.format("Initial request: %s : %s", req.getServletPath(), req.getMethod()));

    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }
}
