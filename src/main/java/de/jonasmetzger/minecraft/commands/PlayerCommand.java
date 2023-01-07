package de.jonasmetzger.minecraft.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PlayerCommand extends BukkitCommand {
    protected PlayerCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof final Player player) {
            if (!onCommand(player, args)) {
                player.sendMessage(Component.text(usageMessage));
            }
        }
        return true;
    }

    abstract boolean onCommand(Player player, String[] args);

}
