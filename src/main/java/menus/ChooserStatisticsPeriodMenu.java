package menus;

import entities.Habit;
import entities.User;
import enums.Period;
import enums.StatisticsCommand;
import habitchangers.HabitChooser;
import in.Reader;
import out.ChooserStatisticsPeriodMenuWriter;
import statistics.DayStatisticsHabit;
import statistics.MonthStatisticsHabit;
import statistics.WeekStatisticsHabit;
import validate.StatisticsPeriodMenuValidator;
import wait.Waiter;

/**
 * Отвечает за предоставление пользователю возможности выбрать период для просмотра статистики по его привычкам.
 * Пользователю предлагается выбрать период из следующих вариантов:
 * 1. День
 * 2. Неделя
 * 3. Месяц
 * В зависимости от выбранного периода, отображается соответствующая статистика по выбранной пользователем привычке.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class ChooserStatisticsPeriodMenu {

    private final ChooserStatisticsPeriodMenuWriter writer = new ChooserStatisticsPeriodMenuWriter();
    private final Reader reader = new Reader();
    private final Waiter waiter = new Waiter();
    private final StatisticsPeriodMenuValidator validator = new StatisticsPeriodMenuValidator();
    private final DayStatisticsHabit dayStatisticsHabit = new DayStatisticsHabit();
    private final WeekStatisticsHabit weekStatisticsHabit = new WeekStatisticsHabit();
    private final MonthStatisticsHabit monthStatisticsHabit = new MonthStatisticsHabit();
    private final HabitChooser habitChooser = new HabitChooser();
    User currentUser;

    /**
     * Запускает меню выбора периода для просмотра статистики по привычкам пользователя.
     *
     * @param user авторизованный пользователь
     */
    public void start(User user) {
        currentUser = user;
        selectPeriod();
    }

    /**
     * Отображает список периодов для просмотра статистики и обрабатывает выбор пользователя.
     * Если пользователь выбрал корректный период, отображается соответствующая статистика по выбранной привычке.
     * Если пользователь выбрал некорректный период, пользователю предлагается выбрать период еще раз.
     */
    private void selectPeriod() {
        writer.writePeriods();
        String periodString = reader.read();
        Period period;

        if (validator.isValidPeriod(periodString)) {
            period = getPeriodByNumber(Integer.parseInt(periodString));
            Habit habit = habitChooser.chooseHabit(currentUser);
            switch (period) {
                case DAY:
                    dayStatisticsHabit.getStatistics(currentUser, habit);
                    return;
                case WEEK:
                    weekStatisticsHabit.getStatistics(currentUser, habit);
                    return;
                case MONTH:
                    monthStatisticsHabit.getStatistics(currentUser, habit);
                    return;

            }
        } else {
            writer.reportInvalidPeriod();
            waiter.waitSecond();
        }
        selectPeriod();
    }

    /**
     * Преобразует число, введенное пользователем, в соответствующий период для просмотра статистики.
     *
     * @param periodNumber номер периода, введенный пользователем
     * @return соответствующий период
     */
    Period getPeriodByNumber(int periodNumber) {
        return switch (periodNumber) {
            case 1 -> Period.DAY;
            case 2 -> Period.WEEK;
            default -> Period.MONTH;
        };
    }
}
