package habitapp.repositories;

import habitapp.configuration.ConfigurationManager;
import habitapp.dbconnection.ConnectionManager;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UsersRepository {

    private static final UsersRepository usersRepository = new UsersRepository();
    public static UsersRepository getInstance() {
        return usersRepository;
    }
    private UsersRepository() {
        connectionManager = ConnectionManager.getInstance();
        logger = LoggerFactory.getLogger(UsersRepository.class);
    }

    private ConnectionManager connectionManager;
    private final Logger logger;

    public void addUser(User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO habitschema.users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.executeUpdate();
                logger.info("User added successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void redactUser(User oldUser, User newUser) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "UPDATE habitschema.users SET username = ?, email = ?, password = ? WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUser.getName());
                preparedStatement.setString(2, newUser.getEmail());
                preparedStatement.setString(3, newUser.getPassword());

                preparedStatement.setString(4, oldUser.getEmail());
                preparedStatement.executeUpdate();
                logger.info("User updated successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void deleteUser(User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "DELETE FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.executeUpdate();
                logger.info("User deleted successfully");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public boolean hasUser(User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT * FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public User getUser(String email) {
        User user = null;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT * FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User(resultSet.getString("username"),
                                        resultSet.getString("email"),
                                        resultSet.getString("password") );
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return user;
    }

    public int getUserId(User user) {
        int userId = -1;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT id FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return userId;
    }
}
