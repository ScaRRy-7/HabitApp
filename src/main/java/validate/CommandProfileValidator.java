package validate;

public class CommandProfileValidator {

    public boolean isValid(String command) {
        return switch (command) {
            case "1", "2", "3", "4", "5", "6" -> true;
            default -> false;
        };
    }
}
