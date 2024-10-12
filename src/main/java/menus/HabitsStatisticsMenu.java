package menus;

import entities.User;
import enums.StatisticsCommand;
import in.Reader;
import out.HabitsStatisticsMenuWriter;
import statistics.ProgressReport;
import statistics.StreakCalculator;
import statistics.SuccessCalculator;
import validate.StatisticsValidator;
import wait.Waiter;
import org.slf4j.*;

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

    public void start(User user) {
        logger.info("Запущено меню статистики");
        this.currentUser = user;
        if (!user.getHabits().isEmpty()) {
            logger.debug("У пользователя есть привычки, запускается выбор команды");
            selectCommand();
        } else {
            logger.debug("У Пользователя отсутствуют привычки, просмотр статистики невозможен");
            writer.writeNoHabitsForStatistics();
            waiter.waitSecond();
            return;
        }
    }

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
