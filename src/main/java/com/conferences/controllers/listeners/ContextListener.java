package com.conferences.controllers.listeners;

import com.conferences.services.EventService;
import com.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    private final Logger logger = LogManager.getLogger("Global");


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Starting Conference WebApp");
        final ServletContext ctx = sce.getServletContext();
        final String dbName = (ctx.getInitParameter("db_name"));

        ctx.setAttribute("user_service", new UserService(dbName));
        ctx.setAttribute("event_service", new EventService(dbName));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutdown application");
    }
}
