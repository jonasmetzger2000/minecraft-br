package de.jonasmetzger.minecraft.events;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.user.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerJoinEvent implements Listener {

    @Inject
    private UserService userService;

    @EventHandler
    public void serverJoin(PlayerJoinEvent event) {
        userService.onJoin(event.getPlayer());
    }

}
