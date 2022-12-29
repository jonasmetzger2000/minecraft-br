package de.jonasmetzger.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.config.Configuration;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.*;

public class DatabaseClient {

    @Inject("mongoConnectionString")
    String connectionString;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    @DynamicDependency
    public MongoDatabase connect() {

        mongoClient = MongoClients.create(mongoClientSettings());
        final CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), pojoCodecRegistry);
        mongoDatabase = mongoClient.getDatabase("battle-royale").withCodecRegistry(codecRegistry);
        return mongoDatabase;
    }

    @DynamicDependency("config")
    public MongoCollection<Configuration> configCollection() {
        return mongoDatabase.getCollection("config", Configuration.class);
    }

    @DynamicDependency
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    private MongoClientSettings mongoClientSettings() {
        final CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().register("de.jonasmetzger").build());
        final CodecRegistry codecRegistry = fromRegistries(getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .codecRegistry(codecRegistry)
                .build();
    }
}
