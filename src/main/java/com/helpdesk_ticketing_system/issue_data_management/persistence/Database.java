package com.helpdesk_ticketing_system.issue_data_management.persistence;

public interface Database<T> {
    String saveToDb(T newData) throws Exception;
}
