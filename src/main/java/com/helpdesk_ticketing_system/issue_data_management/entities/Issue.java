package com.helpdesk_ticketing_system.issue_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Issue {
    @JsonProperty("_id") private String _id;
    @JsonProperty("submitted_by") private String submitted_by;
    @JsonProperty("subject") private String subject;
    @JsonProperty("description") private String description;
    @JsonProperty("ticket_id") private String ticket_id;
    @JsonProperty("status") private String status;
    @JsonProperty("posted_on") private Long postedOn;

    public Issue() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSubmitted_by() {
        return submitted_by;
    }

    public void setSubmitted_by(String submitted_by) {
        this.submitted_by = submitted_by;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }
}
