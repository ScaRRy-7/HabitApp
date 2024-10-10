package out;

import entities.Habit;

import java.util.List;

public class IncomplitedHabitsMenuWriter {

    public void infoNoIncomplitedHabits() {
        System.out.println("Нет невыполненных привычек на данный момент!");
    }

    public void writeIncomplitedHabits(List<Habit> incomplitedHabits) {
        int num = 1;
        for (Habit habit : incomplitedHabits) {
            System.out.println(num + " - " + habit + "\n");
            num++;
        }
    }

    public void askNumberForIncHabit() {
        System.out.println("Введи номер привычки, которую хочешь отметить выполненной: ");
    }

    public void reportInvalidNumberOfIncHabit() {
        System.out.println("Некорректно указан номер!");
    }

    public void infoHabitMarked() {
        System.out.println("Привычка была отмечена выполненой!");
    }
}
