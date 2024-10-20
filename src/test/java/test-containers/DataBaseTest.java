package db;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataBaseTest {
    private final Logger logger = LoggerFactory.getLogger(DataBaseTest.class);
    protected PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    void setUp() {
        postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("habitappdbtest")
                .withUsername("habitapp")
                .withPassword("habitapppassword");
        postgresContainer.start();
    }

    @AfterAll
    void tearDown() {
        postgresContainer.stop();
    }

    @Test
    public void testDatabaseConnection() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Assertions.assertNotNull(connection);
            Assertions.assertTrue(connection.isValid(5));
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void testQueryExecution() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Statement statement = connection.createStatement();

            // создаем таблицу
            statement.execute("CREATE TABLE IF NOT EXISTS test_table (id SERIAL PRIMARY KEY, name VARCHAR(255));");

            // вставляем данные
            statement.execute("INSERT INTO test_table (name) VALUES ('Test name');");

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM test_table")) {
                Assertions.assertTrue(resultSet.next());
                Assertions.assertEquals(1, resultSet.getInt("id"));
                Assertions.assertEquals("Test name", resultSet.getString("name"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
