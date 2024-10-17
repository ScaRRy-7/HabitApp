package out;

import entities.Habit;
import entities.User;
import habitchangers.HabitUnmarker;

import java.util.List;

public class HabitsRedactorWriter {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();


    public void writeCommands() {
        System.out.println("Список доступных  команд, введи соотвествующую цифру для выбора команды:" +
                "\n\t1 - Создать привычку" +
                "\n\t2 - Редактировать привычку" +
                "\n\t3 - Отметить привычку выполненной" +
                "\n\t4 - Удалить привычку" +
                "\n\t5 - Посмотреть мои привычки" +
                "\n\t6 - вернуться в меню профиля");
    }

    public void reportInvalidCommand() {
        System.out.println("Некорректно указана команда, введи цифру только из списка предложенных!");
    }

    public void askHabitName() {
        System.out.println("Напиши название привычки (от 5 до 30 символов):");
    }

    public void askHabitDescription() {
        System.out.println("Напиши описание привычки (от 5 до 100 символов)");
    }

    public void askHabitFrequency() {
        System.out.println("Выбери частоту привычки (введи соответствующую цифру):" +
                "\n\t1 - ежедневная" +
                "\n\t2 - еженедельная");
    }

    public void reportInvalidHabitName() {
        System.out.println("Некорректное название привычки! (5-30 символов, латиница/кириллица) Введи ещё раз:");
    }

    public void reportInvalidFrequency() {
        System.out.println("Некорректно указан номер частоты привычки! укажи 1 или 2: ");
    }


    public void infoHabitWasCreated() {
        System.out.println("Привычка создана!");
    }

    public void askNumberOfHabitRemove(User user) {
        habitUnmarker.checkHabits(user);
        writeHabits(user);
        System.out.println("Введи номер привычки, которую хочешь удалить:");
    }

    public void infoHabitRemoved() {
        System.out.println("Привычка была удалена!");
    }

    public void reportInvalidHabitNumberRemove() {
        System.out.println("Некорректно указан номер привычки для удаления!");
    }

    public void infoNoHabits() {
        System.out.println("Привычки отсутствуют!");
    }

    public void writeHabits(User user) {
        int num = 1;
        for (Habit habit : user.getHabits()) {
            System.out.println(num + " - " + habit + "\n");
            num++;
        }
    }

    public void writeHabits(List<Habit> habits) {
        int num = 1;
        for (Habit habit : habits) {
            System.out.println(num + " - " + habit + "\n");
            num++;
        }
    }

    public void reportInvalidSorting() {
        System.out.println("Указна некорректная сортировка! Введите соответствующую цифру: ");
    }

    public void askSorting() {
        System.out.println("Выбери тип сортировки твоих привычек:" +
                "\n\t1 - по дате" +
                "\n\t2 - по частоте");
    }
}
