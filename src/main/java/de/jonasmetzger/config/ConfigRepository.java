package de.jonasmetzger.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneOptions;
import de.jonasmetzger.database.codec.ComponentSerializer;
import de.jonasmetzger.database.codec.CustomSerializer;
import de.jonasmetzger.dependency.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.mod;

public class ConfigRepository {

    @Inject("config")
    MongoCollection<Configuration> collection;

    private Map<Class<?>, CustomSerializer<?>> customSerializers = new HashMap<>();

    public ConfigRepository() {
        customSerializers.put(Component.class, new ComponentSerializer());
    }

    public <T> T get(String key, Class<?> classToSave) {
        if (customSerializers.containsKey(classToSave)) {
            return (T) customSerializers.get(classToSave).deserialize(get(key));
        } else {
            throw new RuntimeException(String.format("No Serializer found for class %s", classToSave.getName()));
        }
    }

    public String get(String key) {
        Configuration config = collection.find(eq("key", key)).first();
        if (Objects.nonNull(config)) {
            return config.value;
        } else {
            throw new RuntimeException(String.format("Key %s does not exists", key));
        }
    }

    public <T> void save(String key, Class<T> classToSave, T objToSave) {
        save(key, classToSave, objToSave, false);
    }

    public <T> void save(String key, Class<T> classToSave, T objToSave, boolean update) {
        if (customSerializers.containsKey(classToSave)) {
            CustomSerializer<T> customSerializer = (CustomSerializer<T>) customSerializers.get(classToSave);
            save(key, customSerializer.serialize(objToSave), update);
        } else {
            throw new RuntimeException(String.format("No Serializer found for class %s", classToSave.getName()));
        }
    }

    public void save(String key, String value) {
        save(key, value, false);
    }

    public void save(String key, String value, boolean update) {
        Configuration configuration = collection.find(eq("key", key)).limit(1).first();
        if (Objects.isNull(configuration)) {
            collection.insertOne(configuration);
        } else {
            if (update) collection.findOneAndReplace(eq("key", value), new Configuration(key, value));
        }
    }
}
