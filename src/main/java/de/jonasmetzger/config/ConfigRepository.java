package de.jonasmetzger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import de.jonasmetzger.dependency.Inject;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class ConfigRepository {

    @Inject("config")
    MongoCollection<Configuration> collection;

    @Inject
    ObjectMapper objectMapper;

    public <T> T get(String key, Class<?> classToSave) {
        return null;
    }

    public Map<String, Object> get(String key) {
        Configuration config = collection.find(eq("key", key)).first();
        if (Objects.nonNull(config)) {
            return config.value;
        } else {
            throw new RuntimeException(String.format("Key %s does not exists", key));
        }
    }

    public void save(String key, Map<String, Object> value) {
        save(key, value, true);
    }

    public void save(String key, Object value, boolean update) {
        Configuration configuration = collection.find(eq("key", key)).limit(1).first();
        if (Objects.isNull(configuration)) {
            Map serializedValue = objectMapper.convertValue(value, Map.class);
            collection.insertOne(new Configuration(key, serializedValue));
        } else {
            if (update) collection.findOneAndReplace(eq("key", key), new Configuration(key, value));
        }
    }
}
