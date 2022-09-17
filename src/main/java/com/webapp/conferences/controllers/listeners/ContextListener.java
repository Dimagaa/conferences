package com.webapp.conferences.controllers.listeners;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    private final UserService service = new UserService("mysql");
    private final Logger logger = LogManager.getLogger("Global");

    public ContextListener() throws DaoException {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Starting Conference WebApp");

        sce.getServletContext().setAttribute("init", service);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutdown application");
    }
}
