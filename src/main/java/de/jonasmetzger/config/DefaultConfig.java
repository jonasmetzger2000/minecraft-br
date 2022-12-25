package de.jonasmetzger.config;

import de.jonasmetzger.dependency.Inject;
import lombok.SneakyThrows;
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
        setDefault("mongo.db.username", "username");
        setDefault("mongo.db.password", "password");
        setDefault("mongo.db.database", "battle-royale");
    }

    private void setDefault(String path, Object value) {
        if (!config.isSet(path)) {
            System.out.println("Setting default Value to: " + value);
            config.set(path, value);
        }
    }

}
