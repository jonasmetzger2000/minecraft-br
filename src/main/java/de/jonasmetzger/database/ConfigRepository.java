package de.jonasmetzger.database;

import com.mongodb.client.MongoCollection;
import de.jonasmetzger.dependency.Inject;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.mongodb.client.model.Filters.eq;

public class ConfigRepository {

    @Inject("config")
    MongoCollection<Configuration> collection;

    public void save(String key, Object obj) {
        collection.insertOne(new Configuration(key, obj.toString()));
    }

    public <T> T get(Class<T> clazz, String key) {
        final Configuration configuration = collection.find(eq("key", key)).first();
        return clazz.cast(configuration.value);
    }

    @AllArgsConstructor
    @Value
    class Configuration {
        String key;
        String value;
    }
}
