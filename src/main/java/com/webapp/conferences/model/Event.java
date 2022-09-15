package com.webapp.conferences.model;

import java.sql.Timestamp;
import java.util.List;

public class Event {
    private long id;
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;
    private String place;

    private List<Report> reports;
    private int limitEvents;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getLimitEvents() {
        return limitEvents;
    }

    public void setLimitEvents(int limitEvents) {
        this.limitEvents = limitEvents;
    }
}
