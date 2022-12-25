package de.jonasmetzger.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseClient {

    @Inject
    FileConfiguration fileConfiguration;

    private MongoClient mongoClient;

    @DynamicDependency
    public MongoDatabase connect() {
        mongoClient = MongoClients.create(fileConfiguration.getString("mongo.db.connectionString"));
        final MongoDatabase db = mongoClient.getDatabase(fileConfiguration.getString("mongo.db.database"));
        return db;
    }

}
