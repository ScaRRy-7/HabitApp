package habitapp.repositories;

import habitapp.dbconnection.ConnectionManager;
import habitapp.entities.Habit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompletedDaysRepository {

    private static final CompletedDaysRepository completedDaysRepository = new CompletedDaysRepository();
    public static CompletedDaysRepository getInstance() {
        return completedDaysRepository;
    }
    private CompletedDaysRepository() {
        logger = LoggerFactory.getLogger(CompletedDaysRepository.class);
        connectionManager = ConnectionManager.getInstance();
    }

    private final Logger logger;
    private final ConnectionManager connectionManager;

    public List<LocalDateTime> getCompletedDays(int habitId) {
        List<LocalDateTime> completedDays = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT completed_date FROM habitschema.completed_days WHERE habit_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        completedDays.add(resultSet.getTimestamp("completed_date").toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return completedDays;
    }

    public void createCompletedDay(int habitId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO habitschema.completed_days (habit_id) VALUES(?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                preparedStatement.executeUpdate();
                logger.info("Completed day created for habit (id = {})", habitId);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public LocalDateTime getLastCompletedDay(int habitId) {
        LocalDateTime lastCompletedDay = null;

        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT MAX(completed_date) AS last_completed_date FROM habitschema.completed_days WHERE habit_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Timestamp timestamp = resultSet.getTimestamp("last_completed_date");
                    lastCompletedDay = timestamp.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return lastCompletedDay;
    }
}
