package out;

import in.IdentificationReader;

public final class IdentificationWriter {

    private static final IdentificationWriter identificationWriter = new IdentificationWriter();

    private IdentificationWriter() {}

    public static IdentificationWriter getInstance() {
        return identificationWriter;
    }

    public void writeGreetings() {
        System.out.println("Введи свою почту, а я проверю, есть ли у тебя аккаунт: ");
    }

    public void reportInvalidEmail() {
        System.out.println("Ты ввел некорректную почту! Попробуй ещё раз");
    }

    public void infoRedirectRegistration() {
        System.out.println("Пользователь с такой почтой еще не существует, направляю тебя на регистрацию!");
    }

    public void infoRedirectAuthentication() {
        System.out.println("Пользователь с такой почтой существует!");
    }
}
