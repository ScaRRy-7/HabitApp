package habitapp.services.entrypoint;

import habitapp.services.in.Reader;
import habitapp.services.menus.AuthorizationMenu;
import habitapp.services.out.RegistrationWriter;
import habitapp.entities.User;
import habitapp.repositories.UsersRepository;
import habitapp.services.validate.NameValidator;
import habitapp.services.validate.PasswordValidator;
import habitapp.services.wait.Waiter;
import org.slf4j.*;

/**
 * Отвечает за процесс регистрации нового пользователя.
 * Этот класс отвечает за сбор необходимой информации от пользователя (имя и пароль), валидацию этой информации
 * и создание нового пользователя в системе.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class Registration {

    private final RegistrationWriter writer = new RegistrationWriter();
    private final Reader reader = new Reader();
    private final NameValidator nameValidator = new NameValidator();
    private final Waiter waiter = new Waiter();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final UsersRepository usersRepository = new UsersRepository();
    private final AuthorizationMenu authorizationMenu = new AuthorizationMenu();
    private final Logger logger = LoggerFactory.getLogger(Registration.class);

    /**
     * Выполняет процесс регистрации нового пользователя.
     *
     * @param email email нового пользователя
     */
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

    /**
     * Создает новый пароль для нового пользователя.
     * Валидирует введенный пользователем пароль и создает нового пользователя в базе данных.
     *
     * @param email email нового пользователя
     * @param name  имя нового пользователя
     */
    public void createPassword(String email, String name) {
        writer.askPassword();
        String password = reader.read();

        if (passwordValidator.isValid(password)) {
            // пароль валиден, создаем пользователя
            logger.info("Пользователь указал валидный пароль, создается новый юзер в БД");
            User user = new User(name, email, password);
            usersRepository.addUserToDatabase(user);
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
