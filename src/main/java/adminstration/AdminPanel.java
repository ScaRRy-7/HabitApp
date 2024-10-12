package adminstration;

import entrypoint.Start;
import enums.AdminCommand;
import in.Reader;
import out.AdministationPanelWriter;
import validate.AdminPanelValidator;
import wait.Waiter;
import org.slf4j.*;
import java.util.ResourceBundle;

public class AdminPanel {

    private final Reader reader = new Reader();
    private final String ADMIN_PROPERTIES_FILE = "admin";
    private final ResourceBundle adminBundle = ResourceBundle.getBundle(ADMIN_PROPERTIES_FILE);
    private final AdministationPanelWriter writer = new AdministationPanelWriter();
    private final AdminPanelValidator validator = new AdminPanelValidator();
    private final Waiter waiter = new Waiter();
    private final AdminBlocator adminBlocator = new AdminBlocator();
    private final AdminRemover adminRemover = new AdminRemover();
    private final Logger logger = LoggerFactory.getLogger(AdminPanel.class);

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

    private AdminCommand getAdminCommandByNum(int num) {
        return switch (num) {
            case 1 -> AdminCommand.BLOCK_USER;
            case 2 -> AdminCommand.DELETE_USER;
            default -> AdminCommand.EXIT;
        };
    }
}
