package services.validate;

import services.entities.User;
import repositories.UsersRepository;

public class HabitChooserValidator {

    private final UsersRepository usersRepository = new UsersRepository();

    public boolean isValidHabitNumber(User user, String habitNumberStr) {
        return habitNumberStr.matches("^[0-9]{1,10000}$") &&
                Integer.parseInt(habitNumberStr) != 0 &&
                Integer.parseInt(habitNumberStr) <= usersRepository.getAllHabits(user).size();

    }
}
