package validate;

import entities.User;

public class HabitChooserValidator {

    public boolean isValidHabitNumber(User user, String habitNumberStr) {
        return habitNumberStr.matches("^[0-9]{1,10000}$") &&
                Integer.parseInt(habitNumberStr) != 0 &&
                Integer.parseInt(habitNumberStr) <= user.getHabits().size();

    }
}
