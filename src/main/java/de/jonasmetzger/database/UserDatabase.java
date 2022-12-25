package de.jonasmetzger.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jonasmetzger.dependency.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class UserDatabase {

    @Inject
    FileConfiguration config;

    @Inject
    MongoDatabase mongoDatabase;

    @RequiredArgsConstructor
    static class User {
        private final UUID id;
        private final Role role;

        enum Role {
            NORMAL,
            VIP,
            MVP,
            PRO,
            MEDIA,
            MODERATOR_TRIAL,
            MODERATOR,
            ADMIN
        }
    }


}
