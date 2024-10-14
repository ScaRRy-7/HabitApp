package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс User представляет пользователя системы.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class User {
    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Email пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Список привычек пользователя.
     */
    private List<Habit> habits = new ArrayList<>();

    /**
     * Флаг, показывающий, заблокирован ли пользователь.
     */
    private boolean isBlocked = false;

    /**
     * Создает нового пользователя с заданными именем, email и паролем.
     *
     * @param name     имя пользователя
     * @param email    email пользователя
     * @param password пароль пользователя
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает email пользователя.
     *
     * @return email пользователя
     */
    public String getEmail() {
        return email;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает новое имя пользователя.
     *
     * @param name новое имя пользователя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает новый email пользователя.
     *
     * @param email новый email пользователя
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Устанавливает новый пароль пользователя.
     *
     * @param password новый пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Добавляет новую привычку пользователя.
     *
     * @param habit новая привычка пользователя
     */
    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    /**
     * Возвращает список привычек пользователя.
     *
     * @return список привычек пользователя
     */
    public List<Habit> getHabits() {
        return habits;
    }

    /**
     * Устанавливает флаг, что пользователь заблокирован.
     */
    public void setBlocked() {
        isBlocked = true;
    }

    /**
     * Возвращает флаг, показывающий, заблокирован ли пользователь.
     *
     * @return true, если пользователь заблокирован, иначе false
     */
    public boolean isBlocked() {
        return isBlocked;
    }
}
