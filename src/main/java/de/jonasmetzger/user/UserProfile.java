package de.jonasmetzger.user;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @BsonId
    UUID id;
    List<Group> groups;
    List<Infraction> infractions;
    Instant firstJoin;
    Instant lastJoin;

    @Value
    @AllArgsConstructor
    static class Infraction {
        String by;
        String description;
        Instant issuedAt;
    }

    @Value
    @AllArgsConstructor
    static class Games {
        UUID gameId;
        Integer placement;
    }

    public enum Group {
        DEFAULT,
        VIP,
        MVP,
        PRO,
        MEDIA,
        MODERATOR,
        ADMIN;
    }
}
