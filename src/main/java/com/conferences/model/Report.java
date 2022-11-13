package com.conferences.model;

import java.io.Serializable;

public class Report implements Serializable {
    private long id;
    private String topic;
    private long eventId;
    private long speakerId;
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(long speakerId) {
        this.speakerId = speakerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report = (Report) o;

        if (id != report.id) return false;
        if (eventId != report.eventId) return false;
        if (speakerId != report.speakerId) return false;
        return topic.equals(report.topic);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + topic.hashCode();
        result = 31 * result + (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (int) (speakerId ^ (speakerId >>> 32));
        return result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        CONSOLIDATED,
        UNCONFIRMED,
        UNDETAILED,
        PROPOSED
    }
}
