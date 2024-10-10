package validate;

public class StatisticsValidator {

    public boolean isValidCommand(String command) {
        return switch (command) {
            case "1", "2", "3", "4", "5" -> true;
            default -> false;
        };
    }
}
