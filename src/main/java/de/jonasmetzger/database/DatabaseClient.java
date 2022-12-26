package de.jonasmetzger.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseClient {

    @Inject("mongoConnectionString")
    String connectionString;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    @DynamicDependency
    public MongoDatabase connect() {
        mongoClient = MongoClients.create(mongoClientSettings());
        mongoDatabase = mongoClient.getDatabase("battle-royale");
        return mongoDatabase;
    }

    @DynamicDependency("config")
    public MongoCollection<ConfigRepository.Configuration> configCollection() {
        return mongoDatabase.getCollection("config", ConfigRepository.Configuration.class);
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
