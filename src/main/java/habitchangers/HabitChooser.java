package habitchangers;

import entities.Habit;
import entities.User;
import in.Reader;
import out.HabitChooserWriter;
import validate.HabitChooserValidator;
import wait.Waiter;

public class HabitChooser {

    private final HabitIndicator habitIndicator = new HabitIndicator();
    private final HabitChooserWriter writer = new HabitChooserWriter();
    private final Reader reader = new Reader();
    private final HabitChooserValidator validator = new HabitChooserValidator();
    private final Waiter waiter = new Waiter();

    public Habit chooseHabit(User user) {
        Habit choosedHabit = null;
        habitIndicator.showHabits(user);
        writer.askHabitNumber();
        String habitNumber = reader.read();

        if (validator.isValidHabitNumber(user, habitNumber)) {
            int habitIndex = Integer.parseInt(habitNumber) - 1;
            choosedHabit = user.getHabits().get(habitIndex);
        } else {
            writer.reportIncorrectHabitNumber();
            waiter.waitSecond();
            choosedHabit = chooseHabit(user);
        }
        return choosedHabit;
    }
}
