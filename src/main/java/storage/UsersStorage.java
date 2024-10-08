package storage;

import java.util.*;

public final class UsersStorage {

    private static final UsersStorage usersStorage = new UsersStorage();

    private Map<String, User> users = new HashMap<>();

    private UsersStorage() {}

    public static UsersStorage getInstance() {
        return usersStorage;
    }

    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public boolean hasUser(String email) {
        return users.get(email) != null;
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public void removeUser(String email) {
        users.remove(email);
    }

}
