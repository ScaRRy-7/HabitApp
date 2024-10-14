package checkers;

import entities.User;
import storage.UsersController;

/**
 * Класс PasswordChecker отвечает за проверку пароля пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class PasswordChecker {
    /**
     * Объект класса UsersController для получения пользователя из базы данных.
     */
    private final UsersController usersController = new UsersController();

    /**
     * Проверяет, совпадает ли введенный пароль с паролем пользователя в базе данных.
     *
     * @param email    email пользователя
     * @param password пароль, который нужно проверить
     * @return true, если пароль верный, иначе false
     */
    public boolean checkPassword(String email, String password) {
        User user = usersController.getUserFromDatabase(email);
        return user.getPassword().equals(password);
    }
}
