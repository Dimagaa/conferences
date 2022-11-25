package com.conferences.controllers.servlets;

import com.conferences.dto.EventDto;
import com.conferences.exceptions.DaoException;
import com.conferences.model.User.Role;
import com.conferences.services.EventService;
import com.conferences.services.pagination.Page;
import org.junit.jupiter.api.Assertions;
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
import java.util.ArrayList;
import java.util.List;

import static com.conferences.model.Event.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventsServletTest {

    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
    private final ServletConfig servletConfig = mock(ServletConfig.class);
    private final ServletContext servletContext = mock(ServletContext.class);

    private final Page page = new Page(1);
    private final EventService eventService = mock(EventService.class);
    private final Events servlet = spy(Events.class);
    private final String contextPath = "stub";

    @BeforeEach
    public void setUp() {
        when(servlet.getServletConfig()).thenReturn(servletConfig);
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(req.getSession()).thenReturn(session);

        when(req.getContextPath()).thenReturn(contextPath);
        when(servletContext.getAttribute("event_service")).thenReturn(eventService);
        when(session.getAttribute("page")).thenReturn(page);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testSetEventAttributeWhenEventsNotFound() throws DaoException, ServletException, IOException {
        when(eventService.getPreparedEvents(any(Page.class), any(Role.class), anyLong()))
                .thenReturn(new ArrayList<>());
        when(session.getAttribute("role")).thenReturn(Role.USER);
        doAnswer(inv -> {
            List<EventDto> events = inv.getArgument(1);
            Assertions.assertTrue(events.isEmpty());
            return null;
        }).when(req).setAttribute(eq("events"), anyCollection());
        servlet.doGet(req, resp);
        verify(req).setAttribute(eq("events"), anyCollection());
        verify(req, times(0)).setAttribute("countProposed", eventService.countProposedReport(1));
    }

    @Test
    public void testSetEventAttributeWhenEventNotFoundAndUserRoleIsSpeaker() throws DaoException, ServletException, IOException {
        when(eventService.getPreparedEvents(any(Page.class), any(Role.class), anyLong()))
                .thenReturn(new ArrayList<>());
        when(session.getAttribute("role")).thenReturn(Role.SPEAKER);
        doAnswer(inv -> {
            List<EventDto> events = inv.getArgument(1);
            Assertions.assertTrue(events.isEmpty());
            return null;
        }).when(req).setAttribute(eq("events"), anyCollection());
        servlet.doGet(req, resp);
        verify(req).setAttribute(eq("events"), anyCollection());
        verify(req).setAttribute("countProposed", eventService.countProposedReport(1));
    }

    @Test
    public void testDoGetDevelopingEvents() throws ServletException, IOException {
        when(req.getServletPath()).thenReturn("/events/developing");
        servlet.doGet(req, resp);
        assertEquals(DEVELOPING.name(), page.getTargetFilters().get("eventsByStatus"));
    }

    @Test
    public void testDoGetCanceledEvents() throws ServletException, IOException {
        when(req.getServletPath()).thenReturn("/events/canceled");
        servlet.doGet(req, resp);
        assertEquals(CANCELED.name(), page.getTargetFilters().get("eventsByStatus"));
    }

    @Test
    public void testDoGetJoinedEventsWhenRoleIsUser() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn(Role.USER);
        when(req.getServletPath()).thenReturn("/events/joined");
        servlet.doGet(req, resp);
        assertNull(page.getTargetFilters().get("eventsBySpeaker"));
        assertEquals("1", page.getTargetFilters().get("joinedEvents"));
    }

    @Test
    public void testDoGetJoinedEventsWhenRoleIsSpeaker() throws ServletException, IOException {
        when(session.getAttribute("role")).thenReturn(Role.SPEAKER);
        when(req.getServletPath()).thenReturn("/events/joined");
        servlet.doGet(req, resp);
        assertNull(page.getTargetFilters().get("joinedEvents"));
        assertEquals("1", page.getTargetFilters().get("eventsBySpeaker"));
    }

    @Test
    public void testDoGetPassedEvents() throws ServletException, IOException {
        when(req.getServletPath()).thenReturn("/events/passed");
        when(session.getAttribute("role")).thenReturn(Role.USER);
        servlet.doGet(req, resp);
        assertEquals("true", page.getTargetFilters().get("expiredEvents"));
        assertEquals("1", page.getTargetFilters().get("joinedEvents"));
    }

    @Test
    public void doGetByDefault() throws ServletException, IOException {
        servlet.doGet(req, resp);
        assertEquals(String.format("%s %s %s", ACTIVE, COMPLETED, CANCELED), page.getTargetFilters().get("eventsByStatus"));
    }

    @Test
    public void doGetWithDaoException() throws DaoException, ServletException, IOException {
        when(eventService.getPreparedEvents(any(), any(), anyLong())).thenThrow(DaoException.class);
        servlet.doGet(req, resp);
        verify(req).getRequestDispatcher(contextPath + "/error");
    }

    @Test
    public void doGetWhenPageIsNotExist() throws ServletException, IOException {
        when(session.getAttribute("page")).thenReturn(null);
        doAnswer( inv -> {
            Page page = inv.getArgument(1);
            assertNotNull(page);
            assertEquals(1, page.getPageNumber());
            assertEquals(4, page.getPageSize());
            return null;
        }).when(session).setAttribute(eq("page"), any(Page.class));
        servlet.doGet(req, resp);
    }
}
