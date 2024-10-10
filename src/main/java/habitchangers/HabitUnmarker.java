package habitchangers;

import entities.Habit;
import entities.User;
import storage.UsersController;

import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;

import static enums.HabitFrequency.DAILY;
import static enums.HabitFrequency.WEEKLY;

public class HabitUnmarker {

    private final UsersController usersController = new UsersController();

    public void checkHabits(User user) {
        for (Habit habit : user.getHabits()) {
            if (!habit.isComplited()) continue;

            long daysPassed = ChronoUnit.MINUTES.between(habit.getDaysHabitComplited().getLast(), LocalDateTime.now());
            if ( (daysPassed >= 1 && habit.getFrequenсy() == DAILY) || (daysPassed >= 7 && habit.getFrequenсy() == WEEKLY)) {
                habit.setUncomplited();
            }
        }
        usersController.updateRedactedUser(user, user.getEmail());
    }
}
