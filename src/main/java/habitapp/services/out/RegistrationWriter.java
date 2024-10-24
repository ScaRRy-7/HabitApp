package services.out;

public final class RegistrationWriter {

    public void askName() {
        System.out.println("Введи своё имя. оно не может быть пустым, содержать пробелы или цифры: ");
    }

    public void askPassword() {
        System.out.println("Придумай пароль: ");
    }


    public void reportInvalidName() {
        System.out.println("Ошибка! В указанном тобой имени содержатся cимволы не из кириллицы/латинского алфавита." +
                " Попробуй еще раз");
    }

    public void reportInvalidPassword() {
        System.out.println("Ошибка! Пароль должен содержать хотя бы одну строчную букву, быть длиной от 1 до 16" +
                " символов и  содержать буквы  и (необязательно) цифры.");
    }
}
