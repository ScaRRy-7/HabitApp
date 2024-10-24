package habitapp.services.validate;

import habitapp.repositories.HabitappRepository;

public class HabitChooserValidator {

    private final HabitappRepository habitappRepository = new HabitappRepository();

//    public boolean isValidHabitNumber(User user, String habitNumberStr) {
//        return habitNumberStr.matches("^[0-9]{1,10000}$") &&
//                Integer.parseInt(habitNumberStr) != 0 &&
//                Integer.parseInt(habitNumberStr) <= usersRepository.getAllHabits(user).size();
//
//    }
}
