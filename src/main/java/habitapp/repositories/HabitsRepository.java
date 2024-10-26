package habitapp.repositories;

import habitapp.dbconnection.ConnectionManager;
import habitapp.entities.Habit;
import habitapp.enums.HabitFrequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitsRepository {

    private static final HabitsRepository habitsRepository = new HabitsRepository();
    public static HabitsRepository getInstance() {
        return habitsRepository;
    }
    private HabitsRepository() {
        connectionManager = ConnectionManager.getInstance();
        logger = LoggerFactory.getLogger(HabitsRepository.class);
    }

    private ConnectionManager connectionManager;
    private final Logger logger;

    public void createHabit(Habit habit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO habitschema.habits (name, description, frequency, user_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                preparedStatement.executeUpdate();
                logger.info("Habit added successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void redactHabit(Habit oldHabit, Habit newHabit) {
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
                logger.info("Habit redacted successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public List<Habit> getAllHabits(int userId) {
        List<Habit> userHabits = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT * FROM habitschema.habits WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
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
            logger.error(e.getMessage());
        }
        return userHabits;
    }

    public boolean hasHabit(Habit habit, int userId) {
        boolean hasHabit = false;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT * FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasHabit = true;
                        logger.info("Habit has been found successfully");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return hasHabit;
    }

    public void deleteHabit(Habit habit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "DELETE FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, habit.getName());
                preparedStatement.setString(2, habit.getDescription());
                preparedStatement.setString(3, habit.getFrequency().getName());
                preparedStatement.setInt(4, userId);
                preparedStatement.executeUpdate();
                logger.info("Habit deleted successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void redactHabit(Habit oldHabit, Habit newHabit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "UPDATE habitschema.habits SET name = ?, description = ?, frequency = ? WHERE" +
                    " name = ? AND description = ? AND frequency = ? AND user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newHabit.getName());
                preparedStatement.setString(2, newHabit.getDescription());
                preparedStatement.setString(3, newHabit.getFrequency().getName());

                preparedStatement.setString(4, oldHabit.getName());
                preparedStatement.setString(5, oldHabit.getDescription());
                preparedStatement.setString(6, oldHabit.getFrequency().getName());
                preparedStatement.setInt(7, userId);
                preparedStatement.executeUpdate();
                logger.info("Habit redacted successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public int getHabitId(Habit habit, int userId) {
        int habitId = -1;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT id FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
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
            logger.error(e.getMessage());
        }
        return habitId;
    }

    public void markHabit(Habit habit, int userId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "UPDATE habitschema.habits SET is_completed = true WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public boolean isMarked(int habitId) {
        boolean marked = false;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT is_completed FROM habitschema.habits WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        marked =  resultSet.getBoolean("is_completed");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return marked;
    }

    public void unmarkHabit(int habitId) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "UPDATE habitschema.habits SET is_completed = false WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, habitId);
                preparedStatement.executeUpdate();
                logger.info("Habit unmarked successfully with id {}", habitId);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}