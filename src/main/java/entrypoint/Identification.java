package entrypoint;

import adminstration.AdminPanel;
import in.Reader;
import out.IdentificationWriter;
import storage.UsersStorage;
import validate.EmailValidator;
import wait.Waiter;
import org.slf4j.*;
import java.util.ResourceBundle;

public final class Identification {

    private final IdentificationWriter writer = new IdentificationWriter();
    private final Reader reader = new Reader();
    private final EmailValidator emailValidator = new EmailValidator();
    private final UsersStorage usersStorage = UsersStorage.getInstance();
    private final Authentication authentication = new Authentication();
    private final Registration registration = new Registration();
    private final Waiter waiter = new Waiter();
    private final String ADMIN_PROPERTIES_FILE = "admin";
    ResourceBundle adminBundle = ResourceBundle.getBundle(ADMIN_PROPERTIES_FILE);
    private final AdminPanel adminPanel = new AdminPanel();
    private final Logger logger = LoggerFactory.getLogger(Identification.class);

    public void start() {
        writer.writeGreetings();
        String email = reader.read();

        if (adminBundle.getString("admin.email").equals(email)) {
            logger.info("Пользователь ввел почту админа");
            adminPanel.authentication();
        }

        if (emailValidator.isValid(email)) {
            // емэйл валиден, можно пробовать идентифицировать
            logger.info("Эмейл валиден");
            identificate(email);

        } else {
            // емэйл не валиден, необходимо сообщить об этом пользователю и запросить емэйл повторно
            logger.info("Эмейл не валиден, повторный запрос емэйла");
            writer.reportInvalidEmail();
            waiter.waitSecond();
            start();
        }
    }

    public void identificate(String email) {
        if (usersStorage.hasUser(email)) {
            if (usersStorage.getUser(email).isBlocked()) {
                logger.info("Пользователю отказано в доступе так как он заблокирован");
                writer.infoUserBlocked();
                waiter.waitSecond();
                Start.main(null);
            }
            //  пользователь с таким емэйлом существует - направляем на аутентификацию
            logger.info("Пользователь с такой почтой уже существует, направляем на аутентификацию");
            writer.infoRedirectAuthentication();
            waiter.waitSecond();
            authentication.login(email);
        } else {
            // пользователя с таким емэйлом не существует - направляем на регистрацию
            logger.info("Пользователь с таким емэйлом не существует - направляем на регистрацию");
            writer.infoRedirectRegistration();
            waiter.waitSecond();
            registration.registrate(email);
        }
    }
}
