package de.jonasmetzger.database.codec;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ComponentSerializer implements CustomSerializer<Component> {

    @Override
    public String serialize(Component obj) {
        return GsonComponentSerializer.gson().serialize(obj);
    }

    @Override
    public Component deserialize(String str) {
        return GsonComponentSerializer.gson().deserialize(str);
    }
}
