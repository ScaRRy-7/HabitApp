package habitchangers;

import entities.Habit;
import entities.User;
import enums.Sorting;
import in.Reader;
import out.HabitsRedactorWriter;
import validate.HabitIndicatorValidator;
import wait.Waiter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HabitIndicator {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final Waiter waiter = new Waiter();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Reader reader = new Reader();
    private final HabitIndicatorValidator validator = new HabitIndicatorValidator();

    public void showHabits(User currentUser) {
        habitUnmarker.checkHabits(currentUser); // ПРЕДВАРИТЕЛЬНО РАЗМАРКИРОВКА ПРИВЫЧЕК ЕСЛИ ПРОШЕЛ СРОК (ДЕНЬ ИЛИ МЕСЯЦ)
        if (currentUser.getHabits().isEmpty()) {
            writer.infoNoHabits();
        } else {
            writer.writeHabits(currentUser);
        }
        waiter.waitSecond();
    }

    public void showHabits(List<Habit> habits, Sorting sorting) {
        List<Habit> copiedHabits = new ArrayList<>(habits);

        if (sorting == Sorting.DATE) {
            copiedHabits.sort((habit1, habit2) -> habit1.getCreatedDateTime().compareTo(habit2.getCreatedDateTime()));
        } else {
            copiedHabits.sort((habit1, habit2) -> habit2.getFrequency().compareTo(habit1.getFrequency()));
        }

        writer.writeHabits(copiedHabits);

    }

    public void chooseSorting(User currentUser) {
        habitUnmarker.checkHabits(currentUser);

        if (currentUser.getHabits().isEmpty()) {
            writer.infoNoHabits();
            waiter.waitSecond();
            return;
        }

        writer.askSorting();
        String commandString = reader.read();

        if (validator.isValidCommandString(commandString)) {
            Sorting sorting = getSortingByNum(Integer.parseInt(commandString));
            switch (sorting) {
                case DATE:
                    showHabits(currentUser.getHabits(), Sorting.DATE);
                    break;
                case FREQUENCY:
                    showHabits(currentUser.getHabits(), Sorting.FREQUENCY);
                    break;
            }
        } else {
            writer.reportInvalidSorting();
            waiter.waitSecond();
            chooseSorting(currentUser);
        }
    }

    private Sorting getSortingByNum(int num) {
        return switch (num) {
            case 1 -> Sorting.DATE;
            default -> Sorting.FREQUENCY;
        };
    }
}
