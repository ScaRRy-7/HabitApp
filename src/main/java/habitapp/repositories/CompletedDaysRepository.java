package habitapp.repositories;

import habitapp.entities.Habit;
import habitapp.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompletedDaysRepository {

    private static final CompletedDaysRepository COMPLETED_DAYS_REPOSITORY = new CompletedDaysRepository();

    public static CompletedDaysRepository getInstance() {
        return COMPLETED_DAYS_REPOSITORY;
    }

    private final Logger logger;
    private final UsersRepository usersRepository;

    private CompletedDaysRepository() {
        logger = LoggerFactory.getLogger(CompletedDaysRepository.class);
        usersRepository = UsersRepository.getInstance();
    }

    public List<LocalDateTime> getAllCompletedDays(User user, Habit habit) {
        List<LocalDateTime> completedDays = new ArrayList<>();

        try (Connection connection = getConnection()) {

            int userId = getUserIdFromDB(user.getEmail());

            // Получаем ID привычки
            String sqlGetHabitId = "SELECT id FROM habitschema.habits WHERE user_id = ? AND name = ? AND description = ? AND frequency = ?";
            PreparedStatement getHabitIdStatement = connection.prepareStatement(sqlGetHabitId);
            getHabitIdStatement.setInt(1, userId);
            getHabitIdStatement.setString(2, habit.getName());
            getHabitIdStatement.setString(3, habit.getDescription());
            getHabitIdStatement.setString(4, habit.getFrequenсy().getName());
            ResultSet habitIdResultSet = getHabitIdStatement.executeQuery();

            if (habitIdResultSet.next()) {
                int habitId = habitIdResultSet.getInt("id");

                // Получаем все даты выполнения привычки
                String sqlGetCompletedDays = "SELECT completed_date FROM habitschema.completed_days WHERE habit_id = ?";
                PreparedStatement getCompletedDaysStatement = connection.prepareStatement(sqlGetCompletedDays);
                getCompletedDaysStatement.setInt(1, habitId);
                ResultSet completedDaysResultSet = getCompletedDaysStatement.executeQuery();

                while (completedDaysResultSet.next()) {
                    Timestamp completedDate = completedDaysResultSet.getTimestamp("completed_date");
                    completedDays.add(completedDate.toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            logger.error("Ошибка при получении выполненных дней: {}", e.getMessage());
        }

        return completedDays;
    }

}
