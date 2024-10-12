package menus;

import entrypoint.Start;
import in.Reader;
import out.AuthorizationWriter;
import entities.User;
import validate.CommandAuthorizationValidator;
import wait.Waiter;
import enums.MenuCommand;
import org.slf4j.*;

public final class AuthorizationMenu implements Commander {

    private final AuthorizationWriter writer = new AuthorizationWriter();
    private final Reader reader = new Reader();
    private final HabitsRedactorMenu habitsRedactorMenu = new HabitsRedactorMenu();
    private final HabitsStatisticsMenu habitsStatisticsMenu = new HabitsStatisticsMenu();
    private final ProfileMenu profileMenu = new ProfileMenu();
    private final CommandAuthorizationValidator commandValidator = new CommandAuthorizationValidator();
    private final Waiter waiter = new Waiter();
    private User currentUser;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void start(User user) {
        logger.info("меню авторизации запущено");
        currentUser = user;
        writer.greetings(user);
        selectCommand();
    }

    public void selectCommand() {
        logger.info("выведен список команд");
        writer.getCommands();
        String commandString = (reader.read());
        MenuCommand command;

        if (commandValidator.isValid(commandString)) {
            logger.info("Пользователь написал корректную команду");
            command = getMenuCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case REDACTPROFILE:
                    logger.info("Пользователь выбрал редактирование профиля, запускается меню профиля");
                    profileMenu.start(currentUser);
                    break;
                case MANAGEHABITS:
                    logger.info("Пользователь выбрал редактирование привычек, запускается меню привычек");
                    habitsRedactorMenu.start(currentUser);
                    break;
                case HABITSTATISTICS:
                    logger.info("ПОльзователь выбрал статистику, запускается меню статистики привычек");
                    habitsStatisticsMenu.start(currentUser);
                    break;
                case EXIT:
                    logger.info("Пользователь вышел из аккаунта");
                    Start.main(null);
                    break;
            }
        } else {
            logger.info("Пользователь написал некорректную команду, ввод запрашивается снова");
            writer.reportInvalidCommand();
            waiter.waitSecond();
            selectCommand();
        }
        selectCommand();
    }

    private MenuCommand getMenuCommandByNumber(int commandNumber) {
        return switch (commandNumber) {
            case 1 -> MenuCommand.REDACTPROFILE;
            case 2 -> MenuCommand.MANAGEHABITS;
            case 3 -> MenuCommand.HABITSTATISTICS;
            case 4 -> MenuCommand.EXIT;
            default -> throw new IllegalArgumentException("Invalid command number");
        };
    }


}
