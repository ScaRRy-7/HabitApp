package habitapp.services.validate;

public class AdminPanelValidator {

    public boolean isValidCommand(String command) {
        return switch (command) {
            case "1", "2", "3" ->  true;
            default -> false;
        };
    }
}
