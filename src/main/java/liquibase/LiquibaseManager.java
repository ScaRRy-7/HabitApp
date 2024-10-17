package liquibase;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseManager {

    private static final String CHANGELOG_FILE = "db/changelog/changelog.xml";
    private static final String URL = ConfigurationManager.getProperty("DB_URL");
    private static final String USER = ConfigurationManager.getProperty("DB_USER");
    private static final String PASSWORD = ConfigurationManager.getProperty("DB_PASSWORD");

    public static void runMigrations() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration complete.");
        } catch (SQLException | LiquibaseException e) {
            System.out.println("SQLException in migration: " + e.getMessage());
        }
    }
}
