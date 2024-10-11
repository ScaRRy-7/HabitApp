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


    public void start(User user) {
        this.currentUser = user;
        if (!user.getHabits().isEmpty()) {
            selectCommand();
        } else {
            writer.writeNoHabitsForStatistics();
            waiter.waitSecond();
            return;
        }
    }

    public void selectCommand() {
        writer.writeCommands();
        String commandString = reader.read();
        StatisticsCommand command;

        if (statisticsValidator.isValidCommand(commandString)) {
            command = getStatisticsCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case STATISTICS_FOR_PERIOD:
                    chooserStatisticsPeriodMenu.start(currentUser);
                    break;
                case CALCULATE_STREAKS:
                    streakCalculator.start(currentUser);
                    break;
                case CALCULATE_SUCCESS_COMPLETION_FOR_PERIOD:
                    successCalculator.start(currentUser);
                    break;
                case GENERATE_COMPLETION_REPORT:
                    progressReport.generateReport(currentUser);
                    break;
                case RETURN_TO_MENU:
                    return;
            }
        } else {
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