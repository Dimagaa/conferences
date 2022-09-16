package com.webapp.conferences.controllers.listeners;

import com.webapp.conferences.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    private final UserService service = UserService.getInstance();
    private final Logger logger = LogManager.getLogger("Global");
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Starting Conference WebApp");

        try {
            service.findAll();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        /*
        service.addUser("test100", "test", User.ROLE.USER);
        service.addUser("test101", "test", User.ROLE.SPEAKER);
        service.addUser("test102", "test", User.ROLE.MODERATOR);
         */

        sce.getServletContext().setAttribute("init", service);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutdown application");
    }
}
