package de.jonasmetzger.config;

import com.mongodb.client.MongoCollection;
import de.jonasmetzger.dependency.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.mongodb.client.model.Filters.eq;

public class ConfigRepository {

    @Inject("config")
    MongoCollection<Configuration> collection;

    public void save(String key, String obj) {
        collection.insertOne(new Configuration(key, obj));
    }

    public Configuration get(String key) {
        final Configuration configuration = collection.find(eq("key", key)).first();
        return configuration;
    }
}
