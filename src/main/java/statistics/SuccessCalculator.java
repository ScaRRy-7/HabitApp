package statistics;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import out.SuccessCalculatorWriter;
import org.slf4j.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Класс SuccessCalculator отвечает за расчет процента успешного выполнения привычек.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class SuccessCalculator {
    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    /**
     * Объект класса SuccessCalculatorWriter для вывода результатов расчета на экран.
     */
    private final SuccessCalculatorWriter writer = new SuccessCalculatorWriter();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Запускает расчет процента успешного выполнения привычек для всех привычек пользователя.
     *
     * @param user текущий пользователь
     */
    public void start(User user) {
        logger.debug("Запущен калькулятор выполняемости привычек в %");
        habitUnmarker.checkHabits(user);
        for (Habit habit : user.getHabits()) {
            double successRate = calculateSuccessRate(habit);
            writer.write(habit, successRate);
        }
    }

    /**
     * Рассчитывает процент успешного выполнения указанной привычки.
     *
     * @param habit привычка, для которой нужно рассчитать процент успешного выполнения
     * @return процент успешного выполнения привычки
     */
    public double calculateSuccessRate(Habit habit) {
        var completedDates = habit.getDaysHabitComplited();
        var createdDateTime = habit.getCreatedDateTime();
        var today = LocalDate.now();

        if (habit.getFrequency() == HabitFrequency.DAILY) {
            long totalDays = ChronoUnit.DAYS.between(createdDateTime.toLocalDate(), today) + 1; // Включая текущий день
            long completedDays = completedDates.size();
            return (completedDays * 100.0) / totalDays;
        } else { // Еженедельная привычка
            long totalWeeks = ChronoUnit.WEEKS.between(createdDateTime.toLocalDate(), today) + 1; // Включая текущую неделю
            long completedWeeks = 0;
            for (LocalDateTime date : completedDates) {
                if (ChronoUnit.WEEKS.between(createdDateTime.toLocalDate(), date.toLocalDate()) < totalWeeks) {
                    completedWeeks++;
                }
            }
            return (completedWeeks * 100.0) / totalWeeks;
        }
    }
}
