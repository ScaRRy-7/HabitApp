package entrypoint;

import in.Reader;
import menus.AuthorizationMenu;
import out.RegistrationWriter;
import entities.User;
import storage.UsersController;
import validate.NameValidator;
import validate.PasswordValidator;
import wait.Waiter;
import org.slf4j.*;

public final class Registration {

    private final RegistrationWriter writer = new RegistrationWriter();
    private final Reader reader = new Reader();
    private final NameValidator nameValidator = new NameValidator();
    private final Waiter waiter = new Waiter();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final UsersController usersController = new UsersController();
    private final AuthorizationMenu authorizationMenu = new AuthorizationMenu();
    private final Logger logger = LoggerFactory.getLogger(Registration.class);

    public void registrate(String email) {
        writer.askName();
        String name = reader.read();

        if (nameValidator.isValid(name)) {
            logger.info("Пользователь указаал валидное имя, запускается создание пароля");
            // имя указано корректно, можно переходить к созданию пароля
            createPassword(email, name);

        } else {
            // имя указано некорректно (пустое/символы не из латинского алфавита или кириллицы)
            // сообщаем пользователю о некорректности имени и просим ввести его заново
            logger.debug("Пользователь указаал не валидное имя, имя запрашивается снова");
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
            logger.info("Пользователь указал валидный пароль, создается новый юзер в БД");
            User user = new User(name, email, password);
            usersController.addUserToDatabase(user);
            authorizationMenu.start(user);


        } else {
            // пароль не валиден
            logger.debug("Пользователь указал не валидный пароль, пароль спрашивается снова");
            writer.reportInvalidPassword();
            waiter.waitSecond();
            createPassword(email, name);
        }
    }
}
