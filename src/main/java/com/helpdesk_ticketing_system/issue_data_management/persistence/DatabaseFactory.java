package com.helpdesk_ticketing_system.issue_data_management.persistence;

import com.helpdesk_ticketing_system.issue_data_management.persistence.mongo_db.MongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFactory {
    private final ApplicationContext applicationContext;

    @Autowired
    public DatabaseFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Database getDatabaseFor(DatabaseType databaseType){
        if(databaseType.equals(DatabaseType.MONGO_DB))
            return applicationContext.getBean("mongo_db", MongoDB.class);

        return null;
    }
}
