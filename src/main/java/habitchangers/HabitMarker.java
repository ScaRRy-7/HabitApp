package habitchangers;

import entities.Habit;
import entities.User;
import storage.UsersController;

import java.time.LocalDateTime;
import java.util.List;

public class HabitMarker {

    private final UsersController usersController = new UsersController();

    public void markHabit(User user, int numberOfIncomplitedHabit, List<Habit> incomplitedHabits) {
        int numberOfIncomplitedHabitIndex = numberOfIncomplitedHabit - 1;
        Habit incomplitedHabit = incomplitedHabits.get(numberOfIncomplitedHabitIndex);
        incomplitedHabit.setComplited();
        incomplitedHabit.getDaysHabitComplited().add(LocalDateTime.now());
    }
}
