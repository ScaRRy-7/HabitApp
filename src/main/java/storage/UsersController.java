package storage;

import entities.Habit;
import entities.User;
import org.slf4j.*;

/**
 * Класс UsersController отвечает за управление пользователями в базе данных.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class UsersController {
    /**
     * Объект класса UsersStorage для доступа к базе данных пользователей.
     */
    private final UsersStorage usersStorage = UsersStorage.getInstance();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param user новый пользователь
     */
    public void addUserToDatabase(User user) {
        logger.info("Юзер добавляется с помощью UsersController в БД");
        usersStorage.addUser(user);
    }

    /**
     * Получает пользователя из базы данных по его email.
     *
     * @param email email пользователя
     * @return пользователь, найденный по email
     */
    public User getUserFromDatabase(String email) {
        logger.info("Юзер возвращается с помощью запроса UsersController к UsersStorage");
        return usersStorage.getUser(email);
    }

    /**
     * Обновляет информацию о пользователе в базе данных.
     *
     * @param user  обновленный пользователь
     * @param email email пользователя
     */
    public void updateRedactedUser(User user, String email) {
        logger.info("Юзер обновляется");
        usersStorage.removeUser(email);
        usersStorage.addUser(user);
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param email email пользователя
     */
    public void removeUserFromDatabase(String email) {
        logger.info("UsersController делает запрос на удаление юзера к UsersStorage");
        usersStorage.removeUser(email);
    }

    /**
     * Добавляет новую привычку пользователю в базе данных.
     *
     * @param user  пользователь
     * @param habit новая привычка
     */
    public void addNewHabit(User user, Habit habit) {
        logger.info("UsersController делает запрос на добавление привычки юзеру в UsersStorage");
        usersStorage.addHabitToUser(user, habit);
    }

    /**
     * Изменяет существующую привычку пользователя в базе данных.
     *
     * @param user        пользователь
     * @param habit       обновленная привычка
     * @param habitNumber номер привычки, которую нужно изменить
     */
    public void changeHabit(User user, Habit habit, int habitNumber) {
        logger.info("Привычка у юзера обновляется");
        int habitIndex = habitNumber - 1;
        user.getHabits().set(habitIndex, habit);
        usersStorage.addUser(user);
    }

    /**
     * Удаляет привычку пользователя из базы данных.
     *
     * @param user        пользователь
     * @param habitNumber номер привычки, которую нужно удалить
     */
    public void removeHabit(User user, int habitNumber) {
        logger.info("Привычка у юзера удаляется");
        user.getHabits().remove(habitNumber - 1);
        usersStorage.addUser(user);
    }

    /**
     * Блокирует пользователя в базе данных.
     *
     * @param email email пользователя
     */
    public void blockUser(String email) {
        logger.info("Пользователь блокируется");
        usersStorage.getUser(email).setBlocked();
    }
}
