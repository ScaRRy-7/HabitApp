package habitapp.repositories;

import habitapp.configuration.ConfigurationManager;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UsersRepository {

    private static final UsersRepository USERS_REPOSITORY = new UsersRepository();

    public static UsersRepository getInstance() {
        return USERS_REPOSITORY;
    }

    private UsersRepository() {}

    private Logger logger = LoggerFactory.getLogger(UsersRepository.class);



    public void addUser(User user) throws UserIllegalRequestException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        try (Connection connection = getConnection()) {

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

    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();

        try (Connection connection = getConnection()) {

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

    public boolean hasUser(String email) {
        boolean userExists = false;
        try (Connection connection = getConnection()) {
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

    public User getUser(String email) {
        User user = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = getConnection()) {

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

    public void removeUser(String email) {
        try (Connection connection = getConnection()) {

            String sqlDeleteUser = "DELETE FROM habitschema.users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sqlDeleteUser);
            statement.setString(1, email);
            statement.executeUpdate();
            logger.info("Пользователь с почтой {} удален", email);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public int getUserIdFromDB(String email) {
        int id = 0;
        try (Connection connection = getConnection()) {

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

    public void updateRedactedUser(String email, User user) {
        try (Connection connection = getConnection()) {

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


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"));
    }
}
