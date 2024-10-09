package habitchangers;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import in.Reader;
import out.HabitsRedactorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import wait.Waiter;

public class HabitCreator {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final Waiter waiter = new Waiter();
    private final UsersController usersController = new UsersController();

    public void createNewHabit(User user) {
        writer.askHabitName();
        String habitName = getHabitName();

        writer.askHabitDescription();
        String habitDescription = getHabitDescription();

        writer.askHabitFrequency();
        HabitFrequency frequency = getHabitFrequency();

        Habit newHabit = new Habit(habitName, habitDescription, frequency);
        writer.infoHabitWasCreated();
        waiter.waitSecond();

        usersController.addNewHabit(user, newHabit);

    }

    private String getHabitName() {
        String habitName = reader.read();
        if (habitValidator.isValidHabitName(habitName)) {
            return habitName;
        } else {
            writer.reportInvalidHabitName();
            waiter.waitSecond();
            getHabitName();
        }
        return habitName;
    }

    private String getHabitDescription() {
        String habitDescription = reader.read();
        if (habitValidator.isValidDescription(habitDescription)) {
            return habitDescription;
        } else {
            writer.reportInvalidHabitName();
            getHabitDescription();
        }
        return habitDescription;
    }

    private HabitFrequency getHabitFrequency() {
        HabitFrequency habitFrequency = null;
        String habitFrequencyString = reader.read();
        if (habitValidator.isValidFrequency(habitFrequencyString)) {
            habitFrequency = getHabitFrequencyByNumber(Integer.parseInt(habitFrequencyString));
            return habitFrequency;
        } else {
            writer.reportInvalidFrequency();
            habitFrequency = getHabitFrequency();
        }
        return habitFrequency;
    }

    private HabitFrequency getHabitFrequencyByNumber(int number) {
        return switch (number) {
            case 1 -> HabitFrequency.DAILY;
            case 2 -> HabitFrequency.WEEKLY;
            default -> throw new IllegalArgumentException("Invalid command number");
        };
    }
}
