package statistics;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import out.SuccessCalculatorWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SuccessCalculator {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final SuccessCalculatorWriter writer = new SuccessCalculatorWriter();

    public void start(User user) {
        habitUnmarker.checkHabits(user);
        for (Habit habit : user.getHabits()) {
            double successRate = calculateSuccessRate(habit);
            writer.write(habit, successRate);
        }
    }

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
