package validate;

public final class PasswordValidator {

    private static final PasswordValidator passwordValidator = new PasswordValidator();

    private PasswordValidator() {}

    public static PasswordValidator getInstance() {
        return passwordValidator;
    }

    public boolean isValid(String password) {
        return password.matches("^(?=.*[a-z])[A-Za-z]{1,16}$\n");
    }
}
