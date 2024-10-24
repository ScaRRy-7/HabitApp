package habitapp.services.validate;

import habitapp.repositories.HabitappRepository;

public class CommandHabitValidator {

    private final HabitappRepository habitappRepository = new HabitappRepository();

    public boolean isValidCommand(String command) {
        return switch (command) {
            case "1", "2", "3", "4", "5", "6" -> true;
            default -> false;
        };
    }

    public boolean isValidHabitName(String habitName) {
        return habitName.matches("^[a-zA-Z0-9а-яА-ЯёЁ()@%*!?., ]{5,30}$");
    }

    public boolean isValidDescription(String habitDescription) {
        return habitDescription.matches("^[a-zA-Z0-9а-яА-ЯёЁ()@%*!?., ]{5,100}$");
    }

    public boolean isValidFrequency(String frequencyNumber) {
        return switch (frequencyNumber) {
            case "1", "2" -> true;
            default -> false;
        };
    }

//    public boolean isValidHabitNumber(User user, String habitNumberStr) {
//        return habitNumberStr.matches("^[0-9]{1,10000}$") && Integer.parseInt(habitNumberStr) != 0 && Integer.parseInt(habitNumberStr) <= usersRepository.getAllHabits(user).size();
//    }
}
