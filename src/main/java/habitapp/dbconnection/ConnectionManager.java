package habitapp.dbconnection;

import habitapp.configuration.ConfigurationManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final ConnectionManager connectionManager = new ConnectionManager();
    private ConnectionManager() {}
    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {

        }
        return DriverManager.getConnection(ConfigurationManager.getProperty("DB_URL"),
                ConfigurationManager.getProperty("DB_USER"),
                ConfigurationManager.getProperty("DB_PASSWORD"));
    }
}
