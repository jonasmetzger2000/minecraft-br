package de.jonasmetzger.game;

import java.util.List;
import java.util.UUID;

public class Game {

    UUID id;
    Status status;
    List<Participant> participants;

    static class Participant {
        UUID id;
        String kit;
        Integer placement;
        List<UUID> kills;
    }

    enum Status {
        IDLE,
        STARTING,
        ONGOING,
        FINISHED,
    }

}
