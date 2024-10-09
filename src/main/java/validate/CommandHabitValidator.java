package validate;

import entities.User;

public class CommandHabitValidator {

    public boolean isValidCommand(String command) {
        return switch (command) {
            case "1", "2", "3", "4", "5" -> true;
            default -> false;
        };
    }

    public boolean isValidHabitName(String habitName) {
        return habitName.matches("^[\\w\\s]{5,30}$");
    }

    public boolean isValidDescription(String habitDescription) {
        return habitDescription.matches("^[\\w\\s.,!?]{5,100}$");
    }

    public boolean isValidFrequency(String frequencyNumber) {
        return switch (frequencyNumber) {
            case "1", "2" -> true;
            default -> false;
        };
    }

    public boolean isValidHabitNumber(User user, String habitNumberStr) {
        return habitNumberStr.matches("^[0-9]{1,10000}$") && Integer.parseInt(habitNumberStr) != 0 && Integer.parseInt(habitNumberStr) <= user.getHabits().size();
    }
}
