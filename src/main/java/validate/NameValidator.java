package validate;

public final class NameValidator {

    private static final NameValidator nameValidator = new NameValidator();

    private NameValidator() {}

    public static NameValidator getInstance() {
        return nameValidator;
    }

    public boolean isValid(String name) {
        return name.matches("^[a-zA-Zа-яА-Я]+$\n");
    }
}
