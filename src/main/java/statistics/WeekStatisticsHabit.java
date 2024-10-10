package statistics;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import out.WeekStatisticsHabitWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;


public class WeekStatisticsHabit implements StatisticsCreator {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final WeekStatisticsHabitWriter writer = new WeekStatisticsHabitWriter();

    @Override
    public void getStatistics(User user, Habit habit) {
        habitUnmarker.checkHabits(user);
        LocalDate today = LocalDate.now();
        List<String> statisticsWeek = new ArrayList<>();

        // Определение границ отчетного периода (последние 7 дней)
        LocalDate startOfWeek = today.minusDays(6);
        LocalDate endOfWeek = today;

        // Заполнение списка статистики за неделю
        for (int day = 0; day <= 6; day++) {
            LocalDate date  = startOfWeek.plusDays(day); // дата которую проверяем
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            boolean isHabitCreated = habit.getCreatedDateTime().toLocalDate().isBefore(date) ||
                    habit.getCreatedDateTime().toLocalDate().isEqual(date);

            boolean isHabitCompleted = false;
            if (habit.getFrequency() == HabitFrequency.DAILY) {
                isHabitCompleted = habit.getDaysHabitComplited().stream()
                        .anyMatch(dateTime -> dateTime.toLocalDate().equals(date));
            } else if (habit.getFrequency() == HabitFrequency.WEEKLY) {
                LocalDate lastCompletedDate = habit.getDaysHabitComplited().getLast().toLocalDate();
                isHabitCompleted = ChronoUnit.DAYS.between(lastCompletedDate, date) % 7 == 0;
            }

            String status = isHabitCreated ? (isHabitCompleted ? "выполнена" : "не выполнена") : "ещё не была создана";
            statisticsWeek.add(dateStr + " - " + status);
        }

        writer.writeWeekStatistics(statisticsWeek);

    }
}
