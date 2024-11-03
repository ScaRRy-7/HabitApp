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

/**
 * Репозиторий для управления пользователями в базе данных.
 * Предоставляет методы для добавления, обновления, удаления и получения пользователей.
 */
public class UsersRepository {

    private static final UsersRepository usersRepository = new UsersRepository();

    /**
     * Получает экземпляр репозитория пользователей.
     *
     * @return Экземпляр UsersRepository.
     */
    public static UsersRepository getInstance() {
        return usersRepository;
    }

    private UsersRepository() {
        connectionManager = ConnectionManager.getInstance();
        logger = LoggerFactory.getLogger(UsersRepository.class);
    }

    private ConnectionManager connectionManager;
    private final Logger logger;

    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param user Пользователь для добавления.
     */
    public void addUser (User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO habitschema.users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.executeUpdate();
                logger.info("Пользователь успешно добавлен");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param oldUser  Старый пользователь.
     * @param newUser  Новый пользователь с обновленными данными.
     */
    public void redactUser (User oldUser , User newUser ) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "UPDATE habitschema.users SET username = ?, email = ?, password = ? WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUser .getName());
                preparedStatement.setString(2, newUser .getEmail());
                preparedStatement.setString(3, newUser .getPassword());
                preparedStatement.setString(4, oldUser .getEmail());
                preparedStatement.executeUpdate();
                logger.info("Пользователь успешно обновлен");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param user Пользователь для удаления.
     */
    public void deleteUser (User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "DELETE FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.executeUpdate();
                logger.info("Пользователь успешно удален");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Проверяет, существует ли пользователь с указанным адресом электронной почты.
     *
     * @param user Пользователь для проверки.
     * @return true, если пользователь существует, иначе false.
     */
    public boolean hasUser (User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT * FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    /**
     * Получает пользователя по адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Объект User, если пользователь найден, иначе null.
     */
    public User getUser (String email) {
        User user = null;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = "SELECT * FROM habitschema.users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User(resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("password"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return user;
    }

    /**
     * Получает идентификатор пользователя по адресу электронной почты.
     *
     * @param user Пользователь для получения идентификатора.
     * @return Идентификатор пользователя, если он найден, иначе -1.
     */
    public int getUserId (User user) {
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