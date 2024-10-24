package services.validate;

import repositories.UsersDAO;

public class AdminRemoverValidator {

    private final UsersDAO usersDAO = UsersDAO.getInstance();

    public boolean isValid(String email) {
        return usersDAO.hasUser(email);
    }
}
