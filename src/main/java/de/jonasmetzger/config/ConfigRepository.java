package de.jonasmetzger.config;

import com.mongodb.client.MongoCollection;
import de.jonasmetzger.config.serializer.ComponentSerializer;
import de.jonasmetzger.config.serializer.ConfigurationSerializableSerializer;
import de.jonasmetzger.dependency.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class ConfigRepository {

    @Inject("config")
    MongoCollection<ConfigurationValue> configCollection;

    private final ComponentSerializer componentSerializer = new ComponentSerializer();
    private final ConfigurationSerializableSerializer configurationSerializableSerializer = new ConfigurationSerializableSerializer();

    public void save(String key, ConfigurationSerializable configurationSerializable) {
        save(key, configurationSerializableSerializer.serialize(configurationSerializable));
    }

    public void save(String key, Component component) {
        save(key, componentSerializer.serialize(component));
    }

    public void save(String key, String value) {
        ConfigurationValue configuration = configCollection.find(eq("_id", key)).limit(1).first();
        if (Objects.isNull(configuration)) {
            configCollection.insertOne(new ConfigurationValue(key, value));
        } else {
            configCollection.findOneAndReplace(eq("_id", key), new ConfigurationValue(key, value));
        }
    }

    public ItemStack getItemStack(String key) {
        return (ItemStack) configurationSerializableSerializer.deserialize(get(key));
    }

    public Component getComponent(String key) {
        return componentSerializer.deserialize(get(key));
    }

    private String get(String key) {
        return configCollection.find(eq("_id", key)).limit(1).first().value;
    }
}
