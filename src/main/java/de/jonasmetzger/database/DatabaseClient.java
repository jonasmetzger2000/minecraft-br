package de.jonasmetzger.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.config.ConfigurationValue;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.user.User;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.configuration.file.FileConfiguration;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseClient {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    @Inject
    FileConfiguration config;

    private MongoClientSettings mongoClientSettings() {
        final CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().register("de.jonasmetzger").build());
        final CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(config.getString("mongo.connectionString")))
                .codecRegistry(codecRegistry)
                .build();
    }

    @DynamicDependency
    public MongoDatabase connect() {
        mongoClient = MongoClients.create(mongoClientSettings());
        final CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), pojoCodecRegistry);

        mongoDatabase = mongoClient.getDatabase("battle-royale").withCodecRegistry(codecRegistry);
        return mongoDatabase;
    }

    @DynamicDependency("config")
    public MongoCollection<ConfigurationValue> configCollection() {
        return mongoDatabase.getCollection("config", ConfigurationValue.class);
    }

    @DynamicDependency("user")
    public MongoCollection<User> userCollection() {
        return mongoDatabase.getCollection("users", User.class);
    }

    @DynamicDependency
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
