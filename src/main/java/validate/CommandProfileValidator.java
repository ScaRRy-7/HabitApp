package validate;

public class CommandProfileValidator {

    public boolean isValid(String command) {
        if (command.isEmpty()) return false;

        return switch (command) {
            case "1", "2", "3", "4", "5" -> true;
            default -> false;
        };
    }
}
