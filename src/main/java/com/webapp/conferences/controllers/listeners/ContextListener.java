package com.webapp.conferences.controllers.listeners;

import com.webapp.conferences.dao.DaoFactory;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.services.UserService;
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
        ServletContext ctx = sce.getServletContext();
        DaoFactory daoFactory = DaoFactory.getDaoFactory(ctx.getInitParameter("db_name"));
        try {
            UserService userService = new UserService(daoFactory.getUserDao());
            ctx.setAttribute("user_service", userService);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutdown application");
    }
}
