package storage;

public final class UsersController {

    private final UsersStorage usersStorage = UsersStorage.getInstance();

    public void addUserToDatabase(User user) {
        usersStorage.addUser(user);
    }

    public User getUserFromDatabase(String email) {
        return usersStorage.getUser(email);
    }
}
