package habitapp.repositories;

import habitapp.entities.Habit;

import java.util.List;

public interface HabitsRepository {
    public void createHabit(Habit habit, int userId);
    public void editHabit(Habit oldHabit, Habit newHabit);
    public List<Habit> getAllHabits(int userId);
    public boolean hasHabit(Habit habit, int userId);
    public void deleteHabit(Habit habit, int userId);
    public void editHabit(Habit oldHabit, Habit newHabit, int userId);
    public int getHabitId(Habit habit, int userId);
    public void markHabit(Habit habit, int userId);
    public boolean isMarked(int habitId);
    public void unmarkHabit(int habitId);
}
