package com.helpdesk_ticketing_system.issue_data_management.persistence.mongo_db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpdesk_ticketing_system.issue_data_management.entities.Page;
import com.helpdesk_ticketing_system.issue_data_management.exceptions.ResourceNotFoundException;
import com.helpdesk_ticketing_system.issue_data_management.persistence.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;
import java.util.logging.Logger;

public class MongoDB<T> implements Database<T> {
    private final ObjectMapper objectMapper;

    private final MongoCollection<Document> collection;
    
    @Autowired
    public MongoDB(
            @Qualifier("mongo-username") String username,
            @Qualifier("mongo-password") String password,
            @Qualifier("mongo-conn-uri") String connectionURI,
            String databaseName,
            String collectionName) {
        objectMapper = new ObjectMapper();
        MongoClient mongoClient = new MongoDBClient(username,password,connectionURI).getMongoClient();
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

    @Override
    public T getIssueById(Object id, Class<T> targetType) throws Exception {
        MongoCursor<Document> mongoCursor = collection.find(Filters.eq("_id",(String)id)).iterator();
        T fetchedIssue = null;
        try
        {
            if(mongoCursor.available()>0)
                    fetchedIssue = objectMapper.readValue(mongoCursor.next().toJson(),targetType);
        }
        catch (JsonProcessingException e){
            Logger.getLogger("MongoDB").severe(e.getMessage());
            throw  new Exception(e.getMessage());
        }
        finally {
            mongoCursor.close();
        }
        return fetchedIssue;
    }

    @Override
    public List<T> getIssues(
            Object submitted_by,
            Long postedOn,
            Integer limit,
            Page pageDirectionToGo,
            Class<T> targetType
    ) throws Exception {
        List<T> resultSet = new ArrayList<>(limit);

        // creating the query
        List<Bson> queryFiltersList = new LinkedList<>();
        queryFiltersList.add(Filters.eq("submitted_by",submitted_by));
        Bson queryFilters;
        if(pageDirectionToGo.equals(Page.NEXT))
            queryFiltersList.add(Filters.lt("posted_on",postedOn));
        else
            queryFiltersList.add(Filters.gt("posted_on",postedOn));
        queryFilters = Filters.and(queryFiltersList);

        MongoCursor<Document> cursor = null;
        FindIterable<Document> findIterable = collection.find(queryFilters).limit(limit);
        try {
            if (pageDirectionToGo.equals(Page.NEXT)) { // going to next page
                cursor = findIterable.cursor();
                cursor.forEachRemaining(document -> {
                    try {
                        resultSet.add(objectMapper.readValue(document.toJson(), targetType));
                    } catch (JsonProcessingException e) {
                        Logger.getLogger("MongoDB").severe(e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }
                });
            }
            else if (pageDirectionToGo.equals(Page.PREV)) { // going to previous page, then sorting in asc and storing in stack to form desc
                cursor = findIterable.sort(Sorts.ascending("posted_on")).cursor();
                Stack<Document> stack = new Stack<>();
                while (cursor.hasNext()) {
                    stack.push(cursor.next());
                }

                while(!stack.isEmpty())
                {
                    resultSet.add(
                            objectMapper.readValue(
                                    stack.pop().toJson(),targetType
                            )
                    );
                }
            }
        }catch (Exception e){
                Logger.getLogger("MongoDB").severe(e.getMessage());
                throw new Exception(e.getMessage());
        }
        finally {
            if(cursor!=null)
                cursor.close();
        }

        return resultSet;
    }

    @Override
    public T update(Object id, Map<String, Object> updatedFieldValuePairs, Class<T> targetType) throws Exception {
        List<Bson> updateRequests = new LinkedList<>();
        for(String fieldName : updatedFieldValuePairs.keySet()){
            updateRequests.add(
                    Updates.set(fieldName,updatedFieldValuePairs.get(fieldName))
            );
        }

        T oldImage = getIssueById(id,targetType);
        if(oldImage == null)
            throw new ResourceNotFoundException(String.format("Issue with ID '%s' not found...",id));
        try{
            Document newImage = collection.findOneAndUpdate(
                    Filters.eq("_id",id),
                    updateRequests,
                    new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            );
            if(newImage == null)
                throw new ResourceNotFoundException(String.format("Issue with ID '%s' not found...",id));
            return objectMapper.readValue(newImage.toJson(),targetType);
        }
        catch (Exception e){
            Logger.getLogger(this.getClass().getName())
                    .severe("Fields not identified during conversion to Target Class");
            collection.replaceOne(
                    Filters.eq("_id",id),
                    Document.parse(objectMapper.writeValueAsString(oldImage))
            );
            throw e;
        }
    }
}
