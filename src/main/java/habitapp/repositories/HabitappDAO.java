package habitapp.repositories;

import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.configuration.ConfigurationManager;
import habitapp.services.enums.HabitFrequency;
import jakarta.servlet.http.HttpServletResponse;
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
public final class HabitappDAO {
    /**
     * Статический экземпляр класса UsersStorage, реализующий паттерн Singleton.
     */
    private static final HabitappDAO USERS_DAO = new HabitappDAO();

    /**
     * Возвращает статический экземпляр класса UsersStorage.
     *
     * @return статический экземпляр класса UsersStorage
     */
    public static HabitappDAO getInstance() {
        return USERS_DAO;
    }

    /**
     * Карта пользователей, где ключом является email пользователя.
     */
    //private Map<String, User> users = new HashMap<>();

    /**
     * Объект класса Logger для логирования событий.
     */
    private Logger logger = LoggerFactory.getLogger(HabitappDAO.class);

    /**
     * Приватный конструктор для реализации паттерна Singleton.
     */
    private HabitappDAO() {

    }

    /**
     * Добавляет пользователя в базу данных.
     *
     * @param user новый пользователь
     */
    public void addUser(User user) throws UserIllegalRequestException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
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
    public User getUser(String email) {
        User user = null;
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
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

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

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

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

    public List<Habit> getAllHabits(User user) {
        unmarkHabits(user);
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

    public boolean hasHabit(User user, Habit habit) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

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

    public void updateRedactedUser(String email, User user) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlRedactUser = "UPDATE habitschema.users SET username = ?, email = ?, password = ? WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sqlRedactUser);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, getUserIdFromDB(email));
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public boolean habitIsMarked(User user, Habit habit) {
        unmarkHabits(user);
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            String sqlGetHabit = "SELECT habits.name, habits.description, habits.frequency, habits.is_complited FROM habitschema.habits " +
                    "WHERE user_id = ? AND habits.name = ? AND habits.description = ? AND habits.frequency = ?;";

            PreparedStatement statement = connection.prepareStatement(sqlGetHabit);
            statement.setInt(1, getUserIdFromDB(user.getEmail()));
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getDescription());
            statement.setString(4, habit.getFrequenсy().getName());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("is_complited");
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void markHabit(User user, Habit habit) {
        unmarkHabits(user);
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

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
                String sqlChangeHabit = "UPDATE habitschema.habits SET is_complited = true WHERE id = ?";
                PreparedStatement markHabitStatement = connection.prepareStatement(sqlChangeHabit);
                markHabitStatement.setInt(1, habitId);
                markHabitStatement.executeUpdate();

                String sqlAddComplitedDay = "INSERT INTO habitschema.complited_days (complited_date, habit_id) VALUES (?, ?)";
                PreparedStatement addComplitedDayStatement = connection.prepareStatement(sqlAddComplitedDay);
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                addComplitedDayStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().format(formatter)));
                addComplitedDayStatement.setInt(2, habitId);
                addComplitedDayStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("полная пизда " + e);
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println("полная пизда " + e);
        }
    }

    public void unmarkHabits(User user) {
        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

            // Получаем все привычки юзера
            String sqlGetAllHabits = "SELECT id, frequency FROM habitschema.habits WHERE user_id = ?";
            PreparedStatement getHabitsStatement = connection.prepareStatement(sqlGetAllHabits);
            getHabitsStatement.setInt(1, getUserIdFromDB(user.getEmail()));
            ResultSet habitsResultSet = getHabitsStatement.executeQuery();

            while (habitsResultSet.next()) {
                int habitId = habitsResultSet.getInt("id");
                String frequency = habitsResultSet.getString("frequency");

                // Получаем последнюю запись в complited_days для этой привычки
                String sqlGetLastCompletedDay = "SELECT complited_date FROM habitschema.complited_days WHERE habit_id = ? ORDER BY complited_date DESC LIMIT 1";
                PreparedStatement getLastCompletedDayStatement = connection.prepareStatement(sqlGetLastCompletedDay);
                getLastCompletedDayStatement.setInt(1, habitId);
                ResultSet lastCompletedDayResultSet = getLastCompletedDayStatement.executeQuery();

                if (lastCompletedDayResultSet.next()) {
                    Timestamp lastCompletedDate = lastCompletedDayResultSet.getTimestamp("complited_date");
                    LocalDateTime lastCompletedDateTime = lastCompletedDate.toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();

                    // Проверяем, сколько времени прошло с последней записи
                    long daysBetween = java.time.Duration.between(lastCompletedDateTime, now).toDays();

                    // Условие для обновления поля is_complited
                    boolean shouldUnmark = false;
                    if ("ежедневно".equals(frequency) && daysBetween > 1) {
                        shouldUnmark = true;
                    } else if ("еженедельно".equals(frequency) && daysBetween > 7) {
                        shouldUnmark = true;
                    }

                    // Если нужно пометить как не завершенное
                    if (shouldUnmark) {
                        String sqlUpdateHabit = "UPDATE habitschema.habits SET is_complited = false WHERE id = ?";
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

    public List<LocalDateTime> getAllComplitedDays(User user, Habit habit) {
        List<LocalDateTime> completedDays = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"))) {

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
                    String sqlGetCompletedDays = "SELECT complited_date FROM habitschema.complited_days WHERE habit_id = ?";
                    PreparedStatement getCompletedDaysStatement = connection.prepareStatement(sqlGetCompletedDays);
                    getCompletedDaysStatement.setInt(1, habitId);
                    ResultSet completedDaysResultSet = getCompletedDaysStatement.executeQuery();

                    while (completedDaysResultSet.next()) {
                        Timestamp complitedDate = completedDaysResultSet.getTimestamp("complited_date");
                        completedDays.add(complitedDate.toLocalDateTime());
                    }
                }

        } catch (SQLException e) {
            logger.error("Ошибка при получении выполненных дней: {}", e.getMessage());
        }

        return completedDays;
    }
}
