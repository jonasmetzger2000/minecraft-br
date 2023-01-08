package de.jonasmetzger.minecraft.events;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.game.SpectatorService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerLeaveEvent implements Listener {

    @Inject
    SpectatorService spectatorService;

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        spectatorService.onPlayerLeave(event);
    }

}
