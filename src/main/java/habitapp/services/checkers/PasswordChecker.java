package services.checkers;

import services.entities.User;
import repositories.UsersRepository;

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
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Проверяет, совпадает ли введенный пароль с паролем пользователя в базе данных.
     *
     * @param email    email пользователя
     * @param password пароль, который нужно проверить
     * @return true, если пароль верный, иначе false
     */
    public boolean checkPassword(String email, String password) {
        User user = usersRepository.getUserFromDatabase(email);
        return user.getPassword().equals(password);
    }
}
