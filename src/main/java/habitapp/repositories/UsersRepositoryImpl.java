package habitapp.repositories;

import habitapp.SQLQueries;
import habitapp.dbconnection.ConnectionManager;
import habitapp.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Репозиторий для управления пользователями в базе данных.
 * Предоставляет методы для добавления, обновления, удаления и получения пользователей.
 */
@Repository
@Slf4j
public class UsersRepositoryImpl implements UsersRepository {

    public UsersRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private ConnectionManager connectionManager;


    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param user Пользователь для добавления.
     */
    @Override
    public void addUser(User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.INSERT_USER;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.executeUpdate();
                log.info("Пользователь успешно добавлен");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param oldUser  Старый пользователь.
     * @param newUser  Новый пользователь с обновленными данными.
     */
    @Override
    public void redactUser (User oldUser, User newUser ) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.REDACT_USER;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUser .getName());
                preparedStatement.setString(2, newUser .getEmail());
                preparedStatement.setString(3, newUser .getPassword());
                preparedStatement.setString(4, oldUser .getEmail());
                preparedStatement.executeUpdate();
                log.info("Пользователь успешно обновлен");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param user Пользователь для удаления.
     */
    @Override
    public void deleteUser(User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.DELETE_USER;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.executeUpdate();
                log.info("Пользователь успешно удален");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Проверяет, существует ли пользователь с указанным адресом электронной почты.
     *
     * @param user Пользователь для проверки.
     * @return true, если пользователь существует, иначе false.
     */
    @Override
    public boolean hasUser (User user) {
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.HAS_USER;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * Получает пользователя по адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Объект User, если пользователь найден, иначе null.
     */
    @Override
    public User getUser (String email) {
        User user = null;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_USER;
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
            log.error(e.getMessage());
        }
        return user;
    }

    /**
     * Получает идентификатор пользователя по адресу электронной почты.
     *
     * @param user Пользователь для получения идентификатора.
     * @return Идентификатор пользователя, если он найден, иначе -1.
     */
    @Override
    public int getUserId(User user) {
        int userId = -1;
        try (Connection connection = connectionManager.getConnection()) {
            String sql = SQLQueries.SELECT_USER_ID;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getEmail());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return userId;
    }
}