package habitapp.services.entrypoint;

import habitapp.services.adminstration.AdminPanel;
import habitapp.services.in.Reader;
import habitapp.services.out.IdentificationWriter;
import habitapp.repositories.HabitappDAO;
import habitapp.services.wait.Waiter;
import org.slf4j.*;
import java.util.ResourceBundle;

/**
 * Основная точка входа для процесса идентификации пользователей.
 * Этот класс отвечает за обработку процесса идентификации и направления пользователей на регистрацию или аутентификацию.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class Identification {

    private final IdentificationWriter writer = new IdentificationWriter();
    private final Reader reader = new Reader();
    //private final EmailValidator emailValidator = new EmailValidator();
    private final HabitappDAO habitappDAO = HabitappDAO.getInstance();
    private final Authentication authentication = new Authentication();
    private final Registration registration = new Registration();
    private final Waiter waiter = new Waiter();
    private final String ADMIN_PROPERTIES_FILE = "admin";
    ResourceBundle adminBundle = ResourceBundle.getBundle(ADMIN_PROPERTIES_FILE);
    private final AdminPanel adminPanel = new AdminPanel();
    private final Logger logger = LoggerFactory.getLogger(Identification.class);

    /**
     * Запускает процесс идентификации пользователя.
     * Принимает email пользователя, проверяет его валидность и направляет пользователя на регистрацию или аутентификацию.
     */
    public void start() {
        writer.writeGreetings();
        String email = reader.read();

        if (adminBundle.getString("admin.email").equals(email)) {
            logger.info("Пользователь ввел почту админа");
            adminPanel.authentication();
        }

        /*if (emailValidator.isValid(email)) {
            logger.info("Эмейл валиден");
            identificate(email);

        } else {
            logger.info("Эмейл не валиден, повторный запрос емэйла");
            writer.reportInvalidEmail();
            waiter.waitSecond();
            start();
        }

         */
    }

    /**
     * Идентифицирует пользователя по его email.
     * Если пользователь уже существует, направляет его на аутентификацию.
     * Если пользователь не существует, направляет его на регистрацию.
     *
     * @param email email пользователя
     */
//    public void identificate(String email) {
//        if (usersDAO.hasUser(email)) {
//            if (usersDAO.getUser(email).isBlocked()) {
//                logger.info("Пользователю отказано в доступе так как он заблокирован");
//                writer.infoUserBlocked();
//                waiter.waitSecond();
//                Start.main(null);
//            }
//            //  пользователь с таким емэйлом существует - направляем на аутентификацию
//            logger.info("Пользователь с такой почтой уже существует, направляем на аутентификацию");
//            writer.infoRedirectAuthentication();
//            waiter.waitSecond();
//            authentication.login(email);
//        } else {
//            // пользователя с таким емэйлом не существует - направляем на регистрацию
//            logger.info("Пользователь с таким емэйлом не существует - направляем на регистрацию");
//            writer.infoRedirectRegistration();
//            waiter.waitSecond();
//            registration.registrate(email);
//        }
//    }

}
