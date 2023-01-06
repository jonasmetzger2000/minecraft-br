package de.jonasmetzger.user;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import de.jonasmetzger.dependency.Inject;

import java.util.Objects;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class UserRepository {

    @Inject("user")
    private MongoCollection<UserProfile> userCollection;

    public void save(UserProfile userProfileToSave) {
        userCollection.replaceOne(eq("_id", userProfileToSave.getId()), userProfileToSave, new ReplaceOptions().upsert(true));
    }

    public boolean exists(UUID uuid) {
        return Objects.nonNull(userCollection.find(eq("_id", uuid)).limit(1).first());
    }

    public UserProfile get(UUID uuid) {
        return userCollection.find(eq("_id", uuid)).limit(1).first();
    }

}
