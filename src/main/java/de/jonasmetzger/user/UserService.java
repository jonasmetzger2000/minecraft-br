package de.jonasmetzger.user;

import de.jonasmetzger.dependency.Inject;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    @Inject
    private UserRepository repository;

    @Inject
    JavaPlugin plugin;

    public void onJoin(Player player) {
        final UserProfile userProfile = createOrGetUserProfile(player);
        attachPermissibleObjects(player, userProfile);
    }

    private void attachPermissibleObjects(Player player, UserProfile userProfile) {
        for (UserProfile.Role role : userProfile.getRoles()) {
            player.addAttachment(plugin).setPermission(role.name().toLowerCase(), true);
        }
    }

    private UserProfile createOrGetUserProfile(Player player) {
        UserProfile userProfile;
        if (!repository.exists(player.getUniqueId())) {
            userProfile = new UserProfile(player.getUniqueId(), List.of(UserProfile.Role.DEFAULT), new ArrayList<>(), Instant.now());
            repository.save(userProfile);
        } else {
            userProfile = repository.get(player.getUniqueId());
        }
        return userProfile;
    }
}
