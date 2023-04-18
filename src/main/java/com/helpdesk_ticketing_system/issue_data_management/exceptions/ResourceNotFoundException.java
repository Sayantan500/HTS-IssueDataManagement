package com.helpdesk_ticketing_system.issue_data_management.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends Exception{
    @JsonProperty("status") private final Integer status;

    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public Integer getStatus() {
        return status;
    }
}
