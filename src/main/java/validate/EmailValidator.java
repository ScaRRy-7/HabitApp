package validate;

public final class EmailValidator {

    public boolean isValid(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
}
