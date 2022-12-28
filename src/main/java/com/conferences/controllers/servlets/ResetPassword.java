package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.exceptions.MailException;
import com.conferences.model.User;
import com.conferences.services.UserService;
import com.conferences.services.mail.EmailJspTranslator;
import com.conferences.services.mail.GMail;
import com.google.api.services.gmail.Gmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResetPassword extends HttpServlet {

    private final Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/reset.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = (UserService) getServletContext().getAttribute("user_service");
        String token = req.getParameter("token");
        String password = req.getParameter("password");
        try {
            long userId = userService.resetPassword(token, password);
            sendSuccessMessage(userId, req);
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (NoSuchAlgorithmException | DaoException | MailException e) {
            logger.error("A internal server error occurs", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendSuccessMessage(long userId,HttpServletRequest req) throws MailException, DaoException {
        UserService userService = (UserService) getServletContext().getAttribute("user_service");
        User user = userService.getUser(userId).orElseThrow(() -> new DaoException("User not found " + userId));
        req.getSession().setAttribute("locale", user.getLocale());
        GMail gMail = new GMail();
        String languageTag = (String) req.getSession().getAttribute("locale");
        java.util.Locale locale = languageTag == null ? Locale.getDefault() : Locale.forLanguageTag(languageTag);
        ResourceBundle res = ResourceBundle.getBundle("lang", locale);
        EmailJspTranslator jspTranslator = new EmailJspTranslator(req);
        String message = jspTranslator.getMessage("/WEB-INF/emailtemplates/resetPasswordSuccess.jsp");
        gMail.sendMail(user.getLogin(), res.getString("email.subject.changedPwd"), message);
    }
}