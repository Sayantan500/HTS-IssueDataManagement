package com.helpdesk_ticketing_system.issue_data_management.persistence.repository;

import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuesDao {
    String addNewIssue(Issue newIssue)  throws Exception;
}
