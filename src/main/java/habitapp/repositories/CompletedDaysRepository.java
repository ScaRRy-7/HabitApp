package habitapp.repositories;

import habitapp.dbconnection.ConnectionManager;
import habitapp.entities.Habit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления выполненными днями привычек.
 * <p>
 * Этот класс предоставляет методы для работы с таблицей
 * `completed_days`, включая получение списка выполненных дней,
 * создание новой записи о выполненном дне и получение последнего
 * выполненного дня для заданной привычки.
 * </p>
 */
public class CompletedDaysRepository {


    /**
     * Конструктор для инициализации репозитория выполненных дней.
     */
    public CompletedDaysRepository(ConnectionManager connectionManager) {
        logger = LoggerFactory.getLogger(CompletedDaysRepository.class);
        this.connectionManager = connectionManager;
    }

    private final Logger logger;
    private final ConnectionManager connectionManager;

    /**
     * Получает список выполненных дней для заданной привычки.
     *
     * @param habitId Идентификатор привычки.
     * @return Список дат и времени выполнения привычки.
     */
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

    /**
     * Создает запись о выполненном дне для заданной привычки.
     *
     * @param habitId Идентификатор привычки.
     */
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

    /**
     * Получает дату последнего выполненного дня для заданной привычки.
     *
     * @param habitId Идентификатор привычки.
     * @return Дата и время последнего выполненного дня, или null, если не найдено.
     */
    public LocalDateTime getLastCompletedDay(int habitId) {
        LocalDateTime lastCompletedDay = null;

        try (Connection connection = connectionManager.getConnection()) {

            String sql = "SELECT MAX(completed_date) AS last_completed_date FROM habitschema.completed_days WHERE habit_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Timestamp timestamp = resultSet.getTimestamp("last_completed_date");
                        lastCompletedDay = timestamp != null ? timestamp.toLocalDateTime() : null;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return lastCompletedDay;
    }
}
