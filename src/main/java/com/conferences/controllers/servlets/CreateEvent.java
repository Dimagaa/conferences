package com.conferences.controllers.servlets;

import com.conferences.model.Event;
import com.conferences.dto.ReportDto;
import com.conferences.exceptions.DaoException;
import com.conferences.services.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * <code>HttpServlet</code> support <code>doGet</code> and <code>doPost</code> methods.
 * Providing functions for creating events.
 */
public class CreateEvent extends HttpServlet {

    /**
     * {@link HashMap} store action string representation as a key and the corresponding {@link Function} as a value.
     */
    private final Map<String, Function<HttpServletRequest, Boolean>> actions = new HashMap<>();
    {
        actions.put("addReport", this::addReport);
        actions.put("update", this::updateEvent);
        actions.put("save", this::save);
        actions.put("delete", this::deleteReport);
    }

    private final Logger logger = LogManager.getLogger("Global");

    /**
     * Forwards a request from a servlet to {@code ../event-main.jsp}
     *
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/event-main.jsp").forward(req, resp);
    }

    /**
     * Getting request parameter "action" as a key in {@link #actions}.
     * If action executed with success send redirect, otherwise send
     * internal server error.
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        logger.debug(action);
        if(actions.get(action).apply(req)){
            resp.sendRedirect(req.getContextPath() + ("/events/create"));
            return;
        }
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }


    @SuppressWarnings("unchecked")
    private boolean addReport(HttpServletRequest req) {
        List<ReportDto> reports = Optional.ofNullable((ArrayList<ReportDto>) req.getSession().getAttribute("reports")).orElse(new ArrayList<>());
        Optional<String> topic = Optional.ofNullable(req.getParameter("topic"));
        Optional<JsonObject> speaker = Optional.ofNullable((JsonObject) req.getSession().getAttribute("selectedSpeaker"));
        ReportDto report = new ReportDto();
        if(topic.isEmpty()) {
            return false;
        }
        report.setTopic(topic.get());
        speaker.ifPresent( e -> {
            report.setSpeakerId(Long.parseLong(e.getString("id")));
            report.setSpeakerName(e.getString("name"));
            report.setSpeakerEmail(e.getString("email"));
        });
        reports.add(report);
        req.getSession().setAttribute("reports", reports);
        req.getSession().removeAttribute("selectedSpeaker");
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean save(HttpServletRequest req) {
        updateEvent(req);
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        Event event = (Event) req.getSession().getAttribute("event");
        List<ReportDto> reports = (List<ReportDto>) req.getSession().getAttribute("reports");
        try {
            if(eventService.createEvent(event, reports)) {
                req.getSession().removeAttribute("speakerCache");
                req.getSession().removeAttribute("event");
                req.getSession().removeAttribute("reports");
                return true;
            }
        } catch (DaoException e) {
            logger.error("Can't save event in Data Base", e);
            return false;
        }
        return false;
    }

    private boolean updateEvent(HttpServletRequest req) {
        Event event = Optional.ofNullable((Event) req.getSession().getAttribute("event")).orElse(new Event());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Optional.ofNullable(req.getParameter("title")).ifPresent( e -> event.setName(e.trim()));
            Optional.ofNullable(req.getParameter("place")).ifPresent( e -> event.setPlace(e.trim()));
            Optional.ofNullable(req.getParameter("limit")).ifPresent( e -> event.setLimit(Integer.parseInt(e)));

            String start = Optional.ofNullable(req.getParameter("start")).orElse("");
            String end = Optional.ofNullable(req.getParameter("end")).orElse("");
            Timestamp startTime = start.isEmpty() ? null : new Timestamp(formatter.parse(start).getTime());
            Timestamp endTime = end.isEmpty() ? null : new Timestamp(formatter.parse(end).getTime());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
        } catch (ParseException e) {
            logger.error("Parameter date from Event Create For is invalid", e);
            return false;
        }
        req.getSession().setAttribute("event", event);
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean deleteReport(HttpServletRequest req) {
        List<ReportDto> reports = Optional.ofNullable((ArrayList<ReportDto>) req.getSession().getAttribute("reports")).orElse(new ArrayList<>());
        int index = Integer.parseInt(Optional.ofNullable(req.getParameter("id")).orElseThrow());
        reports.remove(index);
        return true;
    }
}
