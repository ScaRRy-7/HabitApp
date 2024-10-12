package habitchangers;

import entities.Habit;
import entities.User;
import storage.UsersController;
import org.slf4j.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import static enums.HabitFrequency.DAILY;
import static enums.HabitFrequency.WEEKLY;

public class HabitUnmarker {

    private final UsersController usersController = new UsersController();
    private final Logger logger = LoggerFactory.getLogger(HabitUnmarker.class);

    public void checkHabits(User user) {
        logger.info("Запущена размаркировка выполненных привычек если уже прошло время");
        for (Habit habit : user.getHabits()) {
            if (!habit.isComplited()) continue;

            long daysPassed = ChronoUnit.DAYS.between(habit.getDaysHabitComplited().get(habit.getDaysHabitComplited().size()-1), LocalDateTime.now());

            if ( (daysPassed >= 1 && habit.getFrequency() == DAILY) || (daysPassed >= 7 && habit.getFrequency() == WEEKLY)) {
                habit.setUncomplited();
            }
        }
        usersController.updateRedactedUser(user, user.getEmail());
    }
}
