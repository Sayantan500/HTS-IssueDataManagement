package com.helpdesk_ticketing_system.issue_data_management.persistence;

import com.helpdesk_ticketing_system.issue_data_management.entities.Page;

import java.util.List;

public interface Database<T> {
    String saveToDb(T newData) throws Exception;
    T getIssueById(Object id, Class<T> targetType) throws Exception;
    List<T> getIssues(
            Object submitted_by, Long postedOn, Integer limit, Page pageDirectionToGo, Class<T> targetType
    ) throws Exception;
}
