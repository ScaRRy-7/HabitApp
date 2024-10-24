package services.out;

import services.entities.Habit;
import services.entities.User;
import services.enums.StatisticsCommand;
import services.habitchangers.HabitUnmarker;
import repositories.UsersRepository;

public class HabitsStatisticsMenuWriter {

    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final UsersRepository usersRepository = new UsersRepository();

    public void writeHabits(User user) {
        habitUnmarker.checkHabits(user);
        int num = 1;
        for (Habit habit : usersRepository.getAllHabits(user)) {
            System.out.println(num + " - " + habit + "\n");
            num++;
        }
    }

    public void writeCommands() {
        System.out.println("Список доступных  команд, введи соотвествующую цифру для выбора команды:" +
                "\n\t1 - " + StatisticsCommand.STATISTICS_FOR_PERIOD.getName() +
                "\n\t2 - " + StatisticsCommand.CALCULATE_STREAKS.getName() +
                "\n\t3 - " + StatisticsCommand.CALCULATE_SUCCESS_COMPLETION_FOR_PERIOD.getName() +
                "\n\t4 - " + StatisticsCommand.GENERATE_COMPLETION_REPORT.getName() +
                "\n\t5 - " + StatisticsCommand.RETURN_TO_MENU.getName());
    }

    public void reportInvalidCommand() {
        System.out.println("Ошибка! некорректно указан номер команды");
    }

    public void writeNoHabitsForStatistics() {
        System.out.println("У вас нет созданных привычек для показа статистики!");
    }
}
