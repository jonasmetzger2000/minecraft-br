package de.jonasmetzger.config.serializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ComponentSerializer  {

    public String serialize(Component obj) {
        return GsonComponentSerializer.gson().serialize(obj);
    }

    public Component deserialize(String json) {
        return GsonComponentSerializer.gson().deserialize(json);
    }
}
