package habitchangers;

import entities.User;
import in.Reader;
import out.HabitsRedactorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import wait.Waiter;
import org.slf4j.*;

public class HabitRemover {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final Waiter waiter = new Waiter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final UsersController usersController = new UsersController();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Logger logger = LoggerFactory.getLogger(HabitRemover.class);

    public void removeHabit(User currentUser) {
        logger.info("Запущен выбор привычки которую пользователь хочет удалить");
        if (currentUser.getHabits().isEmpty()) {
            logger.info("Выбор привычки невозможен, у пользователя нет привычек");
            writer.infoNoHabits();
            waiter.waitSecond();
        } else {
            writer.askNumberOfHabitRemove(currentUser);
            String habitNumberStr = reader.read();
            if (habitValidator.isValidHabitNumber(currentUser, habitNumberStr)) {
                logger.info("Пользователь ввел корректный номер привычки для удаления");
                int habitNumber = Integer.parseInt(habitNumberStr);
                usersController.removeHabit(currentUser, habitNumber);
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
