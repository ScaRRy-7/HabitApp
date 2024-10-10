package statistics;

import entities.Habit;
import entities.User;
import habitchangers.HabitUnmarker;
import out.DayStatisticsHabitWriter;

public class DayStatisticsHabit implements StatisticsCreator  {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final DayStatisticsHabitWriter writer = new DayStatisticsHabitWriter();

    @Override
    public void getStatistics(User user, Habit habit) {
        habitUnmarker.checkHabits(user);
        writer.writeTodayStatistics(habit);

    }
}
