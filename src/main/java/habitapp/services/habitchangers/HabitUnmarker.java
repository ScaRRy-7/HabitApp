package services.habitchangers;

import services.entities.Habit;
import services.entities.User;
import repositories.UsersRepository;
import org.slf4j.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import static services.enums.HabitFrequency.DAILY;
import static services.enums.HabitFrequency.WEEKLY;

/**
 * Класс HabitUnmarker отвечает за размаркировку выполненных привычек пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitUnmarker {
    /**
     * Объект класса UsersController для обновления информации о пользователе.
     */
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(HabitUnmarker.class);


    /**
     * Проверяет и размаркирует выполненные привычки пользователя, если прошел необходимый срок.
     *
     * @param user текущий пользователь
     */
    public void checkHabits(User user) {
        logger.info("Запущена размаркировка выполненных привычек если уже прошло время");
        for (Habit habit : usersRepository.getAllHabits(user)) {
            if (!habit.isComplited()) continue;

            long daysPassed = ChronoUnit.DAYS.between(habit.getDaysHabitComplited().get(habit.getDaysHabitComplited().size() - 1), LocalDateTime.now());

            if ((daysPassed >= 1 && habit.getFrequency() == DAILY) || (daysPassed >= 7 && habit.getFrequency() == WEEKLY)) {
                habit.setUncomplited();
            }
        }
    }
}
