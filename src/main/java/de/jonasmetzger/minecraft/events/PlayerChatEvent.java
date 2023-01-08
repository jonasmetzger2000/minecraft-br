package de.jonasmetzger.minecraft.events;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.game.SpectatorService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatEvent implements Listener {

    @Inject
    SpectatorService spectatorService;

    @EventHandler
    public void chat(AsyncChatEvent event) {
        final Player player = event.getPlayer();
        if (spectatorService.isSpectator(player.getGameMode())) {
            event.setCancelled(true);
            Bukkit.getServer().broadcast(Component.text("<").append(player.displayName()).append(Component.text("> ")).append(event.message()));
        }
    }
}
