package habitchangers;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import enums.PartOfHabit;
import in.Reader;
import out.HabitEditorWriter;
import out.HabitsRedactorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import validate.HabitEditorValidator;
import wait.Waiter;

public class HabitEditor {

    private final Reader reader = new Reader();
    private final HabitEditorWriter writer = new HabitEditorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final HabitEditorValidator validator = new HabitEditorValidator();
    private final UsersController usersController = new UsersController();
    private final Waiter waiter = new Waiter();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    public void redactHabit(User currentUser) {
        if (currentUser.getHabits().isEmpty()) {
            writer.infoNoHabits();
            waiter.waitSecond();
        } else {
            habitUnmarker.checkHabits(currentUser); // ПРЕДВАРИТЕЛЬНО РАЗМАРКИРОВКА ПРИВЫЧЕК ЕСЛИ ПРОШЕЛ СРОК (ДЕНЬ ИЛИ МЕСЯЦ)
            writer.askNumberOfHabitRedact(currentUser);
            String habitNumberStr = reader.read();
            if (habitValidator.isValidHabitNumber(currentUser, habitNumberStr)) {
                int habitNumber = Integer.parseInt(habitNumberStr);
                startRedact(currentUser, habitNumber);
                writer.infoHabitRedacted();
                waiter.waitSecond();
            } else {
                writer.reportInvalidHabitNumberRedact();
                waiter.waitSecond();
                redactHabit(currentUser);
            }
        }
    }

    private void startRedact(User user, int habitNumber) {
        writer.askWhatChange();
        String numberStr = reader.read();
        if (validator.isValidNumberForRedacting(numberStr)) {
            PartOfHabit partOfHabit = getPartOfHabit(numberStr);
            switch (partOfHabit) {
                case NAME:
                    redactName(user, habitNumber);
                    break;
                case DESCRIPTION:
                    redactDescription(user, habitNumber);
                    break;
                case FREQUENCY:
                    redactFrequency(user, habitNumber);
                    break;
            }
        } else {
            writer.reportIncorrectNumberForEditing();
            waiter.waitSecond();
            startRedact(user, habitNumber);
        }
    }

    public void redactName(User user, int habitNumber) {
        writer.askNewHabitName();
        String habitName = reader.read();

        if (validator.isValidHabitName(habitName)) {
            Habit newHabit = user.getHabits().get(habitNumber-1);
            newHabit.setName(habitName);
            usersController.changeHabit(user, newHabit, habitNumber);
        } else {
            writer.reportIncorrectHabitName();
            waiter.waitSecond();
            redactName(user, habitNumber);
        }
    }

    public void redactDescription(User user, int habitNumber) {
        writer.askNewHabitDescription();
        String habitDescription = reader.read();

        if (validator.isValidHabitDescription(habitDescription)) {
            Habit newHabit = user.getHabits().get(habitNumber-1);
            newHabit.setDescription(habitDescription);
            usersController.changeHabit(user, newHabit, habitNumber);
        } else {
            writer.reportIncorrectHabitDescription();
            waiter.waitSecond();
            redactDescription(user, habitNumber);
        }
    }

    public void redactFrequency(User user, int habitNumber) {
        writer.askNewHabitFrequency();
        String habitFrequencyNumber = reader.read();

        if (validator.isValidHabitFrequencyNumber(habitFrequencyNumber)) {
            HabitFrequency newHabitFrequency = getFrequencyByNumber(habitFrequencyNumber);
            Habit newHabit = user.getHabits().get(habitNumber-1);
            newHabit.setFrequenсy(newHabitFrequency);
            usersController.changeHabit(user, newHabit, habitNumber);
        } else {
            writer.reportIncorrectFrequencyNumber();
            waiter.waitSecond();
            redactFrequency(user, habitNumber);
        }
    }

    public PartOfHabit getPartOfHabit(String numberStr) {
        return switch (numberStr) {
            case "1" -> PartOfHabit.NAME;
            case "2" -> PartOfHabit.DESCRIPTION;
            case "3" -> PartOfHabit.FREQUENCY;
            default -> throw new IllegalArgumentException("Invalid number");
        };
    }

    public HabitFrequency getFrequencyByNumber(String numberStr) {
        return switch (numberStr) {
            case "1" -> HabitFrequency.DAILY;
            case "2" -> HabitFrequency.WEEKLY;
            default -> throw new IllegalArgumentException("Invalid number");
        };
    }
}
