package com.helpdesk_ticketing_system.issue_data_management.persistence.mongo_db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

class MongoDBClient
{
    private MongoClient mongoClient;

    @Autowired
    MongoDBClient(String USERNAME, String PASSWORD, String BASE_URI) {
        String connectionURI = String.format(BASE_URI, USERNAME, PASSWORD);
        ConnectionString connectionString = new ConnectionString(connectionURI);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        try {
            mongoClient = MongoClients.create(settings);
        } catch (Exception e) {
            Logger.getLogger("MongoDBClient").severe(e.getMessage());
        }
    }

    public MongoClient getMongoClient(){
        return this.mongoClient;
    }
}
