package com.helpdesk_ticketing_system.issue_data_management.persistence.mongo_db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.logging.Logger;

class MongoDBClient
{
    private MongoClient mongoClient;

    private static volatile MongoDBClient mongoDbClient;

    private MongoDBClient() {
        //Todo : get from system env
        String USERNAME = "admin";
        String PASSWORD = "Password";
        String BASE_URI = "mongodb+srv://%s:%s@cluster0.lcgntfe.mongodb.net/?retryWrites=true&w=majority";

        String counnectionString = String.format(BASE_URI, USERNAME, PASSWORD);
        ConnectionString connectionString = new ConnectionString(counnectionString);

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

    public static MongoDBClient initializeClient(){
        if(mongoDbClient==null){
            synchronized (MongoDBClient.class){
                if(mongoDbClient==null)
                    mongoDbClient = new MongoDBClient();
            }
        }
        return mongoDbClient;
    }

    public MongoClient getMongoClient(){
        return this.mongoClient;
    }
}
