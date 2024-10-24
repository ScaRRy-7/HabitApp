package habitapp.services.out;

public class AuthenticationWriter {

    public void askPassword() {
        System.out.println("Введи пароль для входа или exit чтобы войти в другой аккаунт: ");
    }

    public void reportInvalidPassword() {
        System.out.println("Ошибка! Пароль должен содержать хотя бы одну строчную букву и " +
                "длиной от 1 до 16 символов (только латинский алфавит)");
    }

    public void infoIncorrectPassword() {
        System.out.println("Пароль не верный!");
    }
}
