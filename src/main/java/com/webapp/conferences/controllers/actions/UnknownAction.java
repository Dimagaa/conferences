package com.webapp.conferences.controllers.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnknownAction extends Action {
    protected UnknownAction(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public String execute() throws IOException {
        return "/conferences_war_exploded/view/error.jsp";
    }
}
