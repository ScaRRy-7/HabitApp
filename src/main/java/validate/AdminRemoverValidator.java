package validate;

import storage.UsersStorage;

public class AdminRemoverValidator {

    private final UsersStorage usersStorage = UsersStorage.getInstance();

    public boolean isValid(String email) {
        return usersStorage.hasUser(email);
    }
}
