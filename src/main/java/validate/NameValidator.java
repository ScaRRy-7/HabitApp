package validate;

public final class NameValidator {

    public boolean isValid(String name) {
        return name.matches("^[a-zA-Zа-яА-Яё]+$");
    }
}
