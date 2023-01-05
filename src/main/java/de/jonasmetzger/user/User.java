package de.jonasmetzger.user;

import lombok.RequiredArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class User {

    @BsonId
    UUID id;
    List<String> permissions;
    List<Infraction> infractions;

    @RequiredArgsConstructor
    static class Infraction {
        UUID by;
        String description;
        Instant issued;
    }
}
