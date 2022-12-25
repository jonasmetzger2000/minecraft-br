package de.jonasmetzger.config;

import de.jonasmetzger.dependency.Inject;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class DefaultConfig {

    @Inject
    FileConfiguration config;

    @SneakyThrows
    public void load() {
        File file = new File("battleroyale.yml");
        if (file.exists()) config.load(file);
        addDefaults();
        config.save(file);
    }

    private void addDefaults() {
        // Database
        setDefault("mongo.db.connectionString", "mongodb://username:password@mongo:27017/");
        setDefault("mongo.db.database", "battle-royale");

        // Roles
        setDefault("roles.normal.prefix", Component.empty().color(NamedTextColor.WHITE));
        setDefault("roles.vip.prefix", Component.empty().color(NamedTextColor.GREEN));
        setDefault("roles.mvp.prefix", Component.empty().color(NamedTextColor.BLUE));
        setDefault("roles.pro.prefix", Component.empty().color(NamedTextColor.GOLD));
        setDefault("roles.media.prefix", Component.empty().color(NamedTextColor.AQUA));
        setDefault("roles.moderator_trial.prefix", Component.empty().color(NamedTextColor.DARK_PURPLE));
        setDefault("roles.moderator.prefix", Component.empty().color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC));
        setDefault("roles.admin.prefix", Component.empty().color(NamedTextColor.RED));
    }

    private void setDefault(String path, Object value) {
        if (!config.isSet(path)) {
            System.out.println("Setting default Value to: " + value);
            config.set(path, value);
        }
    }

}
