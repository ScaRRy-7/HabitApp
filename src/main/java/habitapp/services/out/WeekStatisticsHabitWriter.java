package habitapp.services.out;

import java.util.List;

public class WeekStatisticsHabitWriter {

    public void writeWeekStatistics(List<String> weekStatistics) {
        System.out.println("Статистика выполнения привычки за последние 7 дней:");
        for (String entry : weekStatistics) {
            System.out.println(entry);
        }
        System.out.println();
    }
}
