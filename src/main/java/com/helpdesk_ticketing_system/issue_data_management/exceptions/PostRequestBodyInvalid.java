package com.helpdesk_ticketing_system.issue_data_management.exceptions;

import org.springframework.http.HttpStatus;

public class PostRequestBodyInvalid extends Exception
{
    private final Integer status;
    public PostRequestBodyInvalid(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return String.format("%d : %s",status,super.getMessage());
    }

    public Integer getStatus() {
        return status;
    }
}
