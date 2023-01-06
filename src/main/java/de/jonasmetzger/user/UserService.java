package de.jonasmetzger.user;

import de.jonasmetzger.dependency.Inject;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UserService {

    @Inject
    private UserRepository repository;

    public void firstJoin(Player player) {
        if (!repository.exists(player.getUniqueId())) {
            User newUser = new User(player.getUniqueId(), User.Role.DEFAULT, new ArrayList<>(), new ArrayList<>());
            repository.save(newUser);
        }
    }
}
