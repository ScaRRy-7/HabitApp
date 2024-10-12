package habitchangers;

import entities.Habit;
import entities.User;
import in.Reader;
import out.HabitChooserWriter;
import validate.HabitChooserValidator;
import wait.Waiter;

public class HabitChooser {

    private final HabitIndicator habitIndicator;
    private final HabitChooserWriter writer;
    private final Reader reader;
    private final HabitChooserValidator validator;
    private final Waiter waiter;

    public HabitChooser(HabitIndicator habitIndicator, HabitChooserWriter writer, Reader reader,
                        HabitChooserValidator validator, Waiter waiter) {
        this.habitIndicator = habitIndicator;
        this.writer = writer;
        this.reader = reader;
        this.validator = validator;
        this.waiter = waiter;
    }

    public HabitChooser() {
        this.habitIndicator = new HabitIndicator();
        this.writer = new HabitChooserWriter();
        this.reader = new Reader();
        this.validator = new HabitChooserValidator();
        this.waiter = new Waiter();
    }

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