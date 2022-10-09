package com.webapp.conferences.controllers.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutAction extends Action {
    public LogoutAction(HttpServletRequest req, HttpServletResponse resp) {
        super(req, resp);
    }

    @Override
    public String execute() throws IOException {
        request.getSession().removeAttribute("user");
        return "/login";
    }
}
