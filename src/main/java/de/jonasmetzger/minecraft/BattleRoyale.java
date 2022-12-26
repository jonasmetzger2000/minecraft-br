package de.jonasmetzger.minecraft;

import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.config.DefaultConfig;
import de.jonasmetzger.database.DatabaseClient;
import de.jonasmetzger.dependency.DependencyInjector;
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
            dependencyInjector.instantiate(DefaultConfig.class);
            dependencyInjector.instantiate(DatabaseClient.class);
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
