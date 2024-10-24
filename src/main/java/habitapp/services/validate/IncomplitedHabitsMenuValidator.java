package services.validate;

public class IncomplitedHabitsMenuValidator {

    public boolean isValidNumberOfIncHabit(String numberOfIncomplitedHabitStr, int numberOfIncHabits) {
        return numberOfIncomplitedHabitStr.matches("^[0-9]{1,10000}$") &&
                Integer.parseInt(numberOfIncomplitedHabitStr) != 0 &&
                Integer.parseInt(numberOfIncomplitedHabitStr) <= numberOfIncHabits;
    }

}
