package de.jonasmetzger.minecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerJoinEvent implements Listener {

    @EventHandler
    public void serverJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
    }

}
