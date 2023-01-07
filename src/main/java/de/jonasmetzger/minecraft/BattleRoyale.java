package de.jonasmetzger.minecraft;

import de.jonasmetzger.config.DefaultConfiguration;
import de.jonasmetzger.config.ConfigRepository;
import de.jonasmetzger.database.DatabaseClient;
import de.jonasmetzger.dependency.DependencyInjector;
import de.jonasmetzger.minecraft.commands.SaveItemCommand;
import de.jonasmetzger.minecraft.events.ServerJoinEvent;
import de.jonasmetzger.user.UserRepository;
import de.jonasmetzger.user.UserService;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
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
            // commands
            dependencyInjector.instantiate(SaveItemCommand.class);

            registerEvents();
            registerCommands();
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

    @SneakyThrows
    void registerCommands() {
        final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        f.setAccessible(true);
        final CommandMap commandMap = (CommandMap) f.get(getServer());
        for (BukkitCommand bukkitCommand : dependencyInjector.getDependencies(BukkitCommand.class)) {
            System.out.printf("Registering Command %s%n", bukkitCommand.getLabel());
            commandMap.register(bukkitCommand.getLabel(), bukkitCommand);
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
