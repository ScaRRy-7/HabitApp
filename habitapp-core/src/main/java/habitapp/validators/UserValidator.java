package habitapp.validators;

import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import org.springframework.stereotype.Component;

/**
 * Валидатор для проверки данных пользователя.
 * Предоставляет методы для валидации имени, пароля и электронной почты.
 */
@Component
public class UserValidator {

    public UserValidator() {}

    public boolean validUserData(UserDTO userDTO) throws UserIllegalRequestException {
        return isValidEmail(userDTO.getEmail()) && isValidPassword(userDTO.getPassword()) &&
                isValidName(userDTO.getName());
    }

    /**
     * Проверяет, является ли имя действительным.
     *
     * @param name Имя для проверки.
     * @return true, если имя действительно, иначе false.
     */
    public boolean isValidName(String name) {
        return name.matches("^[a-zA-Zа-яА-Яё]+$");
    }

    /**
     * Проверяет, является ли пароль действительным.
     *
     * @param password Пароль для проверки.
     * @return true, если пароль действителен, иначе false.
     */
    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])[A-Za-z\\d]{1,16}$");
    }

    /**
     * Проверяет, является ли адрес электронной почты действительным.
     *
     * @param email Адрес электронной почты для проверки.
     * @return true, если адрес электронной почты действителен, иначе false.
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }


}