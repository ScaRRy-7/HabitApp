package out;

import entities.Habit;
import entities.User;

public class HabitsRedactorWriter {

    public void writeCommands() {
        System.out.println("Список доступных  команд, введи соотвествующую цифру для выбора команды:" +
                "\n\t1 - Создать привычку" +
                "\n\t2 - Редактировать привычку" +
                "\n\t3 - Удалить привычку" +
                "\n\t4 - Посмотреть мои привычки" +
                "\n\t5 - вернуться в меню профиля");
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
}
