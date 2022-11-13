package com.conferences.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class Event implements Serializable {
    private long id;
    private String name;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private String place;
    private int limit;
    private Status status;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (limit != event.limit) return false;
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
        result = 31 * result + limit;
        return result;
    }

    public enum Status {
        ACTIVE,
        DEVELOPING,
        CANCELED,
        COMPLETED
    }
}
