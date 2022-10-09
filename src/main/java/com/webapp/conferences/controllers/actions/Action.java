package com.webapp.conferences.controllers.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public abstract class Action {

    protected final static Logger logger = LogManager.getLogger("Global");
    protected final String GET_METHOD = "GET";
    protected static final String POST_METHOD = "POST";

    protected final HttpServletRequest request;
    protected final HttpServletResponse response;

    protected Action(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    public static Action getAction(HttpServletRequest req, HttpServletResponse resp) {
        Optional<String> action = Optional.of(req.getServletPath());

        switch (action.orElse("unknown")) {
            case "/signup":
                logger.trace("signup action");
                return new SignUpAction(req, resp);
            case "/login":
                logger.trace("login action");
                return new LoginAction(req, resp);
            case "/":
            case "/events":
                logger.trace("events action");
                return new EventsAction(req, resp);
            case "/logout":
                logger.trace("logout event");
                return new LogoutAction(req, resp);
            default: return new UnknownAction(req, resp);
        }
    }

    public abstract String execute() throws IOException;
}
