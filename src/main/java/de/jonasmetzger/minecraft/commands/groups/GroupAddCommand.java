package de.jonasmetzger.minecraft.commands.groups;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.minecraft.commands.CommandFeedback;
import de.jonasmetzger.minecraft.commands.PlayerCommand;
import de.jonasmetzger.user.UserProfile;
import de.jonasmetzger.user.UserService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static de.jonasmetzger.minecraft.commands.CommandFeedback.*;

public class GroupAddCommand extends PlayerCommand {

    @Inject
    UserService userService;

    public GroupAddCommand() {
        super("admin", "groupadd", "Add an group for a player", "/groupadd <player> <group>");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        UserProfile.Group group;
        if (args.length == 2) {
            try {
                UUID playerId = Bukkit.getPlayerUniqueId(args[0]);
                group = UserProfile.Group.valueOf(args[1]);
                if (Objects.nonNull(playerId)) {
                    if (userService.addGroup(playerId, group)) {
                        success(player, "Group added");
                    } else {
                        error(player, String.format("No group with Name %s", args[1]));
                    }
                } else {
                    error(player, String.format("Error resolving name %s", args[0]));
                }
            } catch (IllegalArgumentException e) {
                error(player, String.format("No group with Name %s", args[1]));
            }

        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        } else if (args.length == 2) {
            return Arrays.stream(UserProfile.Group.values()).map(UserProfile.Group::name).toList();
        } else {
            return List.of();
        }
    }
}
