package de.jonasmetzger.database;

import com.mongodb.client.MongoCollection;
import de.jonasmetzger.dependency.Inject;

public class ConfigRepository {

    @Inject("config")
    MongoCollection<Configuration> collection;



    class Configuration {

    }
}
