package habitapp.dbconnection;

import habitapp.configuration.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключениями к базе данных.
 * Реализует паттерн Singleton для обеспечения единственного экземпляра менеджера подключения.
 */
public class ConnectionManager {

    private static final ConnectionManager connectionManager = new ConnectionManager(); // Единственный экземпляр менеджера подключения

    private ConnectionManager() {} // Приватный конструктор для предотвращения создания других экземпляров

    /**
     * Получает единственный экземпляр ConnectionManager.
     *
     * @return экземпляр ConnectionManager
     */
    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    /**
     * Получает соединение с базой данных.
     *
     * @return объект Connection для взаимодействия с базой данных
     * @throws SQLException если возникает ошибка при подключении к базе данных
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // Загружаем драйвер PostgreSQL
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return DriverManager.getConnection(
                ConfigurationManager.getProperty("DB_URL"), // URL базы данных
                ConfigurationManager.getProperty("DB_USER"), // Имя пользователя базы данных
                ConfigurationManager.getProperty("DB_PASSWORD") // Пароль базы данных
        );
    }
}