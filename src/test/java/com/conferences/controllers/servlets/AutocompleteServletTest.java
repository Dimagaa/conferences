package com.conferences.controllers.servlets;
import com.conferences.exceptions.DaoException;
import com.conferences.model.User;
import com.conferences.services.EventService;
import com.conferences.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AutocompleteServletTest {

    private final Autocomplete servlet = spy(Autocomplete.class);
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final EventService eventService = mock(EventService.class);
    private final UserService userService = mock(UserService.class);
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws IOException {
        ServletContext servletContext = mock(ServletContext.class);
        ServletConfig servletConfig = mock(ServletConfig.class);
        when(servletContext.getAttribute("event_service")).thenReturn(eventService);
        when(servletContext.getAttribute("user_service")).thenReturn(userService);
        when(servlet.getServletConfig()).thenReturn(servletConfig);
        when(servlet.getServletContext()).thenReturn(servletContext);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn(printWriter);
    }

    @AfterEach
    public void tearDown() throws IOException {
        stringWriter.close();
        printWriter.close();
    }

    @Test
    public void testDoPostWhenTargetIsPlace() throws IOException, DaoException {
        List<String> strings = List.of("Test1", "Test2", "Test3");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        strings.stream().map(s -> Json.createObjectBuilder().add("value", s)).forEach(builder::add);
        when(req.getParameter("target")).thenReturn("place");
        when(req.getParameter("query")).thenReturn("test");
        when(eventService.getPlaces("%test%")).thenReturn(strings);
        servlet.doPost(req, resp);
        printWriter.flush();
        assertEquals(builder.build().toString(), stringWriter.toString());
    }

    @Test
    public void testDoPostWhenTargetIsSpeaker() throws DaoException, IOException {
        List<User> users = new ArrayList<>(){
            {
             add(createUser("first", 1));
             add(createUser("second", 2));
             add(createUser("third", 3));
             add(createUser("forth", 4));
            }
        };
        JsonArrayBuilder builder = Json.createArrayBuilder();
        users.stream().map(user -> Json.createObjectBuilder().add("id", user.getId())
                .add("email", user.getLogin())
                .add("name", user.getFirstName() + " " + user.getLastName()))
                .forEach(builder::add);
        when(req.getParameter("target")).thenReturn("speaker");
        when(req.getParameter("query")).thenReturn("test");
        when(userService.getAllSpeakers()).thenReturn(users);
        servlet.doPost(req, resp);
        assertEquals(builder.build().toString(), stringWriter.toString());
    }

    @Test
    public void testDoPostWhenTargetIsSearch() throws DaoException,  IOException {
        List<String> strings = List.of("Test1", "Test2", "Test3");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        strings.stream().map(s -> Json.createObjectBuilder().add("value", s)).forEach(builder::add);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("role")).thenReturn(User.Role.USER);
        when(req.getSession()).thenReturn(session);
        when(req.getParameter("target")).thenReturn("search");
        when(req.getParameter("data")).thenReturn("test");
        when(eventService.getEventHeaders("%test%", User.Role.USER)).thenReturn(strings);
        servlet.doPost(req, resp);
        printWriter.flush();
        assertEquals(builder.build().toString(), stringWriter.toString());
    }

    @Test
    public void testDoPostWhenThrowDaoException() throws DaoException, ServletException, IOException {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        doNothing().when(dispatcher).forward(req, resp);
        when(req.getContextPath()).thenReturn("test");
        when(req.getRequestDispatcher("test/error")).thenReturn(dispatcher);
        when(req.getParameter("target")).thenReturn("place");
        when(req.getParameter("query")).thenReturn("test");
        when(eventService.getPlaces("%test%")).thenThrow(DaoException.class);
        servlet.doPost(req, resp);
        verify(dispatcher, times(1)).forward(req, resp);
    }

    private User createUser(String prefix, long id) {
        User user = new User();
        user.setId(id);
        user.setFirstName(prefix + "Test");
        user.setLastName("Test");
        user.setLogin("Test@");
        return user;
    }
}
