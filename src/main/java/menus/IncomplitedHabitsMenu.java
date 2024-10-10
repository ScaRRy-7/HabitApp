package menus;

import entities.Habit;
import entities.User;
import habitchangers.HabitMarker;
import habitchangers.HabitUnmarker;
import in.Reader;
import out.IncomplitedHabitsMenuWriter;
import storage.UsersController;
import validate.IncomplitedHabitsMenuValidator;
import wait.Waiter;

import java.util.ArrayList;
import java.util.List;

public class IncomplitedHabitsMenu {

    private final UsersController usersController = new UsersController();
    private final IncomplitedHabitsMenuWriter writer = new IncomplitedHabitsMenuWriter();
    private final IncomplitedHabitsMenuValidator validator = new IncomplitedHabitsMenuValidator();
    private final HabitMarker habitMarker = new HabitMarker();
    private final Waiter waiter = new Waiter();
    private final Reader reader = new Reader();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    public void start(User user) {
        habitUnmarker.checkHabits(user);
        List<Habit> incomplitedHabits = new ArrayList<>();
        for (Habit habit : user.getHabits()) {
            if (!habit.isComplited()) {
                incomplitedHabits.add(habit);
            }
        }

        if (incomplitedHabits.isEmpty()) {
            writer.infoNoIncomplitedHabits();
            waiter.waitSecond();
            return;
        } else {
            showIncomplitedHabits(user, incomplitedHabits);
        }
    }

    private void showIncomplitedHabits(User user, List<Habit> incomplitedHabits) {
        writer.writeIncomplitedHabits(incomplitedHabits);
        writer.askNumberForIncHabit();
        String numberOfIncomplitedHabitStr = reader.read();

        if (validator.isValidNumberOfIncHabit(numberOfIncomplitedHabitStr, incomplitedHabits.size())) {
            habitMarker.markHabit(user, Integer.parseInt(numberOfIncomplitedHabitStr), incomplitedHabits);
            usersController.updateRedactedUser(user, user.getEmail());
            writer.infoHabitMarked();
            waiter.waitSecond();
        } else {
            writer.reportInvalidNumberOfIncHabit();
            waiter.waitSecond();
            showIncomplitedHabits(user, incomplitedHabits);
        }

    }
}
