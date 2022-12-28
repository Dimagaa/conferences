package com.conferences.controllers.servlets;

import com.conferences.services.EventService;
import com.conferences.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class RecoverPasswordServletTest {
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
    private final ServletConfig servletConfig = mock(ServletConfig.class);
    private final ServletContext servletContext = mock(ServletContext.class);

    private final EventService eventService = mock(EventService.class);
    private final UserService userService = mock(UserService.class);
    private final ForgotPassword servlet = spy(ForgotPassword.class);

    @BeforeEach
    public void setUp() {
        when(servlet.getServletConfig()).thenReturn(servletConfig);
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(req.getSession()).thenReturn(session);

        String contextPath = "stub";
        when(req.getContextPath()).thenReturn(contextPath);
        when(servletContext.getAttribute("event_service")).thenReturn(eventService);
        when(servletContext.getAttribute("user_service")).thenReturn(userService);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGetMethod() throws ServletException, IOException {
        servlet.doGet(req, resp);
        verify(req).getRequestDispatcher(anyString());
    }

    @Test
    public void testDoPostMethod() throws IOException {
        when(req.getParameter("email")).thenReturn("Di.mart.ap@gmail.com");
        servlet.doPost(req, resp);
    }

}
