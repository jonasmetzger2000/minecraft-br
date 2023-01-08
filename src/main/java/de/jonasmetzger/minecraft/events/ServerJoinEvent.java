package de.jonasmetzger.minecraft.events;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.game.SpectatorService;
import de.jonasmetzger.user.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerJoinEvent implements Listener {

    @Inject
    private UserService userService;

    @Inject
    private SpectatorService spectatorService;

    @EventHandler
    public void serverJoin(PlayerJoinEvent event) {
        userService.onJoin(event.getPlayer());
        spectatorService.onPlayerJoin(event.getPlayer());
    }

}
