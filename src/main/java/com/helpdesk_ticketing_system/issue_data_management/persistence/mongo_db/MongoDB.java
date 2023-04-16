package com.helpdesk_ticketing_system.issue_data_management.persistence.mongo_db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpdesk_ticketing_system.issue_data_management.persistence.Database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonString;
import org.bson.Document;

import java.util.logging.Logger;

public class MongoDB<T> implements Database<T> {
    private final ObjectMapper objectMapper;

    private final MongoCollection<Document> collection;
    
    public MongoDB(String databaseName, String collectionName) {
        objectMapper = new ObjectMapper();
        MongoClient mongoClient = MongoDBClient.initializeClient().getMongoClient();
        collection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
    }

    @Override
    public String saveToDb(T newData) throws Exception {
        try{
            InsertOneResult insertOneResult = collection.insertOne(
                    Document.parse(
                            objectMapper.writeValueAsString(newData)
                    )
            );
            BsonString id;
            if(insertOneResult.wasAcknowledged() && insertOneResult.getInsertedId()!=null)
            {
                id = insertOneResult.getInsertedId().asString();
                return id.getValue();
            }
        }catch (Exception e){
            Logger.getLogger("MongoDB").severe(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return null;
    }
}
