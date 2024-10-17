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

/**
 * Класс WeekStatisticsHabit отвечает за создание статистики по привычкам за неделю.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class WeekStatisticsHabit implements StatisticsCreator {
    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    /**
     * Объект класса WeekStatisticsHabitWriter для вывода статистики на экран.
     */
    private final WeekStatisticsHabitWriter writer = new WeekStatisticsHabitWriter();

    /**
     * Получает и записывает статистику по привычке пользователя за последнюю неделю.
     *
     * @param user  текущий пользователь
     * @param habit выбранная привычка
     */
    @Override
    public void getStatistics(User user, Habit habit) {
        habitUnmarker.checkHabits(user);
        LocalDate today = LocalDate.now();
        List<String> statisticsWeek = new ArrayList<>();

        // Определение границ отчетного периода (последние 7 дней)
        LocalDate startOfWeek = today.minusDays(6);
        LocalDate endOfWeek = today;

        // Заполнение списка статистики за неделю
        final int daysInWeek = 7;
        for (int day = 0; day <= daysInWeek-1; day++) {
            LocalDate date  = startOfWeek.plusDays(day); // дата которую проверяем
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            boolean isHabitCreated = habit.getCreatedDateTime().toLocalDate().isBefore(date) ||
                    habit.getCreatedDateTime().toLocalDate().isEqual(date);

            boolean isHabitCompleted = false;
            if (habit.getFrequency() == HabitFrequency.DAILY) {
                isHabitCompleted = habit.getDaysHabitComplited().stream()
                        .anyMatch(dateTime -> dateTime.toLocalDate().equals(date));
            } else if (habit.getFrequency() == HabitFrequency.WEEKLY) {
                LocalDate lastCompletedDate = habit.getDaysHabitComplited().get(habit.getDaysHabitComplited().size()-1).toLocalDate();
                isHabitCompleted = ChronoUnit.DAYS.between(lastCompletedDate, date) % 7 == 0;
            }

            String status = isHabitCreated ? (isHabitCompleted ? "выполнена" : "не выполнена") : "ещё не была создана";
            statisticsWeek.add(dateStr + " - " + status);
        }

        writer.writeWeekStatistics(statisticsWeek);
    }
}
