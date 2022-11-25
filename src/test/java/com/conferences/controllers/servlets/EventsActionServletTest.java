package com.conferences.controllers.servlets;

import com.conferences.exceptions.DaoException;
import com.conferences.model.Event;
import com.conferences.model.Report;
import com.conferences.services.EventService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventsActionServletTest {

    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final ServletConfig servletConfig = mock(ServletConfig.class);
    private final ServletContext servletContext = mock(ServletContext.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    private final EventService eventService = mock(EventService.class);
    private final EventsAction servlet = spy(EventsAction.class);
    private final String contextPath = "stub";

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        when(servlet.getServletConfig()).thenReturn(servletConfig);
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("event_service")).thenReturn(eventService);
        when(req.getParameter("eventId")).thenReturn("1");
        when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(req, resp);
        when(req.getContextPath()).thenReturn(contextPath);
    }

    @Test
    public void testDoGet() throws ServletException, IOException, DaoException {
        when(eventService.getPreparedReports(anyLong())).thenReturn(new ArrayList<>());
        when(eventService.getEvent(1)).thenReturn(Optional.of(new Event()));
        servlet.doGet(req, resp);
        verify(req).setAttribute(eq("event"), any());
        verify(req).setAttribute(eq("reports"), anyCollection());
        verify(req).getRequestDispatcher("/WEB-INF/view/event-main.jsp");
    }

    @Test
    public void testDoGetWithDaoException() throws DaoException, ServletException, IOException {
        when(eventService.getPreparedReports(anyLong())).thenThrow(DaoException.class);
        servlet.doGet(req, resp);
        verify(req).getRequestDispatcher(contextPath + "/error");
    }

    @Test
    public void testSaveAction() throws DaoException, IOException {
        String start = "2022-11-15T21:30";
        String end = "2022-11-15T22:30";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        when(req.getParameter("action")).thenReturn("save");
        when(req.getParameter("eventId")).thenReturn("1");
        when(req.getParameter("title")).thenReturn("title");
        when(req.getParameter("place")).thenReturn("place");
        when(req.getParameter("limit")).thenReturn("1");
        when(req.getParameter("status")).thenReturn("DEVELOPING");
        when(req.getParameter("start")).thenReturn(start);
        when(req.getParameter("end")).thenReturn(end);
        doAnswer(inv -> {
            Event event = inv.getArgument(0);
            assertEquals(1, event.getId());
            assertEquals("title", event.getName());
            assertEquals("place", event.getPlace());
            assertEquals(1, event.getLimit());
            assertEquals(Event.Status.DEVELOPING, event.getStatus());
            assertEquals(formatter.parse(start), event.getStartTime());
            assertEquals(formatter.parse(end), event.getEndTime());
            return null;
        }).when(eventService).updateEvent(any(Event.class));
        servlet.doPost(req, resp);
    }

    @Test
    public void testSaveActionWhenInvalidDate() throws IOException {
        when(req.getParameter("action")).thenReturn("save");
        when(req.getParameter("start")).thenReturn("InvalidDate");
        servlet.doPost(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDeleteAction() throws DaoException, IOException {
        when(req.getParameter("action")).thenReturn("delete");
        when(req.getParameter("data")).thenReturn("1");
        servlet.doPost(req, resp);
        verify(eventService).delete(anyLong());
    }

    @Test
    public void updateStatusWhenActionIsCancel() throws IOException, DaoException {
        when(req.getParameter("action")).thenReturn("cancel");
        when(req.getParameter("data")).thenReturn("1");
        servlet.doPost(req, resp);
        verify(eventService).updateStatus(1, Event.Status.CANCELED);
    }

    @Test
    public void updateStatusWhenActionIsRestore() throws IOException, DaoException {
        when(req.getParameter("action")).thenReturn("restore");
        when(req.getParameter("data")).thenReturn("1");
        servlet.doPost(req, resp);
        verify(eventService).updateStatus(1, Event.Status.ACTIVE);
    }

    @Test
    public void addReportTest() throws DaoException, IOException {
        when(req.getParameter("action")).thenReturn("addReport");
        when(req.getParameter("data")).thenReturn("1");
        when(req.getParameter("topic")).thenReturn("topic");
        when(req.getParameter("speaker")).thenReturn("");
        doAnswer(inv -> {
                    Report report = inv.getArgument(0);
                    assertEquals("topic", report.getTopic());
                    assertEquals(1, report.getEventId());
                    assertEquals(Report.Status.UNDETAILED, report.getStatus());
                    return null;
                }).when(eventService).addReport(any(Report.class));
        servlet.doPost(req, resp);
    }
}
