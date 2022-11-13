package com.conferences.services;

import com.conferences.dao.DaoFactory;
import com.conferences.dao.EventDao;
import com.conferences.dao.ReportDao;
import com.conferences.dto.EventDto;
import com.conferences.dto.ReportDto;
import com.conferences.exceptions.DaoException;
import com.conferences.model.Event;
import com.conferences.model.Report;
import com.conferences.model.User;
import com.conferences.services.pagination.Page;
import com.conferences.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


public class EventService {

    private final EventDao eventDao;
    private final ReportDao reportDao;
    private final UserDao userDao;
    private final Logger logger = LogManager.getLogger("Global");

    public EventService(String database) {
        DaoFactory daoFactory = DaoFactory.getDaoFactory(database);
        this.eventDao = daoFactory.getEventDao();
        this.reportDao = daoFactory.getReportDao();
        this.userDao = daoFactory.getUserDao();
    }

    public List<EventDto> getPreparedEvents(Page page, User.Role role, long userId) throws DaoException {
        List<EventDto> events = new ArrayList<>();
        List<Event> eventList = eventDao.getPreparedEvents(page);
        for (Event event : eventList) {
            EventDto eventDto = prepareEvent(event);
            List<Report> reports = reportDao.findByEvent(event.getId());
            eventDto.setReadyToPublish(isReadyToPublish(event, reports));
            eventDto.setReportCompleted(reports.size() == event.getLimit());
            eventDto.setReports(getPreparedReports(eventDto, role, userId));
            eventDto.setProposedReports(role == User.Role.USER ? null : getProposedReports(event.getId()));
            if(role == User.Role.USER) {
                eventDto.setJoined(eventDao.isJoined(event.getId(), userId));
            }
            events.add(eventDto);
        }
        return events;
    }

    public Optional<Event> getEvent(long eventId) throws DaoException {
        return eventDao.findById(eventId);
    }

    public boolean createEvent(Event event, List<ReportDto> reportDtos) throws DaoException {
        if (isInvalidEvent(event)) {
            logger.error("Event is invalid:" + event.getName());
            return false;
        }
        event.setStatus(Event.Status.DEVELOPING);
        eventDao.add(event);
        if (reportDtos == null || reportDtos.isEmpty()) {
            return true;
        }
        List<Report> reports = reportDtos.stream().map((e) -> {
            Report report = new Report();
            report.setTopic(e.getTopic());
            report.setSpeakerId(e.getSpeakerId());
            report.setEventId(event.getId());
            report.setStatus(report.getSpeakerId() == 0 ? Report.Status.UNDETAILED : Report.Status.UNCONFIRMED);
            return report;
        }).collect(Collectors.toList());
        return reportDao.addAll(reports);
    }

    public boolean updateEvent(Event event) throws DaoException {
        return eventDao.update(event);
    }

    public List<ReportDto> getPreparedReports(long eventId) throws DaoException {
        List<ReportDto> reports = new ArrayList<>();
        for(Report report:reportDao.findByEvent(eventId)) {
            reports.add(prepareReport(report));
        }
        return reports;
    }

    public List<ReportDto> getPreparedReports(EventDto event, User.Role role, long speakerId) throws DaoException {
        List<ReportDto> reports = new ArrayList<>();
        for(Report report : reportDao.findByEvent(event.getId())) {
            ReportDto reportDto = prepareReport(report);
            if(role == User.Role.SPEAKER && nonNull(reportDto.getProposedSpeakers())) {
                reportDto.setRequested(reportDto.getProposedSpeakers()
                        .stream()
                        .anyMatch( u -> u.getId() == speakerId));
            }
            if(report.getStatus() == Report.Status.UNCONFIRMED && report.getSpeakerId() == speakerId) {
                event.hasProposedReport(true);
            }
            reports.add(reportDto);
        }
        return reports;
    }

    public  List<ReportDto> getProposedReports(long eventId) throws DaoException {
        List<ReportDto> reports = new ArrayList<>();
        for (Report report : reportDao.findProposedByEvent(eventId)) {
            reports.add(prepareReport(report));
        }
        return reports;
    }

    public Boolean deleteReport(long reportId) throws DaoException {
        return reportDao.delete(reportId);
    }

    public boolean delete(long id) throws DaoException {
        return eventDao.delete(id);
    }

    public boolean updateStatus(long id, Event.Status status) throws DaoException {
        if(status == Event.Status.ACTIVE) {
            reportDao.deleteALlProposed(id);
        }
        return eventDao.updateStatus(id, status);
    }

