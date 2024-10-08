package entrypoint;

import out.RegistrationWriter;
import in.RegistrationReader;
import storage.User;
import storage.UsersController;
import storage.UsersStorage;
import validate.NameValidator;
import validate.PasswordValidator;
import wait.Waiter;

public final class Registration {

    private static final Registration registration = new Registration();

    private final RegistrationWriter writer = RegistrationWriter.getInstance();
    private final RegistrationReader reader = RegistrationReader.getInstance();
    private final NameValidator nameValidator = NameValidator.getInstance();
    private final Waiter waiter = Waiter.getInstance();
    private final PasswordValidator passwordValidator = PasswordValidator.getInstance();
    private final UsersController usersController = UsersController.getInstance();

    private Registration() {}

    public static Registration getInstance() {
        return registration;
    }

    public void registrate(String email) {
        writer.askName();
        String name = reader.getName();

        if (nameValidator.isValid(name)) {
            // имя указано корректно, можно переходить к созданию пароля
            createPassword(email, name);

        } else {
            // имя указано некорректно (пустое/символы не из латинского алфавита или кириллицы)
            // сообщаем пользователю о некорректности имени и просим ввести его заново
            writer.reportInvalidName();
            waiter.wait(1);
            registrate(email);
        }
    }

    public void createPassword(String email, String name) {
        writer.askPassword();
        String password = reader.getPassword();

        if (passwordValidator.isValid(password)) {
            // пароль валиден, создаем пользователя
            User user = new User(email, name, password);
            usersController.addUserToDatabase(user);


        } else {
            // пароль не валиден
            writer.reportInvalidPassword();
            waiter.wait(1);
            createPassword(email, name);
        }
    }
}
