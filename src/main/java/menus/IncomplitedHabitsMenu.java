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

public class IncomplitedHabitsMenu {

    private final UsersController usersController = new UsersController();
    private final IncomplitedHabitsMenuWriter writer = new IncomplitedHabitsMenuWriter();
    private final IncomplitedHabitsMenuValidator validator = new IncomplitedHabitsMenuValidator();
    private final HabitMarker habitMarker = new HabitMarker();
    private final Waiter waiter = new Waiter();
    private final Reader reader = new Reader();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Logger logger = LoggerFactory.getLogger(IncomplitedHabitsMenu.class);

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
