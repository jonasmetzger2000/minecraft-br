package de.jonasmetzger.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @BsonId
    UUID id;
    List<Role> roles;
    List<String> permissions;
    List<Infraction> infractions;

    @Value
    @AllArgsConstructor
    static class Infraction {
        String by;
        String description;
        Instant issuedAt;
    }

    enum Role {
        DEFAULT,
        VIP,
        MVP,
        PRO,
        MEDIA,
        MODERATOR_TRIAL,
        MODERATOR,
        ADMIN;
    }
}
