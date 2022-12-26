package de.jonasmetzger.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseClient {

    @Inject
    FileConfiguration fileConfiguration;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    @DynamicDependency
    public MongoDatabase connect() {
        mongoClient = MongoClients.create(fileConfiguration.getString("mongo.db.connectionString"));
        mongoDatabase = mongoClient.getDatabase(fileConfiguration.getString("mongo.db.database"));
        return mongoDatabase;
    }

    @DynamicDependency("user")
    public MongoCollection userCollection() {
        return mongoDatabase.getCollection("users");
    }
}
