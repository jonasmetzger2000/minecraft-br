package de.jonasmetzger.minecraft.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class CommandFeedback {

    public static void success(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.GREEN));
    }

    public static void warning(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.YELLOW));
    }

    public static void error(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.RED));
    }
}
