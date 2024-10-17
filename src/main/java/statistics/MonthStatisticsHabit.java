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

/**
 * Класс MonthStatisticsHabit отвечает за создание статистики по привычкам за месяц.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class MonthStatisticsHabit implements StatisticsCreator {
    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private HabitUnmarker habitUnmarker;

    /**
     * Объект класса MonthStatisticsHabitWriter для вывода статистики на экран.
     */
    private final MonthStatisticsHabitWriter writer;

    /**
     * Конструктор по умолчанию инициализирует объекты HabitUnmarker и MonthStatisticsHabitWriter.
     */
    public MonthStatisticsHabit() {
        habitUnmarker = new HabitUnmarker();
        writer = new MonthStatisticsHabitWriter();
    }

    /**
     * Конструктор с параметрами, позволяющий установить объекты HabitUnmarker и MonthStatisticsHabitWriter.
     *
     * @param habitUnmarker объект класса HabitUnmarker
     * @param writer        объект класса MonthStatisticsHabitWriter
     */
    public MonthStatisticsHabit(HabitUnmarker habitUnmarker, MonthStatisticsHabitWriter writer) {
        this.habitUnmarker = habitUnmarker;
        this.writer = writer;
    }

    /**
     * Получает и записывает статистику по привычке пользователя за последний месяц.
     *
     * @param user  текущий пользователь
     * @param habit выбранная привычка
     */
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
                LocalDate lastCompletedDate = habit.getDaysHabitComplited().get(habit.getDaysHabitComplited().size() - 1).toLocalDate();
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
