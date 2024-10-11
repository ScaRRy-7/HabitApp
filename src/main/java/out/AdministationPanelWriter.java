package out;

public class AdministationPanelWriter {

    public void askNickname() {
        System.out.println("Введи ник: ");
    }

    public void askPassword() {
        System.out.println("Введи пароль: ");
    }

    public void reportHackTry() {
        System.out.println("Пресечена попытка взлома!");
    }

    public void writeAdminCommands() {
        System.out.println("Выбери команду:" +
                "\n\t1 - заблокировать пользователя" +
                "\n\t2 - удалить пользователя" +
                "\n\t3 - выйти из аккаунта администратора");
    }

    public void reportInvalidCommand() {
        System.out.println("Некорректная команда! напиши соответствующий номер команды:");
    }
}
