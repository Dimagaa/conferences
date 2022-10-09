package com.webapp.conferences.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Event implements Serializable {
    private long id;
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;
    private String place;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (limitEvents != event.limitEvents) return false;
        if (!name.equals(event.name)) return false;
        if (!startTime.equals(event.startTime)) return false;
        if (!endTime.equals(event.endTime)) return false;
        return place.equals(event.place);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + place.hashCode();
        result = 31 * result + limitEvents;
        return result;
    }
}
