package services.habitchangers;

import services.entities.Habit;
import services.entities.User;
import services.enums.Sorting;
import services.in.Reader;
import services.out.HabitsRedactorWriter;
import repositories.UsersRepository;
import services.validate.HabitIndicatorValidator;
import services.wait.Waiter;
import org.slf4j.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс HabitIndicator отвечает за отображение и сортировку привычек пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitIndicator {

    private final UsersRepository usersRepository = new UsersRepository();
    /**
     * Объект класса HabitsRedactorWriter для записи информации о привычках.
     */
    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();

    /**
     * Объект класса Waiter для ожидания секунды.
     */
    private final Waiter waiter = new Waiter();

    /**
     * Объект класса HabitUnmarker для проверки и размаркировки привычек.
     */
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    /**
     * Объект класса Reader для чтения ввода пользователя.
     */
    private final Reader reader = new Reader();

    /**
     * Объект класса HabitIndicatorValidator для проверки валидности команд сортировки.
     */
    private final HabitIndicatorValidator validator = new HabitIndicatorValidator();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(HabitIndicator.class);

    /**
     * Отображает список привычек текущего пользователя.
     *
     * @param currentUser текущий пользователь
     */
    public void showHabits(User currentUser) {
        habitUnmarker.checkHabits(currentUser); // Предварительно размаркировка привычек, если прошел срок (день или месяц)
        if (usersRepository.getAllHabits(currentUser).isEmpty()) {
            logger.debug("У пользователя нет привычек");
            writer.infoNoHabits();
        } else {
            logger.debug("У пользователя есть привычки, запускается вывод привычек");
            writer.writeHabits(currentUser);
        }
        waiter.waitSecond();
    }

    /**
     * Отображает список привычек в отсортированном виде по дате или частоте выполнения.
     *
     * @param habits   список привычек
     * @param sorting  способ сортировки (дата или частота)
     */
    public void showHabits(List<Habit> habits, Sorting sorting) {
        logger.debug("Запускается вывод привычек в отсортированном виде по дате/частоте выполнения");
        List<Habit> copiedHabits = new ArrayList<>(habits);
        if (sorting == Sorting.DATE) {
            logger.debug("Выбрана сортировка по дате");
            copiedHabits.sort((habit1, habit2) -> habit1.getCreatedDateTime().compareTo(habit2.getCreatedDateTime()));
        } else {
            logger.debug("Выбрана сортировка по частоте");
            copiedHabits.sort((habit1, habit2) -> habit2.getFrequency().compareTo(habit1.getFrequency()));
        }
        writer.writeHabits(copiedHabits);
        logger.debug("Привычки выведены");
    }

    /**
     * Позволяет пользователю выбрать способ сортировки своих привычек.
     *
     * @param currentUser текущий пользователь
     */
    public void chooseSorting(User currentUser) {
        logger.debug("Запущен выбор сортировки привычек");
        habitUnmarker.checkHabits(currentUser);

        if (usersRepository.getAllHabits(currentUser).isEmpty()) {
            logger.debug("У пользователя отсутствуют привычки, выбор сортировки невозможен");
            writer.infoNoHabits();
            waiter.waitSecond();
            return;
        }

        writer.askSorting();
        String commandString = reader.read();

        if (validator.isValidCommandString(commandString)) {

            logger.debug("Пользователь выбрал валидную команду сортировки");
            Sorting sorting = getSortingByNum(Integer.parseInt(commandString));
            switch (sorting) {
                case DATE:
                    logger.info("Пользователь выбрал сортировку по дате");
                    showHabits(usersRepository.getAllHabits(currentUser), Sorting.DATE);
                    break;
                case FREQUENCY:
                    logger.info("Пользователь выбрал сортировку по частоте");
                    showHabits(usersRepository.getAllHabits(currentUser), Sorting.FREQUENCY);
                    break;
            }
        } else {
            logger.debug("Пользователь выбрал не валидную команду сортировки, команда запрашивается снова");
            writer.reportInvalidSorting();
            waiter.waitSecond();
            chooseSorting(currentUser);
        }
    }

    /**
     * Получает значение Sorting по номеру команды.
     *
     * @param num номер команды
     * @return значение Sorting
     */
    private Sorting getSortingByNum(int num) {
        return switch (num) {
            case 1 -> Sorting.DATE;
            default -> Sorting.FREQUENCY;
        };
    }
}
