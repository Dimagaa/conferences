package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.dto.ReportDto;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;
import com.webapp.conferences.services.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

@WebServlet
public class CreateEvent extends HttpServlet {

    private final Map<String, Function<HttpServletRequest, Boolean>> actions = new HashMap<>();
    {
        actions.put("addReport", this::addReport);
        actions.put("update", this::updateEvent);
        actions.put("save", this::save);
        actions.put("delete", this::deleteReport);
    }

    private final Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/event-main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        logger.debug(action);
        boolean success = actions.get(action).apply(req);
        resp.sendRedirect(req.getContextPath() + (success ? "/events/create" : "/error"));
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
