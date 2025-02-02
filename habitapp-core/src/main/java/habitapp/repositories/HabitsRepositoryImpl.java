package habitapp.repositories;

import habitapp.sql.SQLQueries;
import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dbconnection.ConnectionManager;
import habitapp.entities.Habit;
import habitapp.enums.HabitFrequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с привычками.
 * Предоставляет методы для создания, редактирования, удаления и получения привычек.
 */
@Repository
@Slf4j
@Loggable
public class HabitsRepositoryImpl implements HabitsRepository {

    private final ConnectionManager connectionManager;

    public HabitsRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Создает новую привычку для указанного пользователя.
     *
     * @param habit  Привычка, которую нужно создать.
     * @param userId Идентификатор пользователя, которому принадлежит привычка.
     */
    public void createHabit(Habit habit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO habitschema.habits (name, description, frequency, user_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                preparedStatement.executeUpdate();
                log.info("Привычка успешно добавлена");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Редактирует существующую привычку.
     *
     * @param oldHabit Старая привычка.
     * @param newHabit Новая привычка с обновленными данными.
     */
    public void editHabit(Habit oldHabit, Habit newHabit) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "UPDATE habitschema.habits SET name = ?, description = ?, frequency = ? " +
                    "WHERE name = ? AND description = ? AND frequency = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newHabit.getName());
                preparedStatement.setString(2, newHabit.getDescription());
                preparedStatement.setString(3, newHabit.getFrequency().getName());

                preparedStatement.setString(4, oldHabit.getName());
                preparedStatement.setString(5, oldHabit.getDescription());
                preparedStatement.setString(6, oldHabit.getFrequency().getName());
                preparedStatement.executeUpdate();
                log.info("Привычка успешно отредактирована");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Получает все привычки пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список привычек пользователя.
     */
    public List<Habit> getAllHabits(int userId) {
        List<Habit> userHabits = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_ALL_HABITS;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Habit habit = new Habit(
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                HabitFrequency.getFrequencyByName(resultSet.getString("frequency")),
                                resultSet.getTimestamp("created_date_time").toLocalDateTime(),
                                resultSet.getBoolean("is_completed")
                        );
                        userHabits.add(habit);
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return userHabits;
    }

    /**
     * Проверяет, существует ли привычка у пользователя.
     *
     * @param habit  Привычка, которую нужно проверить.
     * @param userId Идентификатор пользователя.
     * @return true, если привычка существует, иначе false.
     */
    public boolean hasHabit(Habit habit, int userId) {
        boolean hasHabit = false;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_HABIT;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasHabit = true;
                        log.info("Привычка успешно найдена");
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return hasHabit;
    }

    /**
     * Удаляет привычку у пользователя.
     *
     * @param habit  Привычка, которую нужно удалить.
     * @param userId Идентификатор пользователя.
     */
    public void deleteHabit(Habit habit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.DELETE_HABIT;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                preparedStatement.executeUpdate();
                log.info("Привычка успешно удалена");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Редактирует существующую привычку у пользователя.
     *
     * @param oldHabit Старая привычка.
     * @param newHabit Новая привычка с обновленными данными.
     * @param userId   Идентификатор пользователя.
     */
    public void editHabit(Habit oldHabit, Habit newHabit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.UPDATE_HABIT;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newHabit.getName());
                preparedStatement.setString(2, newHabit.getDescription());
                preparedStatement.setString(3, newHabit.getFrequency().getName());

                preparedStatement.setString(4, oldHabit.getName());
                preparedStatement.setString(5, oldHabit.getDescription());
                preparedStatement.setString(6, oldHabit.getFrequency().getName());
                preparedStatement.setInt(7, userId);
                preparedStatement.executeUpdate();
                log.info("Привычка успешно отредактирована");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Получает идентификатор привычки у пользователя.
     *
     * @param habit  Привычка, для которой нужно получить идентификатор.
     * @param userId Идентификатор пользователя.
     * @return Идентификатор привычки.
     */
    public int getHabitId(Habit habit, int userId) {
        int habitId = -1;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_HABIT_ID;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        habitId = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return habitId;
    }

    /**
     * Помечает привычку как выполненную.
     *
     * @param habit  Привычка, которую нужно пометить.
     * @param userId Идентификатор пользователя.
     */
    public void markHabit(Habit habit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.MARK_HABIT;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Проверяет, помечена ли привычка как выполненная.
     *
     * @param habitId Идентификатор привычки.
     * @return true, если привычка помечена как выполненная, и наче false.
     */
    public boolean isMarked(int habitId) {
        boolean marked = false;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_IS_COMPLETED;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        marked = resultSet.getBoolean("is_completed");
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return marked;
    }

    /**
     * Снимает пометку о выполнении привычки.
     *
     * @param habitId Идентификатор привычки.
     */
    public void unmarkHabit(int habitId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.UNMARK_HABIT;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                preparedStatement.executeUpdate();
                log.info("Привычка успешно снята с пометки о выполнении с id {}", habitId);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}