package com.helpdesk_ticketing_system.issue_data_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VariablesBeans {
    @Bean(name = "pagination-limit")
    public Integer getPaginationLimit(){
        return Integer.parseInt(System.getenv("pagination_limit"));
    }
}
