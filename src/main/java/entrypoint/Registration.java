package entrypoint;

import in.Reader;
import menus.AuthorizationMenu;
import out.RegistrationWriter;
import entities.User;
import storage.UsersController;
import validate.NameValidator;
import validate.PasswordValidator;
import wait.Waiter;

public final class Registration {

    private final RegistrationWriter writer = new RegistrationWriter();
    private final Reader reader = new Reader();
    private final NameValidator nameValidator = new NameValidator();
    private final Waiter waiter = new Waiter();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final UsersController usersController = new UsersController();
    private final AuthorizationMenu authorizationMenu = new AuthorizationMenu();

    public void registrate(String email) {
        writer.askName();
        String name = reader.read();

        if (nameValidator.isValid(name)) {
            // имя указано корректно, можно переходить к созданию пароля
            createPassword(email, name);

        } else {
            // имя указано некорректно (пустое/символы не из латинского алфавита или кириллицы)
            // сообщаем пользователю о некорректности имени и просим ввести его заново
            writer.reportInvalidName();
            waiter.waitSecond();
            registrate(email);
        }
    }

    public void createPassword(String email, String name) {
        writer.askPassword();
        String password = reader.read();

        if (passwordValidator.isValid(password)) {
            // пароль валиден, создаем пользователя
            User user = new User(name, email, password);
            usersController.addUserToDatabase(user);
            authorizationMenu.start(user);


        } else {
            // пароль не валиден
            writer.reportInvalidPassword();
            waiter.waitSecond();
            createPassword(email, name);
        }
    }
}
