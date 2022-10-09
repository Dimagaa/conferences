package com.webapp.conferences.controllers.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EventsAction extends Action {
    public EventsAction(HttpServletRequest req, HttpServletResponse resp) {
        super(req, resp);
    }

    @Override
    public String execute() {
        if(request.getMethod().equals(GET_METHOD)){
            return "/WEB-INF/view/events.jsp";
        }
        return "/WEB-INF/view/error.jsp";
    }
}
