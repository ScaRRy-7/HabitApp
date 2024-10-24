package habitapp.services.adminstration;

import habitapp.services.entrypoint.Start;
import habitapp.services.enums.AdminCommand;
import habitapp.services.in.Reader;
import habitapp.services.out.AdministationPanelWriter;
import habitapp.services.validate.AdminPanelValidator;
import habitapp.services.wait.Waiter;
import org.slf4j.*;
import java.util.ResourceBundle;

/**
 * Класс AdminPanel отвечает за работу панели администратора.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class AdminPanel {
    /**
     * Объект класса Reader для чтения ввода пользователя.
     */
    private final Reader reader = new Reader();

    /**
     * Имя файла с настройками администратора.
     */
    private final String ADMIN_PROPERTIES_FILE = "admin";

    /**
     * Объект ResourceBundle для получения настроек администратора из файла.
     */
    private final ResourceBundle adminBundle = ResourceBundle.getBundle(ADMIN_PROPERTIES_FILE);

    /**
     * Объект класса AdministationPanelWriter для записи сообщений для администратора.
     */
    private final AdministationPanelWriter writer = new AdministationPanelWriter();

    /**
     * Объект класса AdminPanelValidator для валидации ввода пользователя.
     */
    private final AdminPanelValidator validator = new AdminPanelValidator();

    /**
     * Объект класса Waiter для ожидания определенного времени.
     */
    private final Waiter waiter = new Waiter();

    /**
     * Объект класса AdminBlocator для блокировки пользователей.
     */
    private final AdminBlocator adminBlocator = new AdminBlocator();

    /**
     * Объект класса AdminRemover для удаления пользователей.
     */
    private final AdminRemover adminRemover = new AdminRemover();

    /**
     * Объект класса Logger для логирования информации.
     */
    private final Logger logger = LoggerFactory.getLogger(AdminPanel.class);

    /**
     * Выполняет аутентификацию администратора.
     */
    public void authentication() {
        writer.askNickname();
        String nickName = reader.read();
        writer.askPassword();
        String password = reader.read();

        if (nickName.equals(adminBundle.getString("admin.nickname")) && password.equals(adminBundle.getString("admin.password"))) {
            logger.debug("Пользователь ввел корректные данные админа, запускается админка");
            start();
        } else {
            logger.debug("Пользователь ввел неправильный ник админа или пароль админа");
            writer.reportHackTry();
            System.exit(0);
        }
    }

    /**
     * Запускает панель администратора.
     */
    private void start() {
        logger.info("Админка запустилась");
        writer.writeAdminCommands();
        String commandString = reader.read();

        if (validator.isValidCommand(commandString)) {
            logger.info("Пользователь ввел корректную команду");
            AdminCommand adminCommand = getAdminCommandByNum(Integer.parseInt(commandString));
            switch (adminCommand) {
                case BLOCK_USER:
                    adminBlocator.blockUser();
                    break;
                case DELETE_USER:
                    adminRemover.removeUser();
                    break;
                case EXIT:
                    Start.main(null);
            }
        } else {
            logger.debug("Пользователь ввел некорректную команду");
            writer.reportInvalidCommand();
            waiter.waitSecond();
        }
        start();
    }

    /**
     * Возвращает команду администратора по ее номеру.
     *
     * @param num номер команды администратора
     * @return команда администратора
     */
    private AdminCommand getAdminCommandByNum(int num) {
        return switch (num) {
            case 1 -> AdminCommand.BLOCK_USER;
            case 2 -> AdminCommand.DELETE_USER;
            default -> AdminCommand.EXIT;
        };
    }
}
