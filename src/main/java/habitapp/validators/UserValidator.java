package habitapp.validators;

import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;

public class UserValidator {

    public static final UserValidator INSTANCE = new UserValidator();

    private UserValidator() {

    }

    private final HabitappRepository habitappRepository = new HabitappRepository();

    public boolean validateUserData(UserDTO userDTO) throws UserIllegalRequestException {
        return isValidEmail(userDTO.getEmail()) && isValidPassword(userDTO.getPassword()) && isValidName(userDTO.getName());
    }

    public boolean isValidName(String name) {
        return name.matches("^[a-zA-Zа-яА-Яё]+$");
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])[A-Za-z\\d]{1,16}$");
    }

    public boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public boolean userExists(UserDTO userDTO) throws UserIllegalRequestException {
        return userDTO != null && habitappRepository.hasUser(userDTO.getEmail());
    }

}
