package checker;

import storage.User;
import storage.UsersController;

public class PasswordChecker {

    private final UsersController usersController = new UsersController();

    public boolean checkPassword(String email, String password) {
        User user = usersController.getUserFromDatabase(email);

        if (user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}
