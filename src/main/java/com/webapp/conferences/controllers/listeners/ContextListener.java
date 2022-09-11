package com.webapp.conferences.controllers.listeners;

import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    private final UserService service = UserService.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        service.addUser("test100", "test", User.ROLE.USER);
        service.addUser("test101", "test", User.ROLE.SPEAKER);
        service.addUser("test102", "test", User.ROLE.MODERATOR);

        sce.getServletContext().setAttribute("init", service);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
