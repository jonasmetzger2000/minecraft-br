package de.jonasmetzger.minecraft;

import de.jonasmetzger.config.MongoConfiguration;
import de.jonasmetzger.database.ConfigRepository;
import de.jonasmetzger.database.DatabaseClient;
import de.jonasmetzger.dependency.DependencyInjector;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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
            dependencyInjector.instantiate(MongoConfiguration.class);
            dependencyInjector.instantiate(DatabaseClient.class);
            final ConfigRepository configRepository = dependencyInjector.instantiate(ConfigRepository.class);
            configRepository.save("test", "jonas");


        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Dependency Injection failed", e);
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
