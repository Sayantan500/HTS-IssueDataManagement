package com.helpdesk_ticketing_system.issue_data_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VariablesBeans {
    @Bean(name = "pagination-limit")
    public Integer getPaginationLimit(){
        return Integer.parseInt(System.getenv("pagination_limit"));
    }

    @Bean(name = "issueId.random_number_range")
    public Integer randomNumberRange(){
        return Integer.parseInt(System.getenv("issueId_random_number_range"));
    }

    @Bean(name = "issueId.num_of_digits_to_extract.from.posted_on")
    public Integer numOfDigitsToExtractFromPostedOn(){
        return Integer.parseInt(System.getenv("issueId_digits_to_extract"));
    }

    @Bean(name = "issueId.prefix")
    public String issueIdPrefix(){
        return System.getenv("issueId_prefix");
    }
}
