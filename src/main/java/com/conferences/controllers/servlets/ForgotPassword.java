package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.exceptions.MailException;
import com.conferences.model.User;
import com.conferences.services.UserService;
import com.conferences.services.mail.EmailJspTranslator;
import com.conferences.services.mail.GMail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ForgotPassword extends HttpServlet {

    private final Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/forgot.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("user_service");
        String address = req.getParameter("email");
        try {
            Optional<User> user = userService.getUser(address);
            if(user.isEmpty()) {
                return;
            }

            String token = userService.createPasswordResetToken(user.get().getId());
            String baseUrl = req.getRequestURL().toString().replace(req.getRequestURI(), "") + req.getContextPath();

            req.setAttribute("userName", user.get().getFirstName());
            req.setAttribute("link", String.format("%s/reset?token=%s", baseUrl, token));
            System.out.println("here");
            sendMessage(address, req);
        } catch (MailException | NoSuchAlgorithmException | InvalidKeySpecException | DaoException e) {
            logger.error("Unable send message", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendMessage(String email, HttpServletRequest req) throws MailException {
        GMail service = new GMail();
        String languageTag = (String) req.getSession().getAttribute("locale");
        Locale locale = languageTag == null ? Locale.getDefault() : Locale.forLanguageTag(languageTag);
        ResourceBundle resources = ResourceBundle.getBundle("lang", locale);
        EmailJspTranslator translator = new EmailJspTranslator(req);
        System.out.println(email);
        System.out.println("///");
        String message = translator.getMessage("/WEB-INF/emailtemplates/resetPasswordEmail.jsp");
        service.sendMail(email, resources.getString("email.subject.resetPwd"), message);
    }
}
