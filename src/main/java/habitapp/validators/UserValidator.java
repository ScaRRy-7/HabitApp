package habitapp.validators;

import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Валидатор для проверки данных пользователя.
 * Предоставляет методы для валидации имени, пароля и электронной почты.
 */
public class UserValidator {

    private static final UserValidator userValidator = new UserValidator();

    /**
     * Получает экземпляр валидатора пользователя.
     *
     * @return Экземпляр UserValidator.
     */
    public static UserValidator getInstance() {
        return userValidator;
    }

    private UserValidator() {}

    /**
     * Проверяет, являются ли данные пользователя действительными.
     *
     * @param userDTO Объект, содержащий данные пользователя.
     * @return true, если данные пользователя действительны, иначе false.
     * @throws UserIllegalRequestException Исключение, если данные пользователя недействительны.
     */
    public boolean validateUserData(UserDTO userDTO) throws UserIllegalRequestException {
        if (!isValidEmail(userDTO.getEmail())) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Недействительный адрес электронной почты.");
        }
        if (!isValidPassword(userDTO.getPassword())) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Недействительный пароль.");
        }
        if (!isValidName(userDTO.getName())) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Недействительное имя.");
        }
        return true;
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