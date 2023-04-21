package com.helpdesk_ticketing_system.issue_data_management.persistence.repository;

import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import com.helpdesk_ticketing_system.issue_data_management.entities.Page;
import com.helpdesk_ticketing_system.issue_data_management.entities.Status;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IssuesDao {
    String addNewIssue(Issue newIssue)  throws Exception;
    Issue getIssueById(String issueId) throws Exception;
    List<Issue> getIssues(String username, Integer limit, Long postedOn, Page pageDirection) throws Exception;
    Issue updateIssue(String issueId, Map<String, Object> updatedFieldValuePairs) throws Exception;

    List<Issue> getNewIssues(Status status, Integer limit, Long startRange, Long endRange) throws Exception;
}
