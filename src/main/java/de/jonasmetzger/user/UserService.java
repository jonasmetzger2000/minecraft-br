package de.jonasmetzger.user;

import de.jonasmetzger.dependency.Inject;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    @Inject
    private UserRepository repository;

    public void firstJoin(Player player) {
        if (!repository.exists(player.getUniqueId())) {
            User newUser = new User(player.getUniqueId(), List.of(User.Role.DEFAULT), new ArrayList<>(), new ArrayList<>());
            repository.save(newUser);
        }
    }
}
