package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.controllers.actions.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController  extends HttpServlet {

    protected String process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Action action = Action.getAction(req, resp);
        return action.execute();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(process(req, resp)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(process(req, resp));
    }
}
