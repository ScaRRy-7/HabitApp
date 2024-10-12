package habitchangers;

import entities.Habit;
import entities.User;
import enums.Sorting;
import in.Reader;
import out.HabitsRedactorWriter;
import validate.HabitIndicatorValidator;
import wait.Waiter;
import org.slf4j.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HabitIndicator {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final Waiter waiter = new Waiter();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Reader reader = new Reader();
    private final HabitIndicatorValidator validator = new HabitIndicatorValidator();
    private final Logger logger = LoggerFactory.getLogger(HabitIndicator.class);

    public void showHabits(User currentUser) {
        habitUnmarker.checkHabits(currentUser); // ПРЕДВАРИТЕЛЬНО РАЗМАРКИРОВКА ПРИВЫЧЕК ЕСЛИ ПРОШЕЛ СРОК (ДЕНЬ ИЛИ МЕСЯЦ)
        if (currentUser.getHabits().isEmpty()) {
            logger.debug("У пользователя нет привычек");
            writer.infoNoHabits();
        } else {
            logger.debug("У ползьзователя есть привычки, запускается вывод привычек");
            writer.writeHabits(currentUser);
        }
        waiter.waitSecond();
    }

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

    public void chooseSorting(User currentUser) {
        logger.debug("Запущен выбор сортировки привычек");
        habitUnmarker.checkHabits(currentUser);

        if (currentUser.getHabits().isEmpty()) {
            logger.debug("У пользователя отсутствуют привычки, выбор сортировки невозможен");
            writer.infoNoHabits();
            waiter.waitSecond();
            return;
        }

        writer.askSorting();
        String commandString = reader.read();

        if (validator.isValidCommandString(commandString)) {
            logger.debug("Пользователь Выбрал валидную команду сортировки");
            Sorting sorting = getSortingByNum(Integer.parseInt(commandString));
            switch (sorting) {
                case DATE:
                    logger.info("Пользователь выбрал сортировку по дате");
                    showHabits(currentUser.getHabits(), Sorting.DATE);
                    break;
                case FREQUENCY:
                    logger.info("Пользователь выбрал сортировку по частоте");
                    showHabits(currentUser.getHabits(), Sorting.FREQUENCY);
                    break;
            }
        } else {
            logger.debug("Пользователь Выбрал не валидную команду сортировки, команда запрашивается снова");
            writer.reportInvalidSorting();
            waiter.waitSecond();
            chooseSorting(currentUser);
        }
    }

    private Sorting getSortingByNum(int num) {
        return switch (num) {
            case 1 -> Sorting.DATE;
            default -> Sorting.FREQUENCY;
        };
    }
}
