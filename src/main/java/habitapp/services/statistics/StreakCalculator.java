package services.statistics;

import services.entities.Habit;
import services.entities.User;
import services.enums.HabitFrequency;
import services.habitchangers.HabitUnmarker;
import services.out.StreakCalculatorWriter;
import repositories.UsersRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Класс StreakCalculator отвечает за расчет серии выполнений привычек.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class StreakCalculator {

    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private final HabitUnmarker habitUnmarker;

    /**
     * Объект класса StreakCalculatorWriter для вывода результатов расчета на экран.
     */
    private final StreakCalculatorWriter writer;

    /**
     * Конструктор по умолчанию инициализирует объекты HabitUnmarker и StreakCalculatorWriter.
     */
    public StreakCalculator() {
        this.habitUnmarker = new HabitUnmarker();
        this.writer = new StreakCalculatorWriter();
    }

    /**
     * Конструктор с параметрами, позволяющий установить объекты HabitUnmarker и StreakCalculatorWriter.
     *
     * @param habitUnmarker объект класса HabitUnmarker
     * @param writer        объект класса StreakCalculatorWriter
     */
    public StreakCalculator(HabitUnmarker habitUnmarker, StreakCalculatorWriter writer) {
        this.habitUnmarker = habitUnmarker;
        this.writer = writer;
    }

    /**
     * Запускает расчет серии выполнений привычек для всех привычек пользователя.
     *
     * @param user текущий пользователь
     */
    public void start(User user) {
        habitUnmarker.checkHabits(user);
        for (Habit habit : usersRepository.getAllHabits(user)) {
            int streak = calculateStreak(habit);
            writer.write(habit.getName(), streak);
        }
    }

    /**
     * Рассчитывает серию выполнений для указанной привычки.
     *
     * @param habit привычка, для которой нужно рассчитать серию
     * @return длина серии выполнений
     */
    public int calculateStreak(Habit habit) {
        List<LocalDateTime> completedDates = habit.getDaysHabitComplited();

        if (completedDates.isEmpty()) {
            return 0;
        }

        int streak = 1;
        LocalDate lastCompletedDate = completedDates.get(completedDates.size() - 1).toLocalDate();
        LocalDate today = LocalDate.now();

        if (habit.getFrequency() == HabitFrequency.DAILY) {
            while (today.minusDays(streak).isEqual(lastCompletedDate) || today.minusDays(streak).isAfter(lastCompletedDate)) {
                streak++;
            }
        } else { // Еженедельная привычка
            long daysBetween = ChronoUnit.DAYS.between(lastCompletedDate, today);
            if (daysBetween >= 7) {
                streak = (int) (daysBetween / 7) + 1;
            }
        }

        return streak;
    }
}
