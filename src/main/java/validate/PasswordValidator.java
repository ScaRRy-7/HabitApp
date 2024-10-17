package validate;

public final class PasswordValidator {

    public boolean isValid(String password) {
        return password.matches("^(?=.*[a-z])[A-Za-z\\d]{1,16}$");
    }
}
