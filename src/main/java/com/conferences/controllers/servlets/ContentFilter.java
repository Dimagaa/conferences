package com.conferences.controllers.servlets;

import com.conferences.services.pagination.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class ContentFilter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clear = Optional.ofNullable(req.getParameter("clear")).orElse("");
        String url = Optional.of(req.getServletPath()).map((s) ->  s.substring(0, s.indexOf("/filter"))).get();
        Page page = (Page) req.getSession().getAttribute("page");
        if(clear.equals("clear")) {
            page.clearParams();
            page.setPageNumber(1);
            req.getSession().removeAttribute("selectedSpeaker");
            req.getRequestDispatcher(url).forward(req, resp);
            return;
        }
        updateEventsParams(req, page);
        req.getRequestDispatcher(url).forward(req, resp);
    }

    private void updateEventsParams(HttpServletRequest req, Page page) {
        Optional<String[]> filterByStatus = Optional.ofNullable(req.getParameterValues("filterByStatus"));
        Optional<String> expiredEvents = Optional.ofNullable(req.getParameter("expiredEvents"));
        Optional<String> filterBySpeaker = Optional.ofNullable(req.getParameter("speaker"));
        Optional<String> filterByPlace = Optional.ofNullable(req.getParameter("place"));
        Optional<String> sortBy = Optional.ofNullable(req.getParameter("sortBy"));
        Optional<String> order = Optional.ofNullable(req.getParameter("orderBtn"));
        Optional<String> search = Optional.ofNullable(req.getParameter("search"));

        page.clearParams();
        page.setPageNumber(1);
        filterByStatus.ifPresent(e -> Arrays.stream(e).forEach(s -> page.setFilterParameter("eventsByStatus", s)));
        expiredEvents.ifPresent( e -> page.setFilterParameter(e, "true"));
        page.setFilterParameter("eventsBySpeaker", filterBySpeaker.orElse(""));
        page.setFilterParameter("eventsByPlace", filterByPlace.orElse(""));
        page.setSortParameter(sortBy.orElse("start"), order.orElse("ASC"));
        page.setFilterParameter("searchEvent", search.orElse(""));
    }
}
