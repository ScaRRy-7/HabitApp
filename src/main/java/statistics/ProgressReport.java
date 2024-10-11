package statistics;

import entities.Habit;
import enums.HabitFrequency;
import entities.User;
import habitchangers.HabitUnmarker;
import out.ProgressReportWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProgressReport {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final StreakCalculator streakCalculator = new StreakCalculator();
    private final SuccessCalculator successCalculator = new SuccessCalculator();
    private final ProgressReportWriter writer = new ProgressReportWriter();

    public void generateReport(User user) {
        habitUnmarker.checkHabits(user);
        System.out.println("Отчет о прогрессе выполнения привычек для пользователя: " + user.getName());

        for (Habit habit : user.getHabits()) {
            int streak = streakCalculator.calculateStreak(habit);
            double successRate = successCalculator.calculateSuccessRate(habit);
            writer.write(habit, streak, successRate);
        }
    }
}