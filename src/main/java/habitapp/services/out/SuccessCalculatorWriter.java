package services.out;

import services.entities.Habit;

public class SuccessCalculatorWriter {

    public void write(Habit habit, double successRate) {
        System.out.println("Привычка: " + habit.getName() + ", Процент успешного выполнения: " + successRate + "%");
    }
}
