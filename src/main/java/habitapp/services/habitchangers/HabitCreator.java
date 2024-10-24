package services.habitchangers;

import services.entities.Habit;
import services.entities.User;
import services.enums.HabitFrequency;
import services.in.Reader;
import services.out.HabitsRedactorWriter;
import repositories.UsersRepository;
import services.validate.CommandHabitValidator;
import services.wait.Waiter;

/**
 * Отвечает за создание новой привычки пользователем.
 * Класс использует {@link HabitsRedactorWriter} для вывода запросов на ввод данных о новой привычке,
 * {@link CommandHabitValidator} для проверки корректности введенных данных,
 * {@link Reader} для чтения ввода пользователя,
 * {@link Waiter} для временной задержки,
 * и {@link UsersRepository} для добавления новой привычки в базу данных пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitCreator {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final Waiter waiter = new Waiter();
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Позволяет пользователю создать новую привычку.
     * Пользователю последовательно предлагается ввести название, описание и частоту новой привычки.
     * Введенные данные проверяются на корректность, и если они корректны, новая привычка создается и добавляется в базу данных пользователя.
     * Если пользователь вводит некорректные данные, ему предлагается ввести их еще раз.
     *
     * @param user пользователь, который создает новую привычку
     */
    public void createNewHabit(User user) {
        writer.askHabitName();
        String habitName = getHabitName();

        writer.askHabitDescription();
        String habitDescription = getHabitDescription();

        writer.askHabitFrequency();
        HabitFrequency frequency = getHabitFrequency();

        Habit newHabit = new Habit(habitName, habitDescription, frequency);
        writer.infoHabitWasCreated();
        waiter.waitSecond();

        usersRepository.addNewHabit(user, newHabit);
    }

    /**
     * Получает название новой привычки от пользователя и проверяет его на корректность.
     * Если название корректно, оно возвращается. Если название некорректно, пользователю предлагается ввести его еще раз.
     *
     * @return корректное название новой привычки
     */
    private String getHabitName() {
        String habitName = reader.read();
        if (habitValidator.isValidHabitName(habitName)) {
            return habitName;
        } else {
            writer.reportInvalidHabitName();
            waiter.waitSecond();
            getHabitName();
        }
        return habitName;
    }

    /**
     * Получает описание новой привычки от пользователя и проверяет его на корректность.
     * Если описание корректно, оно возвращается. Если описание некорректно, пользователю предлагается ввести его еще раз.
     *
     * @return корректное описание новой привычки
     */
    private String getHabitDescription() {
        String habitDescription = reader.read();
        if (habitValidator.isValidDescription(habitDescription)) {
            return habitDescription;
        } else {
            writer.reportInvalidHabitName();
            getHabitDescription();
        }
        return habitDescription;
    }

    /**
     * Получает частоту новой привычки от пользователя и проверяет ее на корректность.
     * Если частота корректна, она возвращается. Если частота некорректна, пользователю предлагается ввести ее еще раз.
     *
     * @return корректная частота новой привычки
     */
    private HabitFrequency getHabitFrequency() {
        HabitFrequency habitFrequency = null;
        String habitFrequencyString = reader.read();
        if (habitValidator.isValidFrequency(habitFrequencyString)) {
            habitFrequency = getHabitFrequencyByNumber(Integer.parseInt(habitFrequencyString));
            return habitFrequency;
        } else {
            writer.reportInvalidFrequency();
            habitFrequency = getHabitFrequency();
        }
        return habitFrequency;
    }

    /**
     * Преобразует число, введенное пользователем, в соответствующую частоту привычки.
     *
     * @param number число, введенное пользователем
     * @return соответствующая частота привычки
     * @throws IllegalArgumentException если введено некорректное число
     */
    private HabitFrequency getHabitFrequencyByNumber(int number) {
        return switch (number) {
            case 1 -> HabitFrequency.DAILY;
            case 2 -> HabitFrequency.WEEKLY;
            default -> throw new IllegalArgumentException("Invalid command number");
        };
    }
}
