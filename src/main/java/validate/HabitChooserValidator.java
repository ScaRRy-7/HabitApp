package validate;

import entities.User;
import storage.UsersController;

public class HabitChooserValidator {

    private final UsersController usersController = new UsersController();

    public boolean isValidHabitNumber(User user, String habitNumberStr) {
        return habitNumberStr.matches("^[0-9]{1,10000}$") &&
                Integer.parseInt(habitNumberStr) != 0 &&
                Integer.parseInt(habitNumberStr) <= usersController.getAllHabits(user).size();

    }
}
