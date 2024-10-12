package validate;

public class HabitEditorValidator {

    public boolean isValidNumberForRedacting(String numberForRedacting) {
        return switch (numberForRedacting) {
            case "1", "2", "3" -> true;
            default -> false;
        };
    }

    public boolean isValidHabitName(String habitName) {
        return habitName.matches("^[a-zA-Z0-9а-яА-ЯёЁ]{5,30}$");
    }

    public boolean isValidHabitDescription(String habitDescription) {
        return habitDescription.matches("^[a-zA-Z0-9а-яА-ЯёЁ,. ]{5,100}$");
    }

    public boolean isValidHabitFrequencyNumber(String habitFrequencyNumber) {
        return switch (habitFrequencyNumber) {
            case "1", "2" -> true;
            default -> false;
        };
    }
}
