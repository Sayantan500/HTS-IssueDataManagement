package com.helpdesk_ticketing_system.issue_data_management;

import com.helpdesk_ticketing_system.issue_data_management.entities.Issue;
import com.helpdesk_ticketing_system.issue_data_management.persistence.Database;
import com.helpdesk_ticketing_system.issue_data_management.persistence.mongo_db.MongoDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean(name = {"mongo_db"})
    public Database<?> getMongoDatabase(){
        String db = System.getenv("mongodb_database");
        String collection = System.getenv("mongodb_collection");
        System.out.println("mongoDatabase = " + db);
        System.out.println("mongoCollection = " + collection);
        return new MongoDB<Issue>(db,collection);
    }


}
