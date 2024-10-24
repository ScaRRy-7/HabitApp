package habitapp.services.statistics;

import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.services.habitchangers.HabitUnmarker;
import habitapp.services.out.DayStatisticsHabitWriter;

/**
 * Отвечает за создание и отображение ежедневной статистики по выбранной привычке пользователя.
 * Класс использует {@link HabitUnmarker} для проверки и обновления статуса невыполненных привычек пользователя,
 * а также {@link DayStatisticsHabitWriter} для вывода статистики по выбранной привычке.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class DayStatisticsHabit implements StatisticsCreator {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final DayStatisticsHabitWriter writer = new DayStatisticsHabitWriter();

    /**
     * Получает статистику по выбранной привычке пользователя за текущий день.
     * Сначала проверяет и обновляет статус невыполненных привычек пользователя,
     * затем выводит статистику по выбранной привычке.
     *
     * @param user пользователь, для которого запрашивается статистика
     * @param habit привычка, по которой запрашивается статистика
     */
    @Override
    public void getStatistics(User user, Habit habit) {
//        habitUnmarker.checkHabits(user);
//        writer.writeTodayStatistics(habit);
    }
}
