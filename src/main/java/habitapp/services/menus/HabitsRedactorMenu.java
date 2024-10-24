package habitapp.services.menus;

import habitapp.entities.User;
import habitapp.repositories.HabitappRepository;
import habitapp.services.habitchangers.*;
import habitapp.services.validate.CommandHabitValidator;
import habitapp.services.enums.HabitCommand;

import habitapp.services.in.Reader;
import habitapp.services.out.HabitsRedactorWriter;
import habitapp.services.wait.Waiter;
import org.slf4j.*;

/**
 * Отвечает за предоставление пользователю меню для управления его привычками.
 * Это меню позволяет пользователю выполнять следующие действия:
 * 1. Создание новой привычки
 * 2. Редактирование существующей привычки
 * 3. Отметка привычки как выполненной
 * 4. Удаление привычки
 * 5. Просмотр списка своих привычек
 * 6. Возврат в предыдущее меню
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitsRedactorMenu implements Commander {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final Waiter waiter = new Waiter();
    private final HabitappRepository habitappRepository = new HabitappRepository();
    private User currentUser;
    private final HabitEditor habitEditor = new HabitEditor();
    private final HabitCreator habitCreator = new HabitCreator();
    private final HabitIndicator habitIndicator = new HabitIndicator();
    private final HabitRemover habitRemover = new HabitRemover();
    private final IncomplitedHabitsMenu incomplitedHabitsMenu = new IncomplitedHabitsMenu();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Logger logger = LoggerFactory.getLogger(HabitsRedactorMenu.class);

    /**
     * Запускает меню управления привычками для авторизованного пользователя.
     *
     * @param user авторизованный пользователь
     */
    public void start(User user) {
        logger.info("Запущено меню привычек");
        currentUser = user;
        selectCommand();
    }

    /**
     * Отображает список команд меню управления привычками и обрабатывает выбранную пользователем команду.
     * Если пользователь выбрал корректную команду, выполняется соответствующая функциональность.
     * Если пользователь выбрал некорректную команду, пользователю предлагается ввести команду еще раз.
     */
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
                    //habitEditor.redactHabit(currentUser);
                    break;
                case MARKHABIT:
                    logger.info("Пользователь выбрал отметить привычку выполненной");
                   // incomplitedHabitsMenu.start(currentUser);
                    break;
                case DELETEHABIT:
                    logger.info("Пользователь выбрал удалить привычку");
                   // habitRemover.removeHabit(currentUser);
                    break;
                case SHOWHMYHABITS:
                    logger.info("Пользователь выбрал показать свои привычки");
                   // habitIndicator.chooseSorting(currentUser);
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

    /**
     * Преобразует число, введенное пользователем, в соответствующую команду меню управления привычками.
     *
     * @param commandNumber номер команды, введенный пользователем
     * @return соответствующая команда меню
     */
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