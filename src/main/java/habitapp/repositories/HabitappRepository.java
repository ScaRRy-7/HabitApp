package habitapp.repositories;

import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
import habitapp.entities.Habit;
import habitapp.exceptions.UserIllegalRequestException;
import org.slf4j.*;

import java.util.List;

/**
 * Класс UsersController отвечает за управление пользователями в базе данных.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class UsersRepository {
    /**
     * Объект класса UsersStorage для доступа к базе данных пользователей.
     */
    private final UsersDAO usersDAO = UsersDAO.getInstance();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(UsersRepository.class);

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param userDTO новый пользователь
     */
    public void addUserToDatabase(UserDTO userDTO) throws UserIllegalRequestException {
        logger.info("Юзер добавляется с помощью UsersController в БД");
        usersDAO.addUser(userDTO);
    }

    /**
     * Получает пользователя из базы данных по его email.
     *
     * @param email email пользователя
     * @return пользователь, найденный по email
     */
    public UserDTO getUserFromDatabase(String email) {
        logger.info("Юзер возвращается с помощью запроса UsersController к UsersStorage");
        return usersDAO.getUser(email);
    }

    /**
     * Обновляет информацию о пользователе в базе данных.
     *
     * @param userDTO  обновленный пользователь
     * @param email email пользователя
     */
    public void updateRedactedUser(String email, UserDTO userDTO)  {
        usersDAO.updateRedactedUser(email, userDTO);
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param email email пользователя
     */
    public void removeUserFromDatabase(String email) {
        logger.info("UsersController делает запрос на удаление юзера к UsersStorage");
        usersDAO.removeUser(email);
    }

    /**
     * Добавляет новую привычку пользователю в базе данных.
     *
     * @param user  пользователь
     * @param habit новая привычка
     */
    public void addNewHabit(UserDTO user, Habit habit) {
        logger.info("UsersController делает запрос на добавление привычки юзеру в UsersStorage");
        usersDAO.addHabitToUser(user, habit);
    }

    /**
     * Изменяет существующую привычку пользователя в базе данных.
     *
     * @param user        пользователь
     * @param oldHabit    старая привычка
     * @param newHabit    Обновленная привычка
     */
    public void changeHabit(UserDTO user, HabitDTO oldHabit, HabitDTO newHabit) {
        usersDAO.changeHabit(user, oldHabit, newHabit);
    }

    /**
     * Удаляет привычку пользователя из базы данных.
     *
     * @param userDTO       пользователь
     * @param habitDTO привычка которую нужно удалить
     */
    public void removeHabit(UserDTO userDTO, HabitDTO habitDTO) {
        logger.info("Привычка у юзера удаляется");
        usersDAO.removeHabitFromUser(userDTO, habitDTO);
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

    public Habit getHabitFromUser(UserDTO user, int habitNumber) {
        return usersDAO.getHabitFromUser(user, habitNumber);
    }

    public List<Habit> getAllHabits(UserDTO userDTO) {
        return usersDAO.getAllHabits(userDTO);
    }

    public boolean hasHabit(UserDTO userDTO, HabitDTO habitDTO) {
        return usersDAO.hasHabit(userDTO, habitDTO);
    }

    public boolean hasUser(String email) {
        return usersDAO.getUserIdFromDB(email) != 0;
    }

}
