package out;

import entities.Habit;
import enums.HabitFrequency;


public class ProgressReportWriter {

    public void write(Habit habit, int streak, double successRate) {
        System.out.println("Привычка: " + habit.getName());
        System.out.println("   Частота: " + (habit.getFrequency() == HabitFrequency.DAILY ? "Ежедневная" : "Еженедельная"));
        System.out.println("   Стрик: " + streak);
        System.out.println("   Процент успешного выполнения: " + successRate + "%");
    }
}
