package habitapp.services.out;

public class HabitChooserWriter {

    public void askHabitNumber() {
        System.out.println("Введи номер привычки, по которой хочешь получить статистику:");
    }

    public void reportIncorrectHabitNumber() {
        System.out.println("Некорректно указан номер привычки!");
    }
}
