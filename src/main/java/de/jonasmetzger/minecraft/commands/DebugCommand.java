package de.jonasmetzger.minecraft.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class DebugCommand extends PlayerCommand {

    public DebugCommand() {
        super("debug", "Only for testing purposes", "/debug", List.of());
    }

    @Override
    boolean onCommand(Player player, String[] args) {
        return false;
    }
}
