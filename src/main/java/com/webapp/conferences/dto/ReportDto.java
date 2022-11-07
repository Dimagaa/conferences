package com.webapp.conferences.dto;

import com.webapp.conferences.model.Report;
import com.webapp.conferences.model.User;

import java.io.Serializable;
import java.util.List;

public class ReportDto implements Serializable {
    private long id;
    private String topic;
    private long speakerId;
    private String speakerName;
    private String speakerEmail;
    private List<User> proposedSpeakers;
    private Report.Status status;
    private boolean isRequested;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerEmail() {
        return speakerEmail;
    }

    public void setSpeakerEmail(String speakerEmail) {
        this.speakerEmail = speakerEmail;
    }

    public long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(long speakerId) {
        this.speakerId = speakerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Report.Status getStatus() {
        return status;
    }

    public void setStatus(Report.Status status) {
        this.status = status;
    }

    public List<User> getProposedSpeakers() {
        return proposedSpeakers;
    }

    public void setProposedSpeakers(List<User> proposedSpeakers) {
        this.proposedSpeakers = proposedSpeakers;
    }

    public boolean isRequested() {
        return isRequested;
    }

    public void setRequested(boolean requested) {
        isRequested = requested;
    }
}
