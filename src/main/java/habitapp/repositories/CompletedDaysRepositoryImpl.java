package habitapp.repositories;

import habitapp.SQLQueries;
import habitapp.dbconnection.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
@Repository
@Slf4j
public class CompletedDaysRepositoryImpl implements CompletedDaysRepository {


    /**
     * Конструктор для инициализации репозитория выполненных дней.
     */
    public CompletedDaysRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private final ConnectionManager connectionManager;

    /**
     * Получает список выполненных дней для заданной привычки.
     *
     * @param habitId Идентификатор привычки.
     * @return Список дат и времени выполнения привычки.
     */
    @Override
    public List<LocalDateTime> getCompletedDays(int habitId) {
        List<LocalDateTime> completedDays = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_COMPLETED_DATES;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        completedDays.add(resultSet.getTimestamp("completed_date").toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return completedDays;
    }

    /**
     * Создает запись о выполненном дне для заданной привычки.
     *
     * @param habitId Идентификатор привычки.
     */
    @Override
    public void createCompletedDay(int habitId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.INSERT_COMPLETED_DAY;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                preparedStatement.executeUpdate();
                log.info("Completed day created for habit (id = {})", habitId);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Получает дату последнего выполненного дня для заданной привычки.
     *
     * @param habitId Идентификатор привычки.
     * @return Дата и время последнего выполненного дня, или null, если не найдено.
     */
    @Override
    public LocalDateTime getLastCompletedDay(int habitId) {
        String sql = SQLQueries.SELECT_LAST_COMPLETED_DAY;
        return executeQueryForLastCompletedDay(sql, habitId);
    }

    private LocalDateTime executeQueryForLastCompletedDay(String sql, int habitId) {
        try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = createPreparedStatement(connection, sql, habitId);
        ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return extractLastCompletedDay(resultSet);
            }
        } catch (SQLException e) {
            log.error("Error fetching last completed day for habit (id = {}): {}", habitId, e.getMessage());
        }
        return null;
    }

    private PreparedStatement createPreparedStatement(Connection connection, String sql, int habitId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, habitId);
        return preparedStatement;
    }

    private LocalDateTime extractLastCompletedDay(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp("last_completed_date");
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

}
