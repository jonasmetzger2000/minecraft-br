package de.jonasmetzger.user;

import com.mongodb.client.MongoCollection;
import de.jonasmetzger.dependency.Inject;

import java.util.Objects;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class UserRepository {

    @Inject("user")
    private MongoCollection<User> userCollection;

    public void save(User userToSave) {
        User user = userCollection.find(eq("_id", userToSave.getId())).limit(1).first();
        if (Objects.isNull(user)) {
            userCollection.insertOne(userToSave);
        } else {
            userCollection.findOneAndReplace(eq("_id", userToSave.getId()), user);
        }
    }

    public boolean exists(UUID uuid) {
        return Objects.nonNull(userCollection.find(eq("_id", uuid)).limit(1).first());
    }

    public User get(UUID uuid) {
        return userCollection.find(eq("_id", uuid)).limit(1).first();
    }

}
