package habitapp.services.out;

public final class IdentificationWriter {

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

    public void infoUserBlocked() {
        System.out.println("Доступ этому пользователю запрещён!");
    }
}