    public List<String> getPlaces(String prefix) throws DaoException {
        return eventDao.findPlaces(prefix);
    }

    public List<String> getEventHeaders(String prefix, User.Role role) throws DaoException {
        return eventDao.getEventHeaders(prefix, role == User.Role.USER);
    }

    public Report addReport(Report report) throws DaoException {
        return reportDao.add(report);
    }

    public boolean updateReport(Report report) throws DaoException {
        return reportDao.update(report);
    }


    public boolean createReportRequest(long reportId, long speakerId) throws DaoException {
        return reportDao.createReportRequest(reportId, speakerId);
    }

    public boolean acceptSpeaker(long reportId, long speakerId) throws DaoException {
        return reportDao.acceptSpeaker(reportId, speakerId);
    }

    public boolean rejectSpeaker(long reportId, long speakerId) throws DaoException {
        return reportDao.rejectSpeaker(reportId, speakerId);
    }

    public boolean rejectReport(long reportId) throws DaoException {
        return reportDao.rejectReport(reportId);
    }

    public boolean updateReportStatus(long reportId, Report.Status status) throws DaoException {
        return reportDao.updateStatus(reportId, status);
    }

    public boolean joinEvent(long eventId, long userId) throws DaoException {
        return eventDao.joinEvent(eventId, userId);
    }

    public boolean leaveEvent(long eventId, long userId) throws DaoException {
        return eventDao.leaveEvent(eventId, userId);
    }

    public int countProposedReport(long speakerId) throws DaoException{
        return reportDao.findUnconfirmedBySpeaker(speakerId).size();
    }

    private EventDto prepareEvent(Event event) throws DaoException {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setTitle(event.getName());
        eventDto.setStart(event.getStartTime());
        eventDto.setDuration(getDuration(event));
        eventDto.setPlace(event.getPlace());
        eventDto.setParticipantsCount(getParticipantsCount(event));
        eventDto.setExpired(event.getStartTime().before(new Date(System.currentTimeMillis())));
        if(event.getStatus() == Event.Status.ACTIVE && eventDto.isExpired()) {
            event.setStatus(Event.Status.COMPLETED);
            eventDao.update(event);
        }
        eventDto.setStatus(event.getStatus());
        return eventDto;
    }

    private ReportDto prepareReport(Report report) throws DaoException {
        ReportDto reportDto = new ReportDto();
        reportDto.setId(report.getId());
        reportDto.setTopic(report.getTopic());
        reportDto.setStatus(report.getStatus());
        if (report.getStatus() == Report.Status.UNDETAILED) {
            reportDto.setProposedSpeakers(userDao.findProposedSpeaker(report.getId()));
            return reportDto;
        }
        userDao.findById(report.getSpeakerId()).ifPresent((speaker) -> {
            reportDto.setSpeakerId(speaker.getId());
            reportDto.setSpeakerName(String.format("%s %s", speaker.getFirstName(), speaker.getLastName()));
            reportDto.setSpeakerEmail(speaker.getLogin());
        });
        return reportDto;
    }

    private boolean isReadyToPublish(Event event, List<Report> reports) {
        if (isInvalidEvent(event) || reports == null || reports.size() != event.getLimit()) {
            return false;
        }
        for (Report report : reports) {
            if (report.getTopic() == null || report.getSpeakerId() == 0 || report.getStatus() != Report.Status.CONSOLIDATED) {
                return false;
            }
        }
        return true;
    }

    private String getDuration(Event event) {
        StringBuilder sb = new StringBuilder();
        int totalTime = (int) (event.getEndTime().getTime() - event.getStartTime().getTime()) / 1000;

        int hours = totalTime / 60 / 60;
        int minutes = totalTime / 60 % 60;

        if (hours > 0) {
            sb.append(hours);
            sb.append("h ");
        }
        if (minutes != 0) {
            sb.append(minutes);
            sb.append("min");
        }
        return sb.toString();
    }

    private int getParticipantsCount(Event event) throws DaoException {
        return userDao.findParticipants(event.getId()).size();
    }

    private boolean isInvalidEvent(Event event) {
        boolean isInvalidName = event.getName() == null || event.getName().isEmpty();
        boolean isInvalidPlace = event.getPlace() == null || event.getPlace().isEmpty();
        boolean isInvalidLimit = event.getLimit() < 1;

        if (event.getStartTime() == null || event.getEndTime() == null) {
            return true;
        }

        if (event.getStartTime().before(new Date(System.currentTimeMillis())) || event.getEndTime().before(event.getStartTime())) {
            return true;
        }
        return isInvalidName || isInvalidPlace || isInvalidLimit;
    }


}
