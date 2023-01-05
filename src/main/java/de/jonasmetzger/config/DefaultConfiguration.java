package de.jonasmetzger.config;

import de.jonasmetzger.dependency.Inject;
import lombok.SneakyThrows;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class DefaultConfiguration {

    @Inject
    FileConfiguration config;

    @SneakyThrows
    public void load() {
        File file = new File("config.yml");
        if (file.exists()) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException("Invalid Configuration file 'config.yml'", e);
            }
        } else {
            throw new RuntimeException("Configuration is missing!");
        }
    }

}
