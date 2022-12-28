package com.conferences.services.mail;

import com.conferences.exceptions.MailException;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EmailJspTranslator extends HttpServlet {
    private final ServletRequest request;
    private final ServletResponse response;

    public EmailJspTranslator(HttpServletRequest req) {
        request = req;
        response = new EmailServletResponse();
    }

    public String getMessage(String path) throws MailException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new MailException("A JSP translate error occurs", e);
        }
        return response.toString();
    }

}
