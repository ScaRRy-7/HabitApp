package out;

import storage.User;

public class AuthorizationWriter {

    private static final AuthorizationWriter authorizationWriter = new AuthorizationWriter();


    private AuthorizationWriter() {}

    public static AuthorizationWriter getInstance() {
        return authorizationWriter;
    }

    public void greetings(User user) {
        System.out.printf("Привет, %s! Выбери команду: %n", user.getName());
    }
}
