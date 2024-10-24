package services.out;

import services.entities.Habit;
import services.entities.User;
import services.habitchangers.HabitUnmarker;
import repositories.UsersRepository;

public class HabitEditorWriter {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final UsersRepository usersRepository = new UsersRepository();

    public void askNewHabitName() {
        System.out.println("Напиши новое имя привычки (5-30 символов):");
    }

    public void askNewHabitDescription() {
        System.out.println("Напиши новое описание привычки (5-100 символов)");
    }

    public void reportIncorrectHabitName() {
        System.out.println("Некорректное имя привычки!");
    }

    public void reportIncorrectHabitDescription() {
        System.out.println("Некорректное описание привычки!");
    }

    public void askNewHabitFrequency() {
        System.out.println("Выбери новую частоту привычки (введи соответствующую цифру):" +
                "\n\t1 - ежедневная" +
                "\n\t2 - еженедельная");
    }

    public void reportIncorrectFrequencyNumber() {
        System.out.println("Некорректный номер частоты привычки!");
    }

    public void infoNoHabits() {
        System.out.println("Привычки отсутствуют!");
    }

    public void askNumberOfHabitRedact(User user) {
        habitUnmarker.checkHabits(user);
        writeHabits(user);
        System.out.println("Введи номер привычки, которую хочешь отредактировать:");

    }

    public void writeHabits(User user) {
        int num = 1;
        for (Habit habit : usersRepository.getAllHabits(user)) {
            System.out.println(num + " - " + habit + "\n");
            num++;
        }
    }

    public void infoHabitRedacted() {
        System.out.println("Привычка отредактирована!");
    }

    public void reportInvalidHabitNumberRedact() {
        System.out.println("Некорректно указан номер привычки для редактирования!");
    }

    public void askWhatChange() {
        System.out.println("Список доступных команд, введи соотвествующую цифру для выбора команды:" +
                "\n\t1 - редактировать название привычки" +
                "\n\t2 - редактировать описание привычки" +
                "\n\t3 - редактировать частоту привычки");
    }

    public void reportIncorrectNumberForEditing() {
        System.out.println("Некорректно указан пункт для редактировани!");
    }
}
