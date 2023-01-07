package de.jonasmetzger.minecraft.commands;

import org.bukkit.entity.Player;

public class DebugCommand extends PlayerCommand {

    public DebugCommand() {
        super("admin", "debug", "Only for testing purposes", "/debug");
    }

    @Override
    boolean onCommand(Player player, String[] args) {
        return false;
    }
}
