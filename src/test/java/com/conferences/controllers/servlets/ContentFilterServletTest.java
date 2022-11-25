package com.conferences.controllers.servlets;

import com.conferences.dao.parameters.SortParameter;
import com.conferences.services.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContentFilterServletTest {

    private final ContentFilter servlet = new ContentFilter();
    private final Page page = new Page(1);
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        when(req.getServletPath()).thenReturn("test/filter");
        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher("test")).thenReturn(dispatcher);
        when(session.getAttribute("page")).thenReturn(page);
        doNothing().when(dispatcher).forward(req, resp);
    }

    @Test
    public void testDoGetWithoutParameters() throws ServletException, IOException {
        servlet.doGet(req, resp);
        assertTrue(page.getFilters().isEmpty());
        assertEquals(SortParameter.Order.ASC, page.getSorters().get("start"));
    }

    @ParameterizedTest
    @MethodSource("filterParameters")
    public void testDoGetWithOneParameter(String reqParameter, String filterParameter) throws ServletException, IOException {
        String filterValue = "TEST";
        when(req.getParameter(reqParameter)).thenReturn(filterValue);
        servlet.doGet(req, resp);
        assertEquals(filterValue, page.getFilterValue(filterParameter));
        assertEquals(SortParameter.Order.ASC, page.getSorters().get("start"));
    }

    @Test
    public void testDoGetWithAllParameters() throws ServletException, IOException {
        String stub = "stub";
        when(req.getParameter("filterByStatus")).thenReturn(stub);
        when(req.getParameter("expiredEvents")).thenReturn(stub);
        when(req.getParameter("speaker")).thenReturn(stub);
        when(req.getParameter("place")).thenReturn(stub);
        when(req.getParameter("sortBy")).thenReturn(stub);
        when(req.getParameter("orderBtn")).thenReturn("DESC");
        when(req.getParameter("search")).thenReturn(stub);

        servlet.doGet(req, resp);
        assertEquals(stub, page.getFilterValue("eventsByStatus"));
        assertEquals(stub, page.getFilterValue("expiredEvents"));
        assertEquals(stub, page.getFilterValue("eventsBySpeaker"));
        assertEquals(stub, page.getFilterValue("eventsByPlace"));
        assertEquals(SortParameter.Order.DESC, page.getSorters().get(stub));
    }

    @Test
    public void testDoGetWithClearParameter() throws ServletException, IOException {
        when(req.getParameter("clear")).thenReturn("clear");
        servlet.doGet(req, resp);
        verify(session, times(1)).removeAttribute("selectedSpeaker");
        assertTrue(page.getFilters().isEmpty());
        assertTrue(page.getSorters().isEmpty());
        assertEquals(1, page.getPageNumber());
    }

    private static Stream<Arguments> filterParameters() {
        return Stream.of(
                Arguments.of("filterByStatus", "eventsByStatus"),
                Arguments.of("expiredEvents", "expiredEvents"),
                Arguments.of("speaker", "eventsBySpeaker"),
                Arguments.of("place", "eventsByPlace")
        );
    }
}
