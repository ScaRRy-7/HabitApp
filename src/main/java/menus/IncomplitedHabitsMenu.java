package menus;

import entities.Habit;
import entities.User;
import habitchangers.HabitMarker;
import habitchangers.HabitUnmarker;
import in.Reader;
import out.IncomplitedHabitsMenuWriter;
import storage.UsersController;
import validate.IncomplitedHabitsMenuValidator;
import wait.Waiter;
import org.slf4j.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Отвечает за предоставление пользователю возможности отметить выполненную, но неотмеченную ранее привычку.
 * Пользователю отображается список всех его неотмеченных привычек, и он может выбрать одну из них для отметки как выполненную.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class IncomplitedHabitsMenu {

    private final UsersController usersController = new UsersController();
    private final IncomplitedHabitsMenuWriter writer = new IncomplitedHabitsMenuWriter();
    private final IncomplitedHabitsMenuValidator validator = new IncomplitedHabitsMenuValidator();
    private final HabitMarker habitMarker = new HabitMarker();
    private final Waiter waiter = new Waiter();
    private final Reader reader = new Reader();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Logger logger = LoggerFactory.getLogger(IncomplitedHabitsMenu.class);

    /**
     * Запускает меню для отметки неотмеченных привычек пользователя.
     * Сначала проверяется, есть ли у пользователя неотмеченные привычки.
     * Если есть, выводится список этих привычек, и пользователь может выбрать одну из них для отметки.
     * Если нет, выводится соответствующее сообщение.
     *
     * @param user авторизованный пользователь
     */
    public void start(User user) {
        logger.info("Запущен отбор неотмеченных привычек из всех привычек у пользователя");
        habitUnmarker.checkHabits(user);
        List<Habit> incomplitedHabits = new ArrayList<>();
        for (Habit habit : user.getHabits()) {
            if (!habit.isComplited()) {
                incomplitedHabits.add(habit);
            }
        }

        if (incomplitedHabits.isEmpty()) {
            logger.info("Неотмеченные привычки отсутствуют");
            writer.infoNoIncomplitedHabits();
            waiter.waitSecond();
            return;
        } else {
            showIncomplitedHabits(user, incomplitedHabits);
        }
    }

    /**
     * Отображает список неотмеченных привычек пользователя и позволяет ему выбрать одну из них для отметки.
     * Если пользователь выбирает корректный номер привычки, выбранная привычка отмечается как выполненная.
     * Если пользователь выбирает некорректный номер привычки, ему предлагается ввести номер еще раз.
     *
     * @param user            авторизованный пользователь
     * @param incomplitedHabits список неотмеченных привычек пользователя
     */
    private void showIncomplitedHabits(User user, List<Habit> incomplitedHabits) {
        logger.info("Неотмеченные привычки есть, запускается их вывод");
        writer.writeIncomplitedHabits(incomplitedHabits);
        logger.info("Пользователь выбирает привычку для ее отметки");
        writer.askNumberForIncHabit();
        String numberOfIncomplitedHabitStr = reader.read();

        if (validator.isValidNumberOfIncHabit(numberOfIncomplitedHabitStr, incomplitedHabits.size())) {
            logger.info("Пользователь ввел корректный номер привычки");
            habitMarker.markHabit(user, Integer.parseInt(numberOfIncomplitedHabitStr), incomplitedHabits);
            usersController.updateRedactedUser(user, user.getEmail());
            writer.infoHabitMarked();
            waiter.waitSecond();
        } else {
            logger.debug("Пользователь ввел некорректный номер привычки, выбор запрашивается снова");
            writer.reportInvalidNumberOfIncHabit();
            waiter.waitSecond();
            showIncomplitedHabits(user, incomplitedHabits);
        }
    }
}
