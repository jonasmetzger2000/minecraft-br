package de.jonasmetzger.config;

import de.jonasmetzger.dependency.DynamicDependency;
import de.jonasmetzger.dependency.Inject;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class MongoConfiguration {

    @Inject
    FileConfiguration config;

    @DynamicDependency("mongoConnectionString")
    public String mongoConnectionString() {
        File file = new File("database.yml");
        if (file.exists()) {
            try {
                config.load(file);
                return config.getString("mongo.connectionString");
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException("Invalid Configuration file 'database.yml'", e);
            }
        } else {
            throw new RuntimeException("Database configuration is missing!");
        }
    }

}
