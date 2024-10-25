package habitapp.repositories;

import habitapp.configuration.ConfigurationManager;
import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.enums.HabitFrequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HabitsRepository {

    private static final HabitsRepository HABITS_REPOSITORY = new HabitsRepository();

    public static HabitsRepository getInstance() {
        return HABITS_REPOSITORY;
    }

    private HabitsRepository() {}

    private Logger logger = LoggerFactory.getLogger(HabitsRepository.class);



    public void addHabitToUser(User user, Habit habit) {
        try (Connection connection = getConnection()) {

            String sqlInsertHabit = "INSERT INTO habitschema.habits (name, description, frequency," +
                    " created_date_time, user_id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sqlInsertHabit);
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequenсy().getName());
            statement.setTimestamp(4, Timestamp.valueOf(habit.getCreatedDateTime()));
            statement.setInt(5, getUserIdFromDB(user.getEmail()));
            statement.executeUpdate();
            logger.info("Пользователю {} добавлена привычка {}", user.getName(), habit.getName());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void removeHabitFromUser(User user, Habit habit) {
        try (Connection connection = getConnection()) {

            String removeHabitFromUserSql = "DELETE FROM habitschema.habits WHERE name = ? AND description = ? AND" +
                    " frequency = ? AND user_id = ?";

            PreparedStatement statement = connection.prepareStatement(removeHabitFromUserSql);
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequenсy().getName());
            statement.setInt(4, getUserIdFromDB(user.getEmail()));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeHabit(User user, Habit oldHabit, Habit newHabit) {

        try (Connection connection = getConnection()) {

            String changeHabitSql = "SELECT habits.id FROM habitschema.habits WHERE user_id = ? AND " +
                    "habits.name = ? AND habits.description = ? AND habits.frequency = ? ORDER BY habits.id LIMIT 1";

            PreparedStatement statement = connection.prepareStatement(changeHabitSql);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));
            statement.setString(2, oldHabit.getName());
            statement.setString(3, oldHabit.getDescription());
            statement.setString(4, oldHabit.getFrequenсy().getName());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int habitId = resultSet.getInt("id");

                String SqlChangeHabit = "UPDATE habitschema.habits SET name = ?, description = ?, " +
                        "frequency = ? WHERE id = ?";

                PreparedStatement updateStatement = connection.prepareStatement(SqlChangeHabit);
                updateStatement.setString(1, newHabit.getName());
                updateStatement.setString(2, newHabit.getDescription());
                updateStatement.setString(3, newHabit.getFrequenсy().getName());
                updateStatement.setInt(4, habitId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public Habit getHabitFromUser(User user, int habitNumber) {
        unmarkHabits(user);
        Habit habit = null;

        try (Connection connection = getConnection()) {

            String sqlGetHabitByNumber = "SELECT name, description, frequency, created_date_time, is_completed FROM habitschema.habits WHERE user_id = ? " +
                    "ORDER BY id LIMIT 1 OFFSET ?";

            PreparedStatement statement = connection.prepareStatement(sqlGetHabitByNumber);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));
            statement.setInt(2, habitNumber-1);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String habitName = resultSet.getString("name");
                String habitDescription = resultSet.getString("description");
                HabitFrequency habitFrequency = HabitFrequency.getFrequencyByName(resultSet.getString("frequency"));
                Timestamp created_date_time = resultSet.getTimestamp("created_date_time");
                boolean iscompleted = resultSet.getBoolean("is_completed");
                habit = new Habit(habitName, habitDescription, habitFrequency, created_date_time.toLocalDateTime(), iscompleted);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        if (habit != null) {
            logger.info("Возвращена привычка");
        } else {
            logger.error("Привычка не найдена в БД");
        }
        return habit;
    }

    public List<Habit> getAllHabits(User user) {
        unmarkHabits(user);
        List<Habit> habits = new ArrayList<>();

        try (Connection connection = getConnection()) {

            String sqlGetHabits = "SELECT habits.name AS habit_name, habits.description AS habit_description, habits.frequency as habit_frequency, habits.created_date_time AS habit_created_date_time, habits.is_completed AS habit_is_completed FROM habitschema.habits WHERE user_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sqlGetHabits);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("habit_name");
                String description = resultSet.getString("habit_description");
                HabitFrequency frequency = HabitFrequency.getFrequencyByName(resultSet.getString("habit_frequency"));
                Timestamp created_date_time = resultSet.getTimestamp("habit_created_date_time");
                boolean iscompleted = resultSet.getBoolean("habit_is_completed");
                Habit habit = new Habit(name, description, frequency, created_date_time.toLocalDateTime(), iscompleted);
                habits.add(habit);
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return habits;
    }



    public boolean hasHabit(User user, Habit habit) {
        try (Connection connection = getConnection()) {

            String sqlGetHabit = "SELECT habits.name, habits.description, habits.frequency FROM habitschema.habits " +
                    "WHERE user_id = ? AND habits.name = ? AND habits.description = ? AND habits.frequency = ?;";

            PreparedStatement statement = connection.prepareStatement(sqlGetHabit);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getDescription());
            statement.setString(4, habit.getFrequenсy().getName());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return false;
    }



    public boolean habitIsMarked(User user, Habit habit) {
        unmarkHabits(user);
        try (Connection connection = getConnection()) {

            String sqlGetHabit = "SELECT habits.name, habits.description, habits.frequency, habits.is_completed FROM habitschema.habits " +
                    "WHERE user_id = ? AND habits.name = ? AND habits.description = ? AND habits.frequency = ?;";

            PreparedStatement statement = connection.prepareStatement(sqlGetHabit);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getDescription());
            statement.setString(4, habit.getFrequenсy().getName());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("is_completed");
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void markHabit(User user, Habit habit) {
        unmarkHabits(user);
        try (Connection connection = getConnection()) {

            String sqlChangeHabitSql = "SELECT habits.id FROM habitschema.habits WHERE habits.user_id = ? AND " +
                    "habits.name = ? AND habits.description = ? AND habits.frequency = ? ORDER BY habits.id LIMIT 1";

            PreparedStatement findHabitStatement = connection.prepareStatement(sqlChangeHabitSql);
            findHabitStatement.setInt(1, getUserIdFromDB(user.getEmail()));
            findHabitStatement.setString(2, habit.getName());
            findHabitStatement.setString(3, habit.getDescription());
            findHabitStatement.setString(4, habit.getFrequenсy().getName());

            ResultSet resultSet = findHabitStatement.executeQuery();
            if (resultSet.next()) {
                int habitId = resultSet.getInt("id");
                String sqlChangeHabit = "UPDATE habitschema.habits SET is_completed = true WHERE id = ?";
                PreparedStatement markHabitStatement = connection.prepareStatement(sqlChangeHabit);
                markHabitStatement.setInt(1, habitId);
                markHabitStatement.executeUpdate();

                String sqlAddcompletedDay = "INSERT INTO habitschema.completed_days (completed_date, habit_id) VALUES (?, ?)";
                PreparedStatement addcompletedDayStatement = connection.prepareStatement(sqlAddcompletedDay);
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                addcompletedDayStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().format(formatter)));
                addcompletedDayStatement.setInt(2, habitId);
                addcompletedDayStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void unmarkHabits(User user) {
        try (Connection connection = getConnection()) {

            // Получаем все привычки юзера
            String sqlGetAllHabits = "SELECT id, frequency FROM habitschema.habits WHERE user_id = ?";
            PreparedStatement getHabitsStatement = connection.prepareStatement(sqlGetAllHabits);
            getHabitsStatement.setInt(1, getUserIdFromDB(user.getEmail()));
            ResultSet habitsResultSet = getHabitsStatement.executeQuery();

            while (habitsResultSet.next()) {
                int habitId = habitsResultSet.getInt("id");
                String frequency = habitsResultSet.getString("frequency");

                // Получаем последнюю запись в completed_days для этой привычки
                String sqlGetLastCompletedDay = "SELECT completed_date FROM habitschema.completed_days WHERE habit_id = ? ORDER BY completed_date DESC LIMIT 1";
                PreparedStatement getLastCompletedDayStatement = connection.prepareStatement(sqlGetLastCompletedDay);
                getLastCompletedDayStatement.setInt(1, habitId);
                ResultSet lastCompletedDayResultSet = getLastCompletedDayStatement.executeQuery();

                if (lastCompletedDayResultSet.next()) {
                    Timestamp lastCompletedDate = lastCompletedDayResultSet.getTimestamp("completed_date");
                    LocalDateTime lastCompletedDateTime = lastCompletedDate.toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();

                    // Проверяем, сколько времени прошло с последней записи
                    long daysBetween = java.time.Duration.between(lastCompletedDateTime, now).toDays();

                    // Условие для обновления поля is_completed
                    boolean shouldUnmark = false;
                    if ("ежедневно".equals(frequency) && daysBetween > 1) {
                        shouldUnmark = true;
                    } else if ("еженедельно".equals(frequency) && daysBetween > 7) {
                        shouldUnmark = true;
                    }

                    // Если нужно пометить как не завершенное
                    if (shouldUnmark) {
                        String sqlUpdateHabit = "UPDATE habitschema.habits SET is_completed = false WHERE id = ?";
                        PreparedStatement updateHabitStatement = connection.prepareStatement(sqlUpdateHabit);
                        updateHabitStatement.setInt(1, habitId);
                        updateHabitStatement.executeUpdate();
                        logger.info("Привычка с ID {} помечена как не завершенная", habitId);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при unmarkHabits: {}", e.getMessage());
        }
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"));
    }
}
