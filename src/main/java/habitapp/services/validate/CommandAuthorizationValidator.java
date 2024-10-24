package services.validate;

public class CommandAuthorizationValidator {

    public boolean isValid(String command) {
        if (command.isEmpty()) return false;

        return switch (command) {
            case "1", "2", "3", "4" -> true;
            default -> false;
        };
    }
}
