package services.menus;

import services.entities.User;
import services.enums.StatisticsCommand;
import services.in.Reader;
import services.out.HabitsStatisticsMenuWriter;
import services.statistics.ProgressReport;
import services.statistics.StreakCalculator;
import services.statistics.SuccessCalculator;
import repositories.UsersRepository;
import services.validate.StatisticsValidator;
import services.wait.Waiter;
import org.slf4j.*;

/**
 * Отвечает за предоставление пользователю меню для просмотра статистики по его привычкам.
 * Это меню позволяет пользователю выполнять следующие действия:
 * 1. Просмотреть статистику по привычкам за определенный период
 * 2. Рассчитать количество стриков по привычкам
 * 3. Рассчитать процент успешного выполнения привычек за определенный период
 * 4. Сгенерировать отчет по завершенным привычкам
 * 5. Вернуться в предыдущее меню
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitsStatisticsMenu implements Commander {

    private User currentUser;
    private final HabitsStatisticsMenuWriter writer = new HabitsStatisticsMenuWriter();
    private final Reader reader = new Reader();
    private final StatisticsValidator statisticsValidator = new StatisticsValidator();
    private final Waiter waiter = new Waiter();
    private final ChooserStatisticsPeriodMenu chooserStatisticsPeriodMenu = new ChooserStatisticsPeriodMenu();
    private final StreakCalculator streakCalculator = new StreakCalculator();
    private final SuccessCalculator successCalculator = new SuccessCalculator();
    private final ProgressReport progressReport = new ProgressReport();
    private final Logger logger = LoggerFactory.getLogger(HabitsStatisticsMenu.class);
    private final UsersRepository usersRepository = new UsersRepository();


    /**
     * Запускает меню статистики привычек для авторизованного пользователя.
     * Если у пользователя есть привычки, отображается список доступных команд.
     * Если у пользователя нет привычек, отображается сообщение, что просмотр статистики невозможен.
     *
     * @param user авторизованный пользователь
     */
    public void start(User user) {
        logger.info("Запущено меню статистики");
        this.currentUser = user;
        if (!usersRepository.getAllHabits(user).isEmpty()) {
            logger.debug("У пользователя есть привычки, запускается выбор команды");
            selectCommand();
        } else {
            logger.debug("У Пользователя отсутствуют привычки, просмотр статистики невозможен");
            writer.writeNoHabitsForStatistics();
            waiter.waitSecond();
            return;
        }
    }

    /**
     * Отображает список команд меню статистики привычек и обрабатывает выбранную пользователем команду.
     * Если пользователь выбрал корректную команду, выполняется соответствующая функциональность.
     * Если пользователь выбрал некорректную команду, пользователю предлагается ввести команду еще раз.
     */
    public void selectCommand() {
        logger.debug("Пользователь выбирает команду");
        writer.writeCommands();
        String commandString = reader.read();
        StatisticsCommand command;

        if (statisticsValidator.isValidCommand(commandString)) {
            logger.debug("Пользователь выбрал валидную команду");
            command = getStatisticsCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case STATISTICS_FOR_PERIOD:
                    logger.debug("Пользователь выбрал просмотр статистики за период");
                    chooserStatisticsPeriodMenu.start(currentUser);
                    break;
                case CALCULATE_STREAKS:
                    logger.debug("Пользователь выбрал подсчет стриков для привычек");
                    streakCalculator.start(currentUser);
                    break;
                case CALCULATE_SUCCESS_COMPLETION_FOR_PERIOD:
                    logger.debug("Пользователь выбрал посчитать процент выполняемости привычек");
                    successCalculator.start(currentUser);
                    break;
                case GENERATE_COMPLETION_REPORT:

                    logger.debug("Пользователь выбрал генерацию отчета по привычкам");
                    progressReport.generateReport(currentUser);
                    break;
                case RETURN_TO_MENU:
                    logger.debug("Пользователь выбрал возврат");
                    return;
            }
        } else {
            logger.debug("Пользователь ввел невалидную команду, запрос команды повторится");
            writer.reportInvalidCommand();
            waiter.waitSecond();
        }
        selectCommand();
    }

    /**
     * Преобразует число, введенное пользователем, в соответствующую команду меню статистики привычек.
     *
     * @param command номер команды, введенный пользователем
     * @return соответствующая команда меню
     */
    StatisticsCommand getStatisticsCommandByNumber(int command) {
        return switch (command) {
            case 1 -> StatisticsCommand.STATISTICS_FOR_PERIOD;
            case 2 -> StatisticsCommand.CALCULATE_STREAKS;
            case 3 -> StatisticsCommand.CALCULATE_SUCCESS_COMPLETION_FOR_PERIOD;
            case 4 -> StatisticsCommand.GENERATE_COMPLETION_REPORT;
            default -> StatisticsCommand.RETURN_TO_MENU;
        };
    }
}
