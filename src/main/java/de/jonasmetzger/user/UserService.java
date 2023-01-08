package de.jonasmetzger.user;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.minecraft.scoreboard.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserService {

    @Inject
    private UserRepository repository;

    @Inject
    GroupManager groupManager;

    @Inject
    JavaPlugin plugin;

    public void onJoin(Player player) {
        final UserProfile userProfile = createOrGetUserProfile(player.getUniqueId());
        attachPermissibleObjects(player, userProfile);
        groupManager.setPlayerTeam(player, userProfile);
        updateLastSeen(userProfile);
        repository.save(userProfile);
    }

    public boolean addGroup(UUID uuid, UserProfile.Group group) {
        final UserProfile userProfile = createOrGetUserProfile(uuid);
        if (Objects.nonNull(userProfile)) {
            if (!userProfile.getGroups().contains(group)) {
                userProfile.groups.add(group);
                repository.save(userProfile);
            }
            return true;
        }
        return false;
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

    private UserProfile createOrGetUserProfile(UUID playerId) {
        UserProfile userProfile;
        if (!repository.exists(playerId)) {
            userProfile = new UserProfile(playerId, new ArrayList<>(List.of(UserProfile.Group.DEFAULT)), new ArrayList<>(), Instant.now(), Instant.now());
            repository.save(userProfile);
        } else {
            userProfile = repository.get(playerId);
        }
        return userProfile;
    }
}
