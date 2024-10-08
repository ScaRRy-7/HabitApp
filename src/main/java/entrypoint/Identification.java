package entrypoint;

import in.IdentificationReader;
import out.IdentificationWriter;
import storage.UsersStorage;
import validate.EmailValidator;

public final class Identification {

    private static final Identification identification = new Identification();

    private final IdentificationWriter writer = IdentificationWriter.getInstance();
    private final IdentificationReader reader = IdentificationReader.getInstance();
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final UsersStorage usersStorage = UsersStorage.getInstance();
    private final Authentication authentication = Authentication.getInstance();
    private final Registration registration = Registration.getInstance();

    private Identification() {}

    public static Identification getInstance() {
        return identification;
    }

    public void start() {
        writer.writeGreetings();
        String email = reader.readEmail();

        if (emailValidator.isValid(email)) {
            // емэйл валиден, можно пробовать идентифицировать
            identificate(email);

        } else {
            // емэйл не валиден, необходимо сообщить об этом пользователю и запросить емэйл повторно
            writer.reportInvalidEmail();
            start();
        }
    }

    public void identificate(String email) {
        if (usersStorage.hasUser(email)) {
            //  пользователь с таким емэйлом существует - направляем на аутентификацию
            authentication.login(email);
        } else {
            // пользователя с таким емэйлом не существует - направляем на регистрацию
            registration.registrate(email);
        }

    }
}
