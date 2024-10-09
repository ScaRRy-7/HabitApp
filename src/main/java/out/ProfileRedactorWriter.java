package out;

public class ProfileRedactorWriter {

    public void writeCommands() {
        System.out.println("Список доступных  команд, введи соотвествующую цифру для выбора команды:" +
                "\n\t1 - Изменить имя" +
                "\n\t2 - Изменить почту" +
                "\n\t3 - Изменить пароль" +
                "\n\t4 - Удалить аккаунт" +
                "\n\t5 - Вернуться в меню" +
                "\n\t6 - Выйти из аккаунта");
    }

    public void reportInvalidCommand() {
        System.out.println("Некорректно указана команда, введи цифру только из списка предложенных:");
    }

    public void askName() {
        System.out.println("Введи своё новое имя: ");
    }

    public void reportInvalidName() {
        System.out.println("Ошибка! В указанном тобой имени содержатся cимволы не из кириллицы/латинского алфавита." +
                " Попробуй еще раз ");
    }

    public void askEmail() {
        System.out.println("Введи новую почту: ");
    }

    public void reportInvalidEmail() {
        System.out.println("Ты ввел некорректную почту! Попробуй ещё раз");
    }

    public void askPassword() {
        System.out.println("Введи новый пароль: ");
    }

    public void reportInvalidPassword() {
        System.out.println("Ошибка! Пароль должен содержать хотя бы одну строчную букву и " +
                "длиной от 1 до 16 символов (только латинский алфавит)");
    }

    public void infoAccDeleted() {
        System.out.println("Аккаунт был успешно удален!");
    }

    public void infoNameChanged() {
        System.out.println("Имя было успешно изменено!");
    }

    public void infoEmailChanged() {
        System.out.println("Почта была успешно изменена!");
    }

    public void infoPasswordChanged() {
        System.out.println("Пароль был успешно изменён!");
    }
}
