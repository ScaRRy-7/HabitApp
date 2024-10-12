package storage;

import entities.Habit;
import entities.User;
import org.slf4j.*;
import java.util.*;

public final class UsersStorage {

    private static final UsersStorage usersStorage = new UsersStorage();

    public static UsersStorage getInstance() {
        return usersStorage;
    }
    private Map<String, User> users = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(UsersStorage.class);
    private UsersStorage() {

    }


    public void addUser(User user) {
        users.put(user.getEmail(), user);
        logger.info("Добавлен пользователь: {}", user.getName());
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public boolean hasUser(String email) {
        return users.get(email) != null;
    }

    public User getUser(String email) {
        logger.info("возвращен пользователь с почтой {}", email);
        return users.get(email);
    }

    public void removeUser(String email) {
        users.remove(email);
        logger.info("Пользователь с почтой {} удален", email);
    }

    public void addHabitToUser(User user, Habit habit) {
        user.addHabit(habit);
        logger.info("Пользователю {} добавлена привычка {}", user.getName(), habit.getName());
        users.put(user.getEmail(), user);
        logger.info("пользователь обновлен");
    }



}
