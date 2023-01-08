package de.jonasmetzger.minecraft.commands;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@AllArgsConstructor
public class Feedback {

    private final Component prefix;
    private Component message;

    public static Feedback error() {
        return new Feedback(Component.empty().color(NamedTextColor.RED), Component.text("Error."));
    }

    public static Feedback success() {
        return new Feedback(Component.empty().color(NamedTextColor.GREEN), Component.text("Success."));
    }

    public Feedback message(String desc) {
        message = Component.text(desc);
        return this;
    }

    public Component getComponent() {
        return prefix.append(message);
    }

}
