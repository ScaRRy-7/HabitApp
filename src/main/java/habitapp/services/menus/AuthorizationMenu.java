package habitapp.services.menus;

import habitapp.entities.User;
import habitapp.services.entrypoint.Start;
import habitapp.services.validate.CommandAuthorizationValidator;
import habitapp.services.in.Reader;
import habitapp.services.out.AuthorizationWriter;
import habitapp.services.wait.Waiter;
import habitapp.services.enums.MenuCommand;
import org.slf4j.*;

/**
 * Отвечает за отображение меню авторизованного пользователя и обработку выбранных им команд.
 * Это меню предоставляет пользователю следующие возможности:
 * 1. Редактирование профиля
 * 2. Управление привычками
 * 3. Просмотр статистики привычек
 * 4. Выход из системы
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class AuthorizationMenu implements Commander {

    private final AuthorizationWriter writer = new AuthorizationWriter();
    private final Reader reader = new Reader();
    private final HabitsRedactorMenu habitsRedactorMenu = new HabitsRedactorMenu();
    private final HabitsStatisticsMenu habitsStatisticsMenu = new HabitsStatisticsMenu();
    //private final ProfileMenu profileMenu = new ProfileMenu();
    private final CommandAuthorizationValidator commandValidator = new CommandAuthorizationValidator();
    private final Waiter waiter = new Waiter();
    private User currentUser;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Запускает меню авторизованного пользователя.
     * Отображает приветствие для пользователя и предлагает ему выбрать команду из списка.
     *
     * @param user авторизованный пользователь
     */
    public void start(User user) {
        logger.info("меню авторизации запущено");
        currentUser = user;
        writer.greetings(user);
        selectCommand();
    }

    /**
     * Отображает список команд меню авторизованного пользователя и обрабатывает выбранную пользователем команду.
     * Если пользователь выбрал корректную команду, выполняется соответствующая функциональность.
     * Если пользователь выбрал некорректную команду, пользователю предлагается ввести команду еще раз.
     */
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
                    //profileMenu.start(currentUser);
                    break;
                case MANAGEHABITS:
                    logger.info("Пользователь выбрал редактирование привычек, запускается меню привычек");
                   // habitsRedactorMenu.start(currentUser);
                    break;
                case HABITSTATISTICS:
                    logger.info("ПОльзователь выбрал статистику, запускается меню статистики привычек");
                   // habitsStatisticsMenu.start(currentUser);
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

/**
 * Преобразует число, введенное пользователем, в соответствующую команду меню авторизованного пользователя.
 *
 * @param commandNumber номер команды, введенный пользователем
 * @return соответствующая команда меню
 * @throws IllegalArgumentException если введен неверный номер команды
 */
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