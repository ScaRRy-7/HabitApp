package validate;

public class EmailValidator {

    private static final EmailValidator emailValidator = new EmailValidator();

    private EmailValidator() {}

    public static EmailValidator getInstance() {
        return emailValidator;
    }

    public boolean isValid(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\\\.[^\\s@]+$");
    }
}
