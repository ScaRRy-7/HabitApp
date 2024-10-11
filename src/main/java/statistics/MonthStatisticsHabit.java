package statistics;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import out.MonthStatisticsHabitWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MonthStatisticsHabit implements StatisticsCreator {

    private HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final MonthStatisticsHabitWriter writer = new MonthStatisticsHabitWriter();

    @Override
    public void getStatistics(User user, Habit habit) {
        habitUnmarker.checkHabits(user);
        LocalDate today = LocalDate.now();
        List<String> statisticsMonth = new ArrayList<>();

        // Определение границ отчетного периода (последние 30 дней)
        LocalDate startOfMonth = today.minusDays(29);
        LocalDate endOfMonth = today;

        // Заполнение списка статистики за месяц
        for (LocalDate date = startOfMonth; date.isBefore(endOfMonth.plusDays(1)); date = date.plusDays(1)) {
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            boolean isHabitCreated = habit.getCreatedDateTime().toLocalDate().isBefore(date) ||
                    habit.getCreatedDateTime().toLocalDate().isEqual(date);

            boolean isHabitCompleted = false;
            if (habit.getFrequency() == HabitFrequency.DAILY) {
                LocalDate finalDate = date;
                isHabitCompleted = habit.getDaysHabitComplited().stream()
                        .anyMatch(dateTime -> dateTime.toLocalDate().isEqual(finalDate));
            } else if (habit.getFrequency() == HabitFrequency.WEEKLY) { // Еженедельная привычка
                LocalDate lastCompletedDate = habit.getDaysHabitComplited().getLast().toLocalDate();
                isHabitCompleted = ChronoUnit.DAYS.between(lastCompletedDate, date) % 7 == 0;
            }

            String status = isHabitCreated
                    ? (isHabitCompleted ? "выполнена" : "не выполнена")
                    : "еще не была создана";
            statisticsMonth.add(dateStr + " - " + status);
        }

        writer.writeWeekStatistics(statisticsMonth);
    }
}
