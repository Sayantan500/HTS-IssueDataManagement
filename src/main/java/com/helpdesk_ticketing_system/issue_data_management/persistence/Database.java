package com.helpdesk_ticketing_system.issue_data_management.persistence;

import java.util.List;

public interface Database<T> {
    String saveToDb(T newData) throws Exception;
    T getIssueById(Object id, Class<T> targetType) throws Exception;
    List<T> getIssues(
            Object submitted_by, Long startRange, Long endRange, Integer limit, Class<T> targetType
    ) throws Exception;

    List<T> getIssuesByStatus(
            Object status, Long startRange, Long endRange, Integer limit, Class<T> targetType
    ) throws Exception;
}
