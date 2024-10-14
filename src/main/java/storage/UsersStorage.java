package storage;

import entities.Habit;
import entities.User;
import org.slf4j.*;
import java.util.*;

/**
 * Класс UsersStorage отвечает за хранение и управление данными пользователей.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class UsersStorage {
    /**
     * Статический экземпляр класса UsersStorage, реализующий паттерн Singleton.
     */
    private static final UsersStorage usersStorage = new UsersStorage();

    /**
     * Возвращает статический экземпляр класса UsersStorage.
     *
     * @return статический экземпляр класса UsersStorage
     */
    public static UsersStorage getInstance() {
        return usersStorage;
    }

    /**
     * Карта пользователей, где ключом является email пользователя.
     */
    private Map<String, User> users = new HashMap<>();

    /**
     * Объект класса Logger для логирования событий.
     */
    private Logger logger = LoggerFactory.getLogger(UsersStorage.class);

    /**
     * Приватный конструктор для реализации паттерна Singleton.
     */
    private UsersStorage() {

    }

    /**
     * Добавляет пользователя в базу данных.
     *
     * @param user новый пользователь
     */
    public void addUser(User user) {
        users.put(user.getEmail(), user);
        logger.info("Добавлен пользователь: {}", user.getName());
    }

    /**
     * Возвращает коллекцию всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    public Collection<User> getUsers() {
        return users.values();
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя
     * @return true, если пользователь существует, иначе false
     */
    public boolean hasUser(String email) {
        return users.get(email) != null;
    }

    /**
     * Возвращает пользователя по его email.
     *
     * @param email email пользователя
     * @return пользователь, найденный по email
     */
    public User getUser(String email) {
        logger.info("возвращен пользователь с почтой {}", email);
        return users.get(email);
    }

    /**
     * Удаляет пользователя из базы данных по его email.
     *
     * @param email email пользователя
     */
    public void removeUser(String email) {
        users.remove(email);
        logger.info("Пользователь с почтой {} удален", email);
    }

    /**
     * Добавляет новую привычку пользователю в базу данных.
     *
     * @param user  пользователь
     * @param habit новая привычка
     */
    public void addHabitToUser(User user, Habit habit) {
        user.addHabit(habit);
        logger.info("Пользователю {} добавлена привычка {}", user.getName(), habit.getName());
        users.put(user.getEmail(), user);
        logger.info("пользователь обновлен");
    }
}
