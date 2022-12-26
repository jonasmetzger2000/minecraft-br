package de.jonasmetzger.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;

public class DatabaseClient {

    @Inject("mongoConnectionString")
    String connectionString;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    @DynamicDependency
    public MongoDatabase connect() {
        mongoClient = MongoClients.create(connectionString);
        mongoDatabase = mongoClient.getDatabase("battle-royale");
        return mongoDatabase;
    }

    @DynamicDependency("user")
    public MongoCollection userCollection() {
        return mongoDatabase.getCollection("users");
    }
}
