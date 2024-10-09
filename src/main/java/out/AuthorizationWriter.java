package out;

import entities.User;

public class AuthorizationWriter {

    public void greetings(User user) {
        System.out.printf("Привет, %s! Выбери команду %n", user.getName());
    }

    public void getCommands() {
        System.out.println("Список доступных  команд, введи соотвествующую цифру для выбора команды:" +
                "\n\t1 - Изменить/удалить профиль" +
                "\n\t2 - Управлять привычками" +
                "\n\t3 - Статистика выполнения привычек" +
                "\n\t4 - выход из аккаунта");

    }

    public void reportInvalidCommand() {
        System.out.println("Некорректно указана команда, введи цифру только из списка предложенных: ");
    }
}
