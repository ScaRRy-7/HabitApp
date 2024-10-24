package habitapp.repositories;

import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.configuration.ConfigurationManager;
import habitapp.services.enums.HabitFrequency;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.*;

import java.sql.*;
import java.util.*;

/**
 * Класс UsersStorage отвечает за хранение и управление данными пользователей.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class UsersDAO {
    /**
     * Статический экземпляр класса UsersStorage, реализующий паттерн Singleton.
     */
    private static final UsersDAO USERS_DAO = new UsersDAO();

    /**
     * Возвращает статический экземпляр класса UsersStorage.
     *
     * @return статический экземпляр класса UsersStorage
     */
    public static UsersDAO getInstance() {
        return USERS_DAO;
    }

    /**
     * Карта пользователей, где ключом является email пользователя.
     */
    //private Map<String, User> users = new HashMap<>();

    /**
     * Объект класса Logger для логирования событий.
     */
    private Logger logger = LoggerFactory.getLogger(UsersDAO.class);

    /**
     * Приватный конструктор для реализации паттерна Singleton.
     */
    private UsersDAO() {

    }

    /**
     * Добавляет пользователя в базу данных.
     *
     * @param userDTO новый пользователь
     */
    public void addUser(UserDTO userDTO) throws UserIllegalRequestException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            if (hasUser(userDTO.getEmail())) {
                String sqlDeleteOldUser = "DELETE FROM habitschema.users WHERE email = ?";
                PreparedStatement statement = connection.prepareStatement(sqlDeleteOldUser);
                statement.setString(1, userDTO.getEmail());
                statement.executeUpdate();
                logger.info("Пользователь с email '{}' был удален.", userDTO.getEmail());
            }

            String sqlInsertUser = "INSERT INTO habitschema.users (username, email, password) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sqlInsertUser);
            statement.setString(1, userDTO.getName());
            statement.setString(2, userDTO.getEmail());
            statement.setString(3, userDTO.getPassword());
            statement.executeUpdate();
            logger.info("Добавлен новый пользователь: {}", userDTO.getName());

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UserIllegalRequestException(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
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
    public UserDTO getUser(String email) {
        UserDTO userDTO = new UserDTO();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

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
                userDTO.setName(nameDB);
                userDTO.setEmail(emailDB);
                userDTO.setPassword(passwordDB);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        if (userDTO.getName() != null) {
            logger.info("возвращен пользователь с почтой {}", email);
        } else {
            logger.error("Пользователь с почтой {} не найден", email);
        }
        return userDTO;
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
    public void addHabitToUser(UserDTO user, Habit habit) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlInsertHabit = "INSERT INTO habitschema.habits (name, description, frequency, created_date_time, user_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sqlInsertHabit);
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency().getName());
            statement.setTimestamp(4, Timestamp.valueOf(habit.getCreatedDateTime()));
            statement.setInt(5, getUserIdFromDB(user.getEmail()));
            statement.executeUpdate();
            logger.info("Пользователю {} добавлена привычка {}", user.getName(), habit.getName());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void removeHabitFromUser(UserDTO userDTO, HabitDTO habitDTO) {
        String sql = "DELETE FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ?";

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, habitDTO.getName());
            statement.setString(2, habitDTO.getDescription());
            statement.setString(3, habitDTO.getFrequenсy().getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeHabit(UserDTO user, HabitDTO oldHabit, HabitDTO newHabit) {
        String sql = "SELECT habits.id FROM habitschema.habits WHERE user_id = ? AND habits.name = ? AND habits.description = ? AND habits.frequency = ? ORDER BY habits.id LIMIT 1";

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));
            statement.setString(2, oldHabit.getName());
            statement.setString(3, oldHabit.getDescription());
            statement.setString(4, oldHabit.getFrequenсy().getName());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int habitId = resultSet.getInt("id");

                String SqlChangeHabit = "UPDATE habitschema.habits SET name = ?, description = ?, frequency = ? WHERE id = ?";
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

    public Habit getHabitFromUser(UserDTO user, int habitNumber) {
        Habit habit = null;

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetHabitByNumber = "SELECT name, description, frequency, created_date_time, is_complited FROM habitschema.habits WHERE user_id = ? " +
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

    public List<Habit> getAllHabits(UserDTO user) {
        List<Habit> habits = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetHabits = "SELECT habits.name AS habit_name, habits.description AS habit_description, habits.frequency as habit_frequency, habits.created_date_time AS habit_created_date_time, habits.is_complited AS habit_is_complited FROM habitschema.habits WHERE user_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sqlGetHabits);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("habit_name");
                String description = resultSet.getString("habit_description");
                HabitFrequency frequency = HabitFrequency.getFrequencyByName(resultSet.getString("habit_frequency"));
                Timestamp created_date_time = resultSet.getTimestamp("habit_created_date_time");
                boolean isComplited = resultSet.getBoolean("habit_is_complited");
                Habit habit = new Habit(name, description, frequency, created_date_time.toLocalDateTime(), isComplited);
                HabitDTO habitDTO = new HabitDTO();
                habits.add(habit);
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return habits;
    }

    public int getUserIdFromDB(String email) {
        int id = 0;
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetUserId = "SELECT id FROM habitschema.users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sqlGetUserId);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return id;
    }

    public boolean hasHabit(UserDTO userDTO, HabitDTO habitDTO) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetHabit = "SELECT habits.name, habits.description, habits.frequency FROM habitschema.habits WHERE user_id = ? AND habits.name = ? AND habits.description = ? AND habits.frequency = ?;";
            PreparedStatement statement = connection.prepareStatement(sqlGetHabit);
            statement.setInt(1, getUserIdFromDB(userDTO.getEmail()));
            statement.setString(2, habitDTO.getName());
            statement.setString(3, habitDTO.getDescription());
            statement.setString(4, habitDTO.getFrequenсy().getName());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void updateRedactedUser(String email, UserDTO userDTO) {
        int userId = getUserIdFromDB(email);
        String sqlRedactUser = "UPDATE habitschema.users SET username = ?, email = ?, password = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {
            PreparedStatement statement = connection.prepareStatement(sqlRedactUser);
            statement.setString(1, userDTO.getName());
            statement.setString(2, userDTO.getEmail());
            statement.setString(3, userDTO.getPassword());
            statement.setInt(4, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
