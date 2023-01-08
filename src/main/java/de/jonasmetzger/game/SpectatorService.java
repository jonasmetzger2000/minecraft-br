package de.jonasmetzger.game;

import de.jonasmetzger.dependency.Inject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.GameMode.*;

public class SpectatorService {

    @Inject
    JavaPlugin plugin;

    public void gamemodeSwitch(Player player, GameMode gameMode) {
        checkSpectator(player, gameMode);
    }

    public void onPlayerJoin(Player player) {
        checkSpectator(player, player.getGameMode());
    }

    private void checkSpectator(Player player, GameMode gameMode) {
        if (isSpectator(gameMode)) {
            hideSpectatorFromPlayers(player);
        } else {
            showSpectatorFromPlayers(player);
        }
    }

    private void hideSpectatorFromPlayers(Player spectator) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isPlayer(player.getGameMode()) && player != spectator) {
                player.hidePlayer(plugin, spectator);
            }
        }
    }

    private void showSpectatorFromPlayers(Player oldSpectator) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, oldSpectator);
        }
    }

    private boolean isSpectator(GameMode gameMode) {
        return gameMode.equals(CREATIVE) || gameMode.equals(SPECTATOR);
    }

    private boolean isPlayer(GameMode gameMode) {
        return gameMode.equals(SURVIVAL);
    }
}
