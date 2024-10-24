package habitapp.services.statistics;

import habitapp.services.habitchangers.HabitUnmarker;
import habitapp.services.out.ProgressReportWriter;
import org.slf4j.*;
import habitapp.repositories.HabitappRepository;

/**
 * Класс ProgressReport отвечает за генерацию отчета о прогрессе выполнения привычек пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class ProgressReport {

    private final HabitappRepository habitappRepository = new HabitappRepository();

    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    /**
     * Объект класса StreakCalculator для расчета серии выполненных привычек.
     */
    private final StreakCalculator streakCalculator = new StreakCalculator();

    /**
     * Объект класса SuccessCalculator для расчета процента успешного выполнения привычек.
     */
    private final SuccessCalculator successCalculator = new SuccessCalculator();

    /**
     * Объект класса ProgressReportWriter для вывода отчета на экран.
     */
    private final ProgressReportWriter writer = new ProgressReportWriter();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(ProgressReport.class);

    /**
     * Генерирует отчет о прогрессе выполнения привычек для текущего пользователя.
     *
     * @param user текущий пользователь
//     */
//    public void generateReport(User user) {
//        logger.debug("Генерируется прогресс по привычкам");
//        habitUnmarker.checkHabits(user);
//        System.out.println("Отчет о прогрессе выполнения привычек для пользователя: " + user.getName());
////
////        for (Habit habit : usersRepository.getAllHabits(user)) {
////            int streak = streakCalculator.calculateStreak(habit);
////            double successRate = successCalculator.calculateSuccessRate(habit);
////            writer.write(habit, streak, successRate);
////        }
////    }
}
