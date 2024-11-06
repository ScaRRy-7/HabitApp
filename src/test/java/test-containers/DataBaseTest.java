import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.*;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataBaseTest {
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
    public void testDatabaseConnection() throws SQLException {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Assertions.assertNotNull(connection);
            Assertions.assertTrue(connection.isValid(1));
        }
    }

    @Test
    public void testQueryExecution() throws SQLException {
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
        }
    }

    @Test
    public void testInvalidConnection() {
        Assertions.assertThrows(SQLException.class, () -> {
            DriverManager.getConnection("jdbc:postgresql://invalid_host:5432/invalid_db", "invalid_user", "invalid_password");
        });
    }
}