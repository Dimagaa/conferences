package com.conferences.controllers.servlets;

import com.conferences.services.pagination.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * <code>HttpServlet</code> support <code>doGet</code> method.
 * Providing filtering and sorting events for clients.
 */
public class ContentFilter extends HttpServlet {

    /**
     * Handling request that could have request parameter "clear". In this case filter parameters
     * and sort parameters in {@link Page} object will remove,
     * otherwise parameters will update
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException -  if the request for the POST could not be handled
     * @throws IOException – if an input or output error is detected when the servlet handles the request
     */
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

    /**
     * Handling {@link HttpServletRequest} and set filter and sort parameters
     * in {@link Page} object.
     * @param req an {@link HttpServletRequest} object that contains the request
     *           the client has made of the servlet
     * @param page an {@link Page} object that store all filters, sorters params
     *             and then will handle in Dao layers.
     */
    private void updateEventsParams(HttpServletRequest req, Page page) {
        Optional<String> filterByStatus = Optional.ofNullable(req.getParameter("filterByStatus"));
        Optional<String> expiredEvents = Optional.ofNullable(req.getParameter("expiredEvents"));
        Optional<String> filterBySpeaker = Optional.ofNullable(req.getParameter("speaker"));
        Optional<String> filterByPlace = Optional.ofNullable(req.getParameter("place"));
        Optional<String> sortBy = Optional.ofNullable(req.getParameter("sortBy"));
        Optional<String> order = Optional.ofNullable(req.getParameter("orderBtn"));
        Optional<String> search = Optional.ofNullable(req.getParameter("search"));

        page.clearParams();
        page.setPageNumber(1);
        filterByStatus.ifPresent(e -> page.setFilterParameter("eventsByStatus", e));
        expiredEvents.ifPresent( e -> page.setFilterParameter("expiredEvents", e));
        page.setFilterParameter("eventsBySpeaker", filterBySpeaker.orElse(""));
        page.setFilterParameter("eventsByPlace", filterByPlace.orElse(""));
        page.setSortParameter(sortBy.orElse("start"), order.orElse("ASC"));
        page.setFilterParameter("searchEvent", search.orElse(""));
    }
}
