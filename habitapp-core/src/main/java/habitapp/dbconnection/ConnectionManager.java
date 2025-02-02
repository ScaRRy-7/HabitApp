package habitapp.dbconnection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключениями к базе данных.
 * Реализует паттерн Singleton для обеспечения единственного экземпляра менеджера подключения.
 */
@Component
@Slf4j
public class ConnectionManager {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Получает соединение с базой данных.
     *
     * @return объект Connection для взаимодействия с базой данных
     * @throws SQLException если возникает ошибка при подключении к базе данных
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driverClassName); // Загружаем драйвер PostgreSQL
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return DriverManager.getConnection(url, username, password);
    }
}