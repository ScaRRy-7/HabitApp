package storage;

import entities.Habit;
import entities.User;
import org.slf4j.*;

public final class UsersController {

    private final UsersStorage usersStorage = UsersStorage.getInstance();
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    public void addUserToDatabase(User user) {
        logger.info("Юзер добавляется с помощью UsersController в БД");
        usersStorage.addUser(user);
    }

    public User getUserFromDatabase(String email) {
        logger.info("Юзер возвращается с помощью запроса UsersController к UsersStorage");
        return usersStorage.getUser(email);
    }

    public void updateRedactedUser(User user, String email) {
        logger.info("Юзер обновляется");
        usersStorage.removeUser(email);
        usersStorage.addUser(user);
    }

    public void removeUserFromDatabase(String email)  {
        logger.info("UsersController делает запрос на удаление юзера к UsersStorage");
        usersStorage.removeUser(email);
    }

    public void addNewHabit(User user, Habit habit) {
        logger.info("UsersController делает запрос на добавление привычки юзеру в UsersStorage");
        usersStorage.addHabitToUser(user, habit);
    }

    public void changeHabit(User user, Habit habit, int habitNumber) {
        logger.info("Привычка у юзера обновляется");
        int habitIndex = habitNumber-1;
        user.getHabits().set(habitIndex, habit);
        usersStorage.addUser(user);
    }

    public void removeHabit(User user, int habitNumber) {
        logger.info("Привычка у юзера удаляется");
        user.getHabits().remove(habitNumber-1);
        usersStorage.addUser(user);
    }

    public void blockUser(String email) {
        logger.info("Пользователь блокируется");
        usersStorage.getUser(email).setBlocked();
    }
}
