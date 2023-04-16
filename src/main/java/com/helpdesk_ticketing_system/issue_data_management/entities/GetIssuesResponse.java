package com.helpdesk_ticketing_system.issue_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetIssuesResponse {

    @JsonProperty("count") private Integer count;
    @JsonProperty("records") private List<Issue> listOfIssues;
    @JsonProperty("first_item_posted_on_value") private Long firstDataItemPostedOnValue;
    @JsonProperty("last_item_posted_on_value") private Long lastDataItemPostedOnValue;

    public GetIssuesResponse() {
    }

    public GetIssuesResponse(Integer count, List<Issue> listOfIssues, Long firstDataItemPostedOnValue, Long lastDataItemPostedOnValue) {
        this.count = count;
        this.listOfIssues = listOfIssues;
        this.firstDataItemPostedOnValue = firstDataItemPostedOnValue;
        this.lastDataItemPostedOnValue = lastDataItemPostedOnValue;
    }
}
