package de.jonasmetzger.minecraft.commands;

import de.jonasmetzger.config.ConfigRepository;
import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.user.UserProfile;
import de.jonasmetzger.user.UserService;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PlayerCommand extends BukkitCommand {

    @Inject
    public ConfigRepository configRepository;

    public PlayerCommand(@NotNull String label, @NotNull String name, @NotNull String description, @NotNull String usageMessage) {
        super(name, description, usageMessage, List.of());
        setLabel(label);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof final Player player) {
            if (hasPermissions(player)) {
                if (!onCommand(player, args)) {
                    player.sendMessage(Component.text(usageMessage));
                }
            } else {
                player.sendMessage(configRepository.getComponent("messages.permissions.missing"));
            }
        }
        return true;
    }

    private boolean hasPermissions(Player player) {
        return player.hasPermission(String.format("%s.%s", getLabel(), getName()));
    }

    protected abstract boolean onCommand(Player player, String[] args);

}
