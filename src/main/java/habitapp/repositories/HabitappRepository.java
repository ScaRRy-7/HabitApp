package habitapp.repositories;

import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import org.slf4j.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс UsersController отвечает за управление пользователями в базе данных.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class HabitappRepository {
    /**
     * Объект класса UsersStorage для доступа к базе данных пользователей.
     */
    private final HabitappDAO habitappDAO = HabitappDAO.getInstance();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(HabitappRepository.class);

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param user новый пользователь
     */
    public void addUserToDatabase(User user) throws UserIllegalRequestException {
        logger.info("Юзер добавляется с помощью UsersController в БД");
        habitappDAO.addUser(user);
    }

    /**
     * Получает пользователя из базы данных по его email.
     *
     * @param email email пользователя
     * @return пользователь, найденный по email
     */
    public User getUserFromDatabase(String email) {
        logger.info("Юзер возвращается с помощью запроса UsersController к UsersStorage");
        return habitappDAO.getUser(email);
    }

    /**
     * Обновляет информацию о пользователе в базе данных.
     *
     * @param user  обновленный пользователь
     * @param email email пользователя
     */
    public void updateRedactedUser(String email, User user)  {
        habitappDAO.updateRedactedUser(email, user);
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param email email пользователя
     */
    public void removeUserFromDatabase(String email) {
        logger.info("UsersController делает запрос на удаление юзера к UsersStorage");
        habitappDAO.removeUser(email);
    }

    /**
     * Добавляет новую привычку пользователю в базе данных.
     *
     * @param user  пользователь
     * @param habit новая привычка
     */
    public void addNewHabit(User user, Habit habit) {
        logger.info("UsersController делает запрос на добавление привычки юзеру в UsersStorage");
        habitappDAO.addHabitToUser(user, habit);
    }

    /**
     * Изменяет существующую привычку пользователя в базе данных.
     *
     * @param user        пользователь
     * @param oldHabit    старая привычка
     * @param newHabit    Обновленная привычка
     */
    public void changeHabit(User user, Habit oldHabit, Habit newHabit) {
        habitappDAO.changeHabit(user, oldHabit, newHabit);
    }

    /**
     * Удаляет привычку пользователя из базы данных.
     *
     * @param user      пользователь
     * @param habit привычка которую нужно удалить
     */
    public void removeHabit(User user, Habit habit) {
        logger.info("Привычка у юзера удаляется");
        habitappDAO.removeHabitFromUser(user, habit);
    }

    /**
     * Блокирует пользователя в базе данных.
     *
     * @param email email пользователя
     */
    public void blockUser(String email) {
        logger.info("Пользователь блокируется");
        //usersDAO.getUser(email).setBlocked();
    }

    public Habit getHabitFromUser(User user, int habitNumber) {
        return habitappDAO.getHabitFromUser(user, habitNumber);
    }

    public List<Habit> getAllHabits(User user) {
        return habitappDAO.getAllHabits(user);
    }

    public boolean hasHabit(User user, Habit habit) {
        return habitappDAO.hasHabit(user, habit);
    }

    public boolean hasUser(String email) {
        return habitappDAO.getUserIdFromDB(email) != 0;
    }

    public boolean habitAlreadyMarked(User user, Habit habit) {
        return habitappDAO.habitIsMarked(user, habit);
    }

    public void markHabit(User user, Habit habit) {
        habitappDAO.markHabit(user,habit);
    }

    public List<LocalDateTime> getComplitedDays(User user, Habit habit) {
        return habitappDAO.getAllComplitedDays(user,habit);
    }

}
