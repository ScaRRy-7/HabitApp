package services.habitchangers;

import services.entities.User;
import services.in.Reader;
import services.out.HabitsRedactorWriter;
import repositories.UsersRepository;
import services.validate.CommandHabitValidator;
import services.wait.Waiter;
import org.slf4j.*;

/**
 * Класс HabitRemover отвечает за удаление привычек пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitRemover {
    /**
     * Объект класса HabitsRedactorWriter для вывода информации на экран.
     */
    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();

    /**
     * Объект класса Waiter для ожидания секунды.
     */
    private final Waiter waiter = new Waiter();

    /**
     * Объект класса CommandHabitValidator для проверки валидности номера привычки.
     */
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();

    /**
     * Объект класса Reader для чтения ввода пользователя.
     */
    private final Reader reader = new Reader();

    /**
     * Объект класса UsersController для управления пользователями.
     */
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(HabitRemover.class);

    /**
     * Позволяет пользователю выбрать и удалить привычку.
     *
     * @param currentUser текущий пользователь
     */
    public void removeHabit(User currentUser) {
        logger.info("Запущен выбор привычки которую пользователь хочет удалить");
        if (usersRepository.getAllHabits(currentUser).isEmpty()) {
            logger.info("Выбор привычки невозможен, у пользователя нет привычек");
            writer.infoNoHabits();
            waiter.waitSecond();
        } else {
            writer.askNumberOfHabitRemove(currentUser);
            String habitNumberStr = reader.read();
            if (habitValidator.isValidHabitNumber(currentUser, habitNumberStr)) {
                logger.info("Пользователь ввел корректный номер привычки для удаления");
                int habitNumber = Integer.parseInt(habitNumberStr);
                usersRepository.removeHabit(currentUser, habitNumber);
                writer.infoHabitRemoved();
                waiter.waitSecond();
            } else {
                logger.debug("Пользователь ввел некорректный номер привычки для удаления");
                writer.reportInvalidHabitNumberRemove();
                waiter.waitSecond();
                removeHabit(currentUser);
            }
        }
    }
}
