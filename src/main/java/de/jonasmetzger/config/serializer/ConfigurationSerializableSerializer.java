package de.jonasmetzger.config.serializer;

import com.google.gson.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationSerializableSerializer {

    private final Gson gson = new GsonBuilder().disableHtmlEscaping().registerTypeHierarchyAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter()).create();

    public String serialize(ConfigurationSerializable obj) {
        return gson.toJson(obj);
    }

    public ConfigurationSerializable deserialize(String json) {
        return gson.fromJson(json, ConfigurationSerializable.class);
    }

    static class ConfigurationSerializableAdapter implements JsonSerializer<ConfigurationSerializable>, JsonDeserializer<ConfigurationSerializable> {

        @Override
        public JsonElement serialize(ConfigurationSerializable src, Type typeOfSrc, JsonSerializationContext context) {
            final Map<String, Object> result = new LinkedHashMap<>();
            result.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(src.getClass()));
            result.putAll(src.serialize());
            return context.serialize(result);
        }

        @Override
        public ConfigurationSerializable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                final JsonElement value = entry.getValue();
                if (value.isJsonObject() &&value.getAsJsonObject().has(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                    result.put(entry.getKey(), this.deserialize(value, value.getClass(), context));
                } else {
                    result.put(entry.getKey(), context.deserialize(value, Object.class));
                }
            }
            return ConfigurationSerialization.deserializeObject(result);
        }
    }
}
