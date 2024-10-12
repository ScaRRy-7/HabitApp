package menus;

import entities.*;
import enums.HabitCommand;
import habitchangers.*;
import in.Reader;
import out.HabitsRedactorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import wait.Waiter;
import org.slf4j.*;

public class HabitsRedactorMenu implements Commander {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final Waiter waiter = new Waiter();
    private final UsersController usersController = new UsersController();
    private User currentUser;
    private final HabitEditor habitEditor = new HabitEditor();
    private final HabitCreator habitCreator = new HabitCreator();
    private final HabitIndicator habitIndicator = new HabitIndicator();
    private final HabitRemover habitRemover = new HabitRemover();
    private final IncomplitedHabitsMenu incomplitedHabitsMenu = new IncomplitedHabitsMenu();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Logger logger = LoggerFactory.getLogger(HabitsRedactorMenu.class);

    public void start(User user) {
        logger.info("Запущено меню привычек");
        currentUser = user;
        selectCommand();
    }

    public void selectCommand() {
        writer.writeCommands();
        String commandString = reader.read();
        HabitCommand command;

        if (habitValidator.isValidCommand(commandString)) {
            logger.info("Пользователь ввел корректную команду");
            command = getHabitCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case CREATEHABIT:
                    logger.info("Пользователь выбрал создание новой привычки");
                    habitCreator.createNewHabit(currentUser);
                    break;
                case REDACTHABIT:
                    logger.info("Пользователь выбрал редактирование существующей привычки");
                    habitEditor.redactHabit(currentUser);
                    break;
                case MARKHABIT:
                    logger.info("Пользователь выбрал отметить привычку выполненной");
                    incomplitedHabitsMenu.start(currentUser);
                    break;
                case DELETEHABIT:
                    logger.info("Пользователь выбрал удалить привычку");
                    habitRemover.removeHabit(currentUser);
                    break;
                case SHOWHMYHABITS:
                    logger.info("Пользователь выбрал показать свои привычки");
                    habitIndicator.chooseSorting(currentUser);
                    break;
                case RETURNTOMENU:
                   return;
            }
        } else {
            logger.info("Пользователь ввел некорректную команду, выбор действия запустится снова");
            writer.reportInvalidCommand();
            waiter.waitSecond();
        }
        selectCommand();
    }

    private HabitCommand getHabitCommandByNumber(int commandNumber) {
        return switch (commandNumber) {
            case 1 -> HabitCommand.CREATEHABIT;
            case 2 -> HabitCommand.REDACTHABIT;
            case 3 -> HabitCommand.MARKHABIT;
            case 4 -> HabitCommand.DELETEHABIT;
            case 5 -> HabitCommand.SHOWHMYHABITS;
            default -> HabitCommand.RETURNTOMENU;

        };
    }


}
