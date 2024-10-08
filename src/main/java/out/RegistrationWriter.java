package out;

public final class RegistrationWriter {

    private static final RegistrationWriter registrationWriter = new RegistrationWriter();

    private RegistrationWriter() {}

    public static RegistrationWriter getInstance() {
        return registrationWriter;
    }


    public void askName() {
        System.out.print("Введи своё имя. оно не может быть пустым, содержать пробелы или цифры: ");
    }

    public void askPassword() {
        System.out.print("Придумай пароль: ");
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
