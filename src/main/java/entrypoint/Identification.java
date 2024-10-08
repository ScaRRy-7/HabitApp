package entrypoint;

import in.IdentificationReader;
import out.IdentificationWriter;
import storage.UsersStorage;
import validate.EmailValidator;
import wait.Waiter;

import java.util.Arrays;

public final class Identification {

    private final IdentificationWriter writer = new IdentificationWriter();
    private final IdentificationReader reader = new IdentificationReader();
    private final EmailValidator emailValidator = new EmailValidator();
    private final UsersStorage usersStorage = UsersStorage.getInstance();
    private final Authentication authentication = new Authentication();
    private final Registration registration = new Registration();
    private final Waiter waiter = new Waiter();

    public void start() {
        writer.writeGreetings();
        String email = reader.readEmail();

        if (emailValidator.isValid(email)) {
            // емэйл валиден, можно пробовать идентифицировать
            identificate(email);

        } else {
            // емэйл не валиден, необходимо сообщить об этом пользователю и запросить емэйл повторно
            writer.reportInvalidEmail();
            waiter.waitSecond();
            start();
        }
    }

    public void identificate(String email) {
        if (usersStorage.hasUser(email)) {
            //  пользователь с таким емэйлом существует - направляем на аутентификацию
            writer.infoRedirectAuthentication();
            waiter.waitSecond();
            authentication.login(email);
        } else {
            // пользователя с таким емэйлом не существует - направляем на регистрацию
            writer.infoRedirectRegistration();
            waiter.waitSecond();
            registration.registrate(email);
        }
    }
}
