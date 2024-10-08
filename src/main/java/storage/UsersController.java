package storage;

public final class UsersController {

    private static final UsersController usersController = new UsersController();

    private final UsersStorage usersStorage = UsersStorage.getInstance();

    private UsersController() {}

    public static UsersController getInstance() {
        return usersController;
    }

    public void addUserToDatabase(User user) {
        usersStorage.addUser(user);
    }

}
