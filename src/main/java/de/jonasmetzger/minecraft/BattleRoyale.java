package de.jonasmetzger.minecraft;

import de.jonasmetzger.config.DefaultConfiguration;
import de.jonasmetzger.config.ConfigRepository;
import de.jonasmetzger.database.DatabaseClient;
import de.jonasmetzger.dependency.DependencyInjector;
import de.jonasmetzger.minecraft.events.ServerJoinEvent;
import de.jonasmetzger.user.UserRepository;
import de.jonasmetzger.user.UserService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BattleRoyale extends JavaPlugin {

    final long lastModified = getFile().lastModified();
    final DependencyInjector dependencyInjector = new DependencyInjector();

    @Override
    public void onEnable() {
        devReload();
        try {
            addDefaultDependencies();
            dependencyInjector.instantiate(DefaultConfiguration.class).load();
            dependencyInjector.instantiate(DatabaseClient.class);
            // repos
            dependencyInjector.instantiate(ConfigRepository.class);
            dependencyInjector.instantiate(UserRepository.class);
            // service
            dependencyInjector.instantiate(UserService.class);
            // events
            dependencyInjector.instantiate(ServerJoinEvent.class);

            registerEvents();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Dependency Injection failed", e);
        }
    }

    void registerEvents() {
        for (Listener listener : dependencyInjector.getDependencies(Listener.class)) {
            System.out.printf("Registering Listener %s%n", listener.getClass().getCanonicalName());
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    void addDefaultDependencies() {
        dependencyInjector.registerDependency(FileConfiguration.class, getConfig());
        dependencyInjector.registerDependency(JavaPlugin.class, this);
        dependencyInjector.registerDependency(Logger.class, getLogger());
    }

    void devReload() {
        new BukkitRunnable() {
            public void run() {
                if (getFile().lastModified() > lastModified) {
                    cancel();
                    Bukkit.reload();
                }
            }
        }.runTaskTimer(this, 20, 20);
    }
}
