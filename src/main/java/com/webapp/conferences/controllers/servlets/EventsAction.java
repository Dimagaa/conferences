package com.webapp.conferences.controllers.servlets;

import com.webapp.conferences.dto.ReportDto;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;
import com.webapp.conferences.model.Report;
import com.webapp.conferences.services.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


public class EventsAction extends HttpServlet {

    private final Map<String, Function<HttpServletRequest, Boolean>> actions = new HashMap<>();

    {
        actions.put("save", this::save);
        actions.put("delete", this::delete);
        actions.put("publish", this::publish);
        actions.put("cancel", this::cancel);
        actions.put("restore", this::restore);
        actions.put("addReport", this::addReport);
        actions.put("updateReport", this::updateReport);
        actions.put("deleteReport", this::deleteReport);
        actions.put("proposeSpeaker", this::proposeSpeaker);
        actions.put("acceptSpeaker", this::acceptSpeaker);
        actions.put("rejectSpeaker", this::rejectSpeaker);
    }

    private final Logger logger = LogManager.getLogger("Global");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long eventId = Long.parseLong(req.getParameter("eventId"));
        try {
            eventService.getEvent(eventId).ifPresent(e -> req.setAttribute("event", e));
            req.setAttribute("reports", eventService.getPreparedReports(eventId));
        } catch (DaoException e) {
            logger.error("Cannot find event", e);
        }
        req.getRequestDispatcher("/WEB-INF/view/event-main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        Optional<String> path = Optional.ofNullable(req.getParameter("redirectPath"));
        boolean success = actions.get(action).apply(req);
        if (!success) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        if (path.isPresent()) {
            resp.sendRedirect(path.get());
        }
    }

    private boolean save(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        Event event = new Event();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Optional.ofNullable(req.getParameter("eventId")).ifPresent(e -> event.setId(Long.parseLong(e)));
            Optional.ofNullable(req.getParameter("title")).ifPresent(e -> event.setName(e.trim()));
            Optional.ofNullable(req.getParameter("place")).ifPresent(e -> event.setPlace(e.trim()));
            Optional.ofNullable(req.getParameter("limit")).ifPresent(e -> event.setLimit(Integer.parseInt(e)));
            Optional.ofNullable(req.getParameter("status")).ifPresent(e -> event.setStatus(Event.Status.valueOf(e)));

            String start = Optional.ofNullable(req.getParameter("start")).orElse("");
            String end = Optional.ofNullable(req.getParameter("end")).orElse("");
            Timestamp startTime = start.isEmpty() ? null : new Timestamp(formatter.parse(start).getTime());
            Timestamp endTime = end.isEmpty() ? null : new Timestamp(formatter.parse(end).getTime());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            return eventService.updateEvent(event);
        } catch (ParseException e) {
            logger.error("Parameter date is invalid", e);
            return false;
        } catch (DaoException e) {
            logger.error("Cannot update event: " + event.getName(), e);
            return false;
        }
    }

    private boolean delete(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long eventId = Long.parseLong(req.getParameter("data"));
        try {
            return eventService.delete(eventId);
        } catch (DaoException e) {
            logger.error("Cannot delete event" + eventId, e);
            return false;
        }
    }

    private boolean publish(HttpServletRequest req) {
        return updateStatus(req, Event.Status.ACTIVE);
    }

    private boolean cancel(HttpServletRequest req) {
        return updateStatus(req, Event.Status.CANCELED);
    }

    private boolean restore(HttpServletRequest req) {
        return updateStatus(req, Event.Status.ACTIVE);
    }

    private boolean addReport(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long eventId = Long.parseLong(req.getParameter("data"));
        String speakerId = req.getParameter("speaker").trim();
        Report report = new Report();
        report.setTopic(req.getParameter("topic"));
        report.setEventId(eventId);
        report.setSpeakerId(Long.parseLong(speakerId.isEmpty() ? "0" : speakerId));
        try {
            return eventService.addReport(report) != null;
        } catch (DaoException e) {
            logger.error("Cannot add report:" + report.getTopic(), e);
            return false;
        }
    }

    private boolean deleteReport(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        logger.debug(req.getParameter("data"));
        long reportId = Long.parseLong(req.getParameter("data"));
        try {
            return eventService.deleteReport(reportId);
        } catch (DaoException e) {
            logger.error("Cannot delete report: " + reportId, e);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean updateReport(HttpServletRequest req) {
        String scope = req.getParameter("scope");
        String topic = req.getParameter("topic");
        Optional<String> speakerId = Optional.ofNullable(req.getParameter("speaker"));
        String reportId = req.getParameter("reportId");
        if (scope != null && scope.equals("session")) {
            List<ReportDto> reports = (List<ReportDto>) req.getSession().getAttribute("reports");
            ReportDto report = new ReportDto();
            report.setTopic(topic);
            speakerId.ifPresent(e -> {
                if (e.isEmpty()) {
                    return;
                }
                report.setSpeakerId(Long.parseLong(e));
                report.setSpeakerName(req.getParameter("speakerName"));
                report.setSpeakerEmail(req.getParameter("speakerEmail"));
            });
            reports.set(Integer.parseInt(reportId), report);
            return true;
        }

        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long eventId = Long.parseLong(req.getParameter("data"));
        Report report = new Report();
        report.setId(Long.parseLong(reportId));
        report.setTopic(topic);
        report.setEventId(eventId);
        speakerId.ifPresent(e -> report.setSpeakerId(Long.parseLong(e.isEmpty() ? "0" : e)));
        report.setStatus(report.getSpeakerId() == 0 ? Report.Status.UNDETAILED : Report.Status.UNCONFIRMED);
        try {
            return eventService.updateReport(report);
        } catch (DaoException e) {
            logger.error("Cannot update report:" + topic, e);
            return false;
        }
    }

    private boolean proposeSpeaker(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long reportId = Long.parseLong(req.getParameter("data"));
        long speakerId = (long) req.getSession().getAttribute("userId");
        try {
            return eventService.createReportRequest(reportId, speakerId);
        } catch (DaoException e) {
            logger.error("Cannot create Report request", e);
            return false;
        }

    }

    private boolean acceptSpeaker(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long reportId = Long.parseLong(req.getParameter("data[reportId]"));
        long speakerId = Long.parseLong(req.getParameter("data[speakerId]"));
        try {
            return eventService.acceptSpeaker(reportId, speakerId);
        } catch (DaoException e) {
            logger.error("Speaker" + speakerId + " not accepted for report: " + reportId, e);
            return false;
        }
    }

    private boolean rejectSpeaker(HttpServletRequest req) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long reportId = Long.parseLong(req.getParameter("data[reportId]"));
        long speakerId = Long.parseLong(req.getParameter("data[speakerId]"));
        try {
            return eventService.rejectSpeaker(reportId, speakerId);
        } catch (DaoException e) {
            logger.error("Speaker" + speakerId + " not rejected for report: " + reportId, e);
            return false;
        }
    }


    private boolean updateStatus(HttpServletRequest req, Event.Status status) {
        EventService eventService = (EventService) getServletContext().getAttribute("event_service");
        long eventId = Long.parseLong(req.getParameter("data"));
        try {
            return eventService.updateStatus(eventId, status);
        } catch (DaoException e) {
            logger.error("Cannot update event status" + eventId, e);
            return false;
        }
    }
}
