package com.helpdesk_ticketing_system.issue_data_management.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthCheckEndpoint {
    @GetMapping
    public ResponseEntity<Object> getHealth(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
