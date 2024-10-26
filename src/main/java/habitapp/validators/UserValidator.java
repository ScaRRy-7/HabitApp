package habitapp.validators;

import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;

public class UserValidator {

    private static final UserValidator userValidator = new UserValidator();
    public static UserValidator getInstance() {
        return userValidator;
    }
    private UserValidator() {}


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



}
