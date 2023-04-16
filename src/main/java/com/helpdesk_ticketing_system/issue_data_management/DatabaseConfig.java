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
        return new MongoDB<Issue>(
                mongoDbUsername(),
                mongoDbUserPassword(),
                mongoDbConnectionURI(),
                mongoDatabaseName(),
                mongoCollectionName());
    }

    @Bean(name = "mongo-username")
    public String mongoDbUsername(){
        return System.getenv("mongodb_username");
    }

    @Bean(name = "mongo-password")
    public String mongoDbUserPassword(){
        return System.getenv("mongodb_password");
    }

    @Bean(name = "mongo-conn-uri")
    public String mongoDbConnectionURI(){
        return System.getenv("mongodb_connect_uri");
    }

    @Bean
    public String mongoDatabaseName(){
        return System.getenv("mongo_db_name");
    }

    @Bean
    public String mongoCollectionName(){
        return System.getenv("mongo_collection_name");
    }
}
