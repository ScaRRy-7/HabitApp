package habitapp.services.entrypoint;

import habitapp.services.checkers.ExitChecker;
import habitapp.services.checkers.PasswordChecker;
import habitapp.services.in.Reader;
import habitapp.services.menus.AuthorizationMenu;
import habitapp.services.out.AuthenticationWriter;
import habitapp.repositories.HabitappRepository;
import habitapp.services.wait.Waiter;
import org.slf4j.*;

/**
 * Основная точка входа для процесса аутентификации.
 * Этот класс отвечает за обработку процесса входа в систему, включая валидацию пароля, проверку пароля и авторизацию пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class Authentication {

    private final AuthenticationWriter writer = new AuthenticationWriter();
    private final Reader reader = new Reader();
    //private final PasswordValidator passwordValidator = new PasswordValidator();
    private final Waiter waiter = new Waiter();
    private final PasswordChecker passwordChecker = new PasswordChecker();
    private final AuthorizationMenu authorizationMenu = new AuthorizationMenu();
    private final HabitappRepository habitappRepository = new HabitappRepository();
    private final Logger logger = LoggerFactory.getLogger(Authentication.class);

    /**
     * Обрабатывает процесс входа пользователя в систему.
     *
     * @param email электронный адрес пользователя, пытающегося войти в систему
     */
    public void login(String email) {
        writer.askPassword();
        String password = reader.read();

        // Если пользователь решил выйти
        ExitChecker.check(password);
        /*
        if (passwordValidator.isValid(password)) {
            if (passwordChecker.checkPassword(email, password)) {
                User user = usersRepository.getUserFromDatabase(email);
                authorizationMenu.start(user);
            } else {
                writer.infoIncorrectPassword();
                waiter.waitSecond();
                login(email);
            }
        } else {
            writer.reportInvalidPassword();
            waiter.waitSecond();
            login(email);
        }

         */
    }
}
