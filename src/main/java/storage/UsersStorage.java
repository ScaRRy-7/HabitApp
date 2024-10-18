package storage;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import liquibase.ConfigurationManager;
import liquibase.sql.Sql;
import org.slf4j.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс UsersStorage отвечает за хранение и управление данными пользователей.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class UsersStorage {
    /**
     * Статический экземпляр класса UsersStorage, реализующий паттерн Singleton.
     */
    private static final UsersStorage usersStorage = new UsersStorage();

    /**
     * Возвращает статический экземпляр класса UsersStorage.
     *
     * @return статический экземпляр класса UsersStorage
     */
    public static UsersStorage getInstance() {
        return usersStorage;
    }

    /**
     * Карта пользователей, где ключом является email пользователя.
     */
    //private Map<String, User> users = new HashMap<>();

    /**
     * Объект класса Logger для логирования событий.
     */
    private Logger logger = LoggerFactory.getLogger(UsersStorage.class);

    /**
     * Приватный конструктор для реализации паттерна Singleton.
     */
    private UsersStorage() {

    }

    /**
     * Добавляет пользователя в базу данных.
     *
     * @param user новый пользователь
     */
    public void addUser(User user) {

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            if (hasUser(user.getEmail())) {
                String sqlDeleteOldUser = "DELETE FROM habitschema.users WHERE email = ?";
                PreparedStatement statement = connection.prepareStatement(sqlDeleteOldUser);
                statement.setString(1, user.getEmail());
                statement.executeUpdate();
                logger.info("Пользователь с email '{}' был удален.", user.getEmail());
            }

            String sqlInsertUser = "INSERT INTO habitschema.users (username, email, password) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sqlInsertUser);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            logger.info("Добавлен новый пользователь: {}", user.getName());

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Возвращает коллекцию всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetUsers = "SELECT * FROM habitschema.users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetUsers);
            while (resultSet.next()) {
                String name = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(name, email, password);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return users;
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя
     * @return true, если пользователь существует, иначе false
     */
    public boolean hasUser(String email) {
        /* deprecated
        return users.containsKey(email);

         */
        boolean userExists = false;
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {
        String sqlHasUser = "SELECT COUNT(*) FROM habitschema.users WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(sqlHasUser);
        statement.setString(1, email);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int count = resultSet.getInt(1);
            userExists = count > 0;
        }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return userExists;
    }

    /**
     * Возвращает пользователя по его email.
     *
     * @param email email пользователя
     * @return пользователь, найденный по email
     */
    public User getUser(String email) {
        User user = null;

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetUser = "SELECT * FROM habitschema.users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sqlGetUser);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nameDB = resultSet.getString("username");
                String emailDB = resultSet.getString("email");
                String passwordDB = resultSet.getString("password");
                user = new User(nameDB, emailDB, passwordDB);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        if (user != null) {
            logger.info("возвращен пользователь с почтой {}", email);
        } else {
            logger.error("Пользователь с почтой {} не найден", email);
        }
        return user;
    }

    /**
     * Удаляет пользователя из базы данных по его email.
     *
     * @param email email пользователя
     */
    public void removeUser(String email) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlDeleteUser = "DELETE FROM habitschema.users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sqlDeleteUser);
            statement.setString(1, email);
            statement.executeUpdate();
            logger.info("Пользователь с почтой {} удален", email);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Добавляет новую привычку пользователю в базу данных.
     *
     * @param user  пользователь
     * @param habit новая привычка
     */
    public void addHabitToUser(User user, Habit habit) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlInsertHabit = "INSERT INTO habitschema.habits (name, description, frequency, created_date_time, user_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sqlInsertHabit);
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency().getName());
            statement.setTimestamp(4, Timestamp.valueOf(habit.getCreatedDateTime()));
            statement.setInt(5, getUserIdFromDB(user));
            statement.executeUpdate();
            logger.info("Пользователю {} добавлена привычка {}", user.getName(), habit.getName());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void removeHabitFromUser(User user, int habitNumber) {

    }

    public void changeHabit(User user, Habit habit, int habitNumber) {
        String sql = "SELECT habits.id FROM habitschema.habits WHERE user_id = ? ORDER BY habits.id LIMIT 1 OFFSET ?";

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, getUserIdFromDB(user));
            statement.setInt(2, habitNumber-1);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int habitId = resultSet.getInt("habits.id");

                String SqlChangeHabit = "UPDATE habitschema.habits SET name = ?, description = ?, frequency = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(SqlChangeHabit);
                updateStatement.setString(1, habit.getName());
                updateStatement.setString(2, habit.getDescription());
                updateStatement.setString(3, habit.getFrequency().toString());
                updateStatement.setInt(4, habitId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public Habit getHabitFromUser(User user, int habitNumber) {
        Habit habit = null;

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetHabitByNumber = "SELECT name, description, frequency, created_date_time, is_complited FROM habitschema.habits WHERE user_id = ? " +
                    "ORDER BY id LIMIT 1 OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(sqlGetHabitByNumber);
            statement.setInt(1, getUserIdFromDB(user));
            statement.setInt(2, habitNumber-1);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String habitName = resultSet.getString("name");
                String habitDescription = resultSet.getString("description");
                HabitFrequency habitFrequency = HabitFrequency.valueOf(resultSet.getString("frequency"));
                Timestamp created_date_time = resultSet.getTimestamp("created_date_time");
                boolean isComplited = resultSet.getBoolean("is_complited");
                habit = new Habit(habitName, habitDescription, habitFrequency, created_date_time.toLocalDateTime(), isComplited);
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
        List<Habit> habits = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetHabits = "SELECT habits.name AS habit_name, habits.description AS habit_description, habits.frequency as habit_frequency, habits.created_date_time AS habit_created_date_time, habits.is_complited AS habit_is_complited FROM habitschema.habits WHERE user_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sqlGetHabits);
            statement.setInt(1, getUserIdFromDB(user));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("habit_name");
                String description = resultSet.getString("habit_description");
                HabitFrequency frequency = HabitFrequency.getFrequencyByName(resultSet.getString("habit_frequency"));
                Timestamp created_date_time = resultSet.getTimestamp("habit_created_date_time");
                boolean isComplited = resultSet.getBoolean("habit_is_complited");
                Habit habit = new Habit(name, description, frequency, created_date_time.toLocalDateTime(), isComplited);
                habits.add(habit);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return habits;
    }

    public int getUserIdFromDB(User user) {
        int id = 0;
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetUserId = "SELECT id FROM habitschema.users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sqlGetUserId);
            statement.setString(1, user.getEmail());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return id;
    }
}
