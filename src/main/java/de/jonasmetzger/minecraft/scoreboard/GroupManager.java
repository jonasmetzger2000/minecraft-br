package de.jonasmetzger.minecraft.scoreboard;

import de.jonasmetzger.config.ConfigRepository;
import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.user.UserProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupManager {

    @Inject
    ConfigRepository configRepository;

    public void setPlayerTeam(Player player, UserProfile userProfile) {
//        player.displayName(Component.text("sdfg"));
        final Team team = createOrGetTeam(userProfile.getGroups());
        team.addPlayer(player);
        player.displayName(getPlayerPrefix(userProfile.getGroups()).append(player.name()));
    }

    private Team createOrGetTeam(List<UserProfile.Group> groups) {
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        final String teamKey = groups.stream().map(UserProfile.Group::name).collect(Collectors.joining());
        Team team = scoreboard.getTeam(teamKey);
        if (Objects.isNull(team)) {
            team = scoreboard.registerNewTeam(teamKey);
            team.prefix(getPlayerPrefix(groups));
        }
        return team;
    }

    private Component getPlayerPrefix(List<UserProfile.Group> groups) {
        return groups.stream().map(this::getTeamDatabaseKey).map(configRepository::getComponent).reduce(Component::append).get();
    }

    private String getTeamDatabaseKey(UserProfile.Group group) {
        return String.format("group.%s.prefix", group.name().toLowerCase());
    }
}
