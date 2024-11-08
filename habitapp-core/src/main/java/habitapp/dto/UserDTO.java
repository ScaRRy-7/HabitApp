package habitapp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для пользователя.
 * <p>
 * Этот класс используется для передачи данных о пользователе между слоями приложения.
 * Он содержит основные атрибуты, такие как имя, электронная почта и пароль.
 * </p>
 */
@Getter
@Setter
public class UserDTO {
    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;
}
