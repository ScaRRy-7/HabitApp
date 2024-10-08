package entrypoint;

public class Start {

    public static void main(String[] args) {
        /* программа начинается с попытки идентификации пользователя

        если почта не будет найдена - редирект в регистрацию
        если почта найдена - редирект в авторизацию

         */
        Identification identification = Identification.getInstance();
        identification.start();
    }
}
