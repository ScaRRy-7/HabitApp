package statistics;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import out.StreakCalculatorWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class StreakCalculator {

    private final HabitUnmarker habitUnmarker;
    private final StreakCalculatorWriter writer;

    public StreakCalculator() {
        this.habitUnmarker = new HabitUnmarker();
        this.writer = new StreakCalculatorWriter();
    }

    public StreakCalculator(HabitUnmarker habitUnmarker, StreakCalculatorWriter writer) {
        this.habitUnmarker = habitUnmarker;
        this.writer = writer;
    }

    public void start(User user) {
        habitUnmarker.checkHabits(user);
        for (Habit habit : user.getHabits()) {
            int streak = calculateStreak(habit);
            writer.write(habit.getName(), streak);
        }
    }

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
