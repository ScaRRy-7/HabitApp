package services.habitchangers;

import services.entities.Habit;
import services.entities.User;
import repositories.UsersRepository;
import org.slf4j.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс HabitMarker отвечает за маркировку невыполненных привычек пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitMarker {
    /**
     * Объект класса UsersController для обновления информации о пользователе.
     */
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Объект класса Logger для логирования событий.
     */
    private final Logger logger = LoggerFactory.getLogger(HabitMarker.class);

    /**
     * Позволяет пользователю отметить невыполненную привычку как выполненную.
     *
     * @param user               текущий пользователь
     * @param numberOfIncomplitedHabit индекс невыполненной привычки (1-based)
     * @param incomplitedHabits   список невыполненных привычек
     */
    public void markHabit(User user, int numberOfIncomplitedHabit, List<Habit> incomplitedHabits) {
        int numberOfIncomplitedHabitIndex = numberOfIncomplitedHabit - 1;
        Habit incomplitedHabit = incomplitedHabits.get(numberOfIncomplitedHabitIndex);
        incomplitedHabit.setComplited();
        incomplitedHabit.getDaysHabitComplited().add(LocalDateTime.now());
        logger.info("Привычка {} была отмечена пользователем {} как выполненная", incomplitedHabit.getName(), user.getName());
    }
}
