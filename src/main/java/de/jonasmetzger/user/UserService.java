package de.jonasmetzger.user;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.minecraft.scoreboard.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {

    @Inject
    private UserRepository repository;

    @Inject
    GroupManager groupManager;

    @Inject
    JavaPlugin plugin;

    public void onJoin(Player player) {
        final UserProfile userProfile = createOrGetUserProfile(player);
        attachPermissibleObjects(player, userProfile);
        groupManager.setPlayerTeam(player, userProfile);
        updateLastSeen(userProfile);
        repository.save(userProfile);
    }

    public UserProfile getProfile(UUID uuid) {
        return repository.get(uuid);
    }

    private void updateLastSeen(UserProfile userProfile) {
        userProfile.setLastJoin(Instant.now());
    }

    private void attachPermissibleObjects(Player player, UserProfile userProfile) {
        for (UserProfile.Group group : userProfile.getGroups()) {
            player.addAttachment(plugin).setPermission(group.name().toLowerCase(), true);
        }
    }

    private UserProfile createOrGetUserProfile(Player player) {
        UserProfile userProfile;
        if (!repository.exists(player.getUniqueId())) {
            userProfile = new UserProfile(player.getUniqueId(), List.of(UserProfile.Group.DEFAULT), new ArrayList<>(), Instant.now(), Instant.now());
            repository.save(userProfile);
        } else {
            userProfile = repository.get(player.getUniqueId());
        }
        return userProfile;
    }
}
