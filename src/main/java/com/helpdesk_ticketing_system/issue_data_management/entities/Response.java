package com.helpdesk_ticketing_system.issue_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    @JsonProperty("status") private Integer status;
    @JsonProperty("message") private String message;

    public Response() {
    }

    public Response(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
