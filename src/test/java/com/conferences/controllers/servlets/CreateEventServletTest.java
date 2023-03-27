package com.conferences.controllers.servlets;

import com.conferences.dto.ReportDto;
import com.conferences.exceptions.DaoException;
import com.conferences.model.Event;
import com.conferences.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class CreateEventServletTest {

    private final CreateEvent servlet = spy(CreateEvent.class);
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
    private final ServletContext servletContext = mock(ServletContext.class);
    private final EventService eventService = mock(EventService.class);

    @BeforeEach
    public void setUp() {
        when(req.getSession()).thenReturn(session);
        when(req.getContextPath()).thenReturn("stub");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(req.getRequestDispatcher("/WEB-INF/view/event-main.jsp")).thenReturn(dispatcher);
        servlet.doGet(req, resp);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void testAddActionWhenReportTopicIsNull() throws IOException {
        when(req.getParameter("action")).thenReturn("addReport");
        servlet.doPost(req, resp);
        verify(resp).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testAddActionWhenReportIsFirstWithOutSpeaker() throws IOException {
        String stub = "stub";
        when(req.getParameter("action")).thenReturn("addReport");
        when(req.getParameter("topic")).thenReturn(stub);
        doAnswer(inv -> {
            List<ReportDto> reports = inv.getArgument(1);
            ReportDto report = reports.get(0);

            assertEquals(1, reports.size());
            assertEquals(stub, report.getTopic());
            assertNull(report.getSpeakerName());
            assertNull(report.getSpeakerEmail());
            assertEquals(0, report.getId());
            assertEquals(0, report.getSpeakerId());
            return null;
        }).when(session).setAttribute(eq("reports"), anyCollection());
        servlet.doPost(req, resp);
    }

    @Test
    public void testAddReportWhenReportListIsExistWithOutSpeaker() throws IOException {
        List<ReportDto> reports = new ArrayList<>();
        reports.add(new ReportDto());
        String stub = "stub";
        when(req.getParameter("action")).thenReturn("addReport");
        when(req.getParameter("topic")).thenReturn(stub);
        when(session.getAttribute("reports")).thenReturn(reports);
        doAnswer(inv -> {
            List<ReportDto> reps = inv.getArgument(1);
            ReportDto report = reps.get(1);

            assertEquals(2, reports.size());
            assertEquals(stub, report.getTopic());
            assertNull(report.getSpeakerName());
            assertNull(report.getSpeakerEmail());
            assertEquals(0, report.getId());
            assertEquals(0, report.getSpeakerId());
            return null;
        }).when(session).setAttribute(eq("reports"), anyCollection());

        servlet.doPost(req, resp);
    }

    @Test
    public void testAddReportsWithSpeakers() throws IOException {
        List<ReportDto> reports = new ArrayList<>();
        String stub = "stub";
        when(req.getParameter("action")).thenReturn("addReport");
        when(req.getParameter("topic")).thenReturn(stub);
        when(session.getAttribute("reports")).thenReturn(reports);
        doAnswer(inv -> inv.<List<ReportDto>>getArgument(1)).when(session).setAttribute(eq("reports"), anyCollection());
        for (int i = 0; i < 5; i++) {
            createReport(i);
            servlet.doPost(req, resp);
        }
        verify(resp, times(5)).sendRedirect("stub/events/developing");
        assertEquals(5, reports.size());
    }

    @Test
    public void testDeleteReport() throws IOException {
        List<ReportDto> reports = new ArrayList<>(List.of(new ReportDto(), new ReportDto(), new ReportDto()));
        when(req.getParameter("action")).thenReturn("delete");
        when(session.getAttribute("reports")).thenReturn(reports);
        when(req.getParameter("id")).thenReturn("0");
        servlet.doPost(req, resp);
        assertEquals(2, reports.size());
    }

    @Test
    public void testUpdateEventWhenEventNotCreated() {
        String stub = "stub";
        when(req.getParameter("action")).thenReturn("update");
        when(req.getParameter("title")).thenReturn(stub);
        when(req.getParameter("place")).thenReturn(stub);
        when(req.getParameter("limit")).thenReturn("1");
        doAnswer(inv -> {
            Event event = inv.getArgument(1);
            assertEquals(stub, event.getName());
            assertEquals(stub, event.getPlace());
            assertEquals(1, event.getId());
            return null;
        }).when(session).setAttribute(eq("event"), any(Event.class));
    }

    @Test
    public void testUpdateEventWithoutParameter() throws IOException {
        Event event = new Event();
        event.setName("Old");
        when(req.getParameter("action")).thenReturn("update");
        when(req.getParameter("title")).thenReturn("New");
        when(session.getAttribute("event")).thenReturn(event);
        doNothing().when(session).setAttribute(eq("event"), any(Event.class));
        servlet.doPost(req, resp);
        assertEquals("New", event.getName());
    }

    @Test
    public void testUpdateEventWithInvalidDate() throws IOException {
        when(req.getParameter("action")).thenReturn("update");
        when(req.getParameter("start")).thenReturn("incorrectDate");
        servlet.doPost(req, resp);
        verify(resp).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testSave() throws DaoException, IOException {
        ServletConfig servletConfig = mock(ServletConfig.class);
        when(servlet.getServletConfig()).thenReturn(servletConfig);
        when(req.getParameter("action")).thenReturn("save");
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("event_service")).thenReturn(eventService);
        when(session.getAttribute("event")).thenReturn(new Event());
        when(session.getAttribute("reports")).thenReturn(new ArrayList<>());
        when(eventService.createEvent(any(Event.class), any())).thenReturn(true);
        servlet.doPost(req, resp);
        verify(resp).sendRedirect("stub/events/developing");
    }

    @Test
    public void testSaveWhenEventServiceThrowException() throws DaoException, IOException {
        ServletConfig servletConfig = mock(ServletConfig.class);
        when(servlet.getServletConfig()).thenReturn(servletConfig);
        when(req.getParameter("action")).thenReturn("save");
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("event_service")).thenReturn(eventService);
        when(session.getAttribute("event")).thenReturn(new Event());
        when(session.getAttribute("reports")).thenReturn(new ArrayList<>());
        when(eventService.createEvent(any(Event.class), any())).thenThrow(DaoException.class);
        servlet.doPost(req, resp);
        verify(resp).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void createReport(int id) {
        JsonObject speaker = Json.createObjectBuilder().add("id", "" + id).add("name", "name" + id).add("email", "email" + id).build();
        when(req.getParameter("topic")).thenReturn("Topic" + id);
        when(session.getAttribute("selectedSpeaker")).thenReturn(speaker);
    }
}
