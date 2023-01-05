package com.conferences.services.mail;

import com.conferences.exceptions.MailException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;
import static org.apache.commons.codec.binary.Base64.*;

public class GMail {

    private final NetHttpTransport httpTransport;
    private final GsonFactory jsonFactory;


    public GMail() throws MailException {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = GsonFactory.getDefaultInstance();
        } catch (GeneralSecurityException | IOException e) {
            throw new MailException("A initial error occurs", e);
        }
    }

    public void sendMail(String address, String subject, String message) throws MailException {
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage email = new MimeMessage(session);
        try {
            email.setFrom(new InternetAddress("ConferencesAppProject@gmail.com"));
            email.addRecipient(TO, new InternetAddress(address));
            email.setSubject(subject, "UTF-8");
            email.setContent(message, "text/html; charset=utf-8");
            System.out.println(address);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageByte = buffer.toByteArray();
            String encodedEmail = encodeBase64String(rawMessageByte);
            Message msg = new Message();
            msg.setRaw(encodedEmail);
            Gmail service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials()).setApplicationName("Conferences-webapp").build();
            service.users().messages().send("me", msg).execute();
        } catch (MessagingException | IOException e) {
            throw new MailException("Cannot send mail", e);
        }

    }

    private Credential getCredentials() throws IOException {
        try (InputStream is = GMail.class.getResourceAsStream("/client_secret_123045641102-n7a5hplckv43q7e93qo1os7ebg5q5d6l.apps.googleusercontent.com.json")) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found");
            }
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(is));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND)).setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile())).setAccessType("offline").build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(-1).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("me");
        }
    }
}
