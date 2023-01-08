package de.jonasmetzger.minecraft.events;

import de.jonasmetzger.dependency.Inject;
import de.jonasmetzger.game.SpectatorService;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeSwitchEvent implements Listener {

    @Inject
    SpectatorService spectatorService;

    @EventHandler
    public void onGamemodeSwitch(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode().equals(GameMode.ADVENTURE)) {
            event.setCancelled(true);
            return;
        }
        spectatorService.gamemodeSwitch(event.getPlayer(), event.getNewGameMode());
    }

}
