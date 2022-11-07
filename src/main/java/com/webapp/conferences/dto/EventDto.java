package com.webapp.conferences.dto;

import com.webapp.conferences.model.Event.Status;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class EventDto implements Serializable {
    private long id;
    private String title;
    private List<ReportDto> reports;
    private List<ReportDto> proposedReports;
    private Timestamp start;
    private String duration;
    private String place;
    private int participantsCount;
    private int limitReports;
    private Status status;
    private boolean isExpired;
    private boolean isReportCompleted;
    private boolean isReadyToPublish;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ReportDto> getReports() {
        return reports;
    }

    public void setReports(List<ReportDto> reports) {
        this.reports = reports;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getLimitReports() {
        return limitReports;
    }

    public void setLimitReports(int limitReports) {
        this.limitReports = limitReports;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ReportDto> getProposedReports() {
        return proposedReports;
    }

    public void setProposedReports(List<ReportDto> proposedReports) {
        this.proposedReports = proposedReports;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isReadyToPublish() {
        return isReadyToPublish;
    }

    public void setReadyToPublish(boolean readyToPublish) {
        isReadyToPublish = readyToPublish;
    }

    public boolean isReportCompleted() {
        return isReportCompleted;
    }

    public void setReportCompleted(boolean reportCompleted) {
        isReportCompleted = reportCompleted;
    }

}
