package habitapp.dbconnection;

import habitapp.configuration.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключениями к базе данных.
 * Реализует паттерн Singleton для обеспечения единственного экземпляра менеджера подключения.
 */
@Component
public class ConnectionManager {

    private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPassword;

    public ConnectionManager() {

    }

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
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}