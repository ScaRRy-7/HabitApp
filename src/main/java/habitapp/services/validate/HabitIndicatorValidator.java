package services.validate;

public class HabitIndicatorValidator {

    public boolean isValidCommandString(String commandString) {
        return switch (commandString) {
            case "1", "2" -> true;
            default -> false;
        };
    }
}
