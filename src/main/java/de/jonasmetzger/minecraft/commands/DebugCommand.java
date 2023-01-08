package de.jonasmetzger.minecraft.commands;

import de.jonasmetzger.dependency.Inject;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DebugCommand extends PlayerCommand {

    @Inject
    JavaPlugin plugin;

    public DebugCommand() {
        super("admin", "debug", "Only for testing purposes", "/debug");
    }

    @Override
    protected Feedback onCommand(Player player, String[] args) {
        return Feedback.success();
    }
}
