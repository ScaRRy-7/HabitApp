package out;

import entities.Habit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DayStatisticsHabitWriter {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void writeTodayStatistics(Habit habit) {
        System.out.println(LocalDateTime.now().format(formatter) + "(сегодня) - привычка " + (habit.isComplited() ? "выполнена\n" : "не выполнена\n"));
    }
}
