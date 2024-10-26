package habitapp.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс User представляет пользователя системы.
 * <p>
 * Этот класс содержит информацию о пользователе, такую как имя, электронная почта и пароль.
 * Он также предоставляет методы для управления состоянием блокировки пользователя.
 * </p>
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class User {

    /**
     * Репозиторий для работы с данными о привычках пользователя.
     */

    /**
     * Имя пользователя.
     */
    @Setter
    @Getter
    private String name;

    /**
     * Электронная почта пользователя.
     */
    @Setter
    @Getter
    private String email;

    /**
     * Пароль пользователя.
     */
    @Setter
    @Getter
    private String password;

    /**
     * Флаг, указывающий, заблокирован ли пользователь.
     */
    private boolean isBlocked = false;

    /**
     * Конструктор для создания нового пользователя.
     *
     * @param name     имя пользователя
     * @param email    электронная почта пользователя
     * @param password пароль пользователя
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Устанавливает статус блокировки пользователя.
     * После вызова этого метода пользователь будет заблокирован.
     */
    public void setBlocked() {
        isBlocked = true;
    }

    /**
     * Проверяет, заблокирован ли пользователь.
     *
     * @return true, если пользователь заблокирован; false в противном случае
     */
    public boolean isBlocked() {
        return isBlocked;
    }
}