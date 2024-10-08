package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class UsersStorage {

    private static final UsersStorage usersStorage = new UsersStorage();

    private List<User> users = new ArrayList<>();

    private UsersStorage() {}

    public static UsersStorage getInstance() {
        return usersStorage;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean hasUser(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
