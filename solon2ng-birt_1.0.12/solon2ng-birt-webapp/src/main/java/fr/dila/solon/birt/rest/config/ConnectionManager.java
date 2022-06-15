package fr.dila.solon.birt.rest.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class ConnectionManager {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);

    protected static final String BIRT_JDBCDRIVER_KEY = "solon.birt.jdbcDriver";
    protected static final String BIRT_JDBCPASSWORD_KEY = "solon.birt.jdbcPassword";
    protected static final String BIRT_JDBCURL_KEY = "solon.birt.jdbcUrl";
    protected static final String BIRT_JDBCUSER_KEY = "solon.birt.jdbcUser";

    private final Properties properties;
    private Connection connection;

    public ConnectionManager(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        if (!connectionIsValid() && containsJDBCKeys()) {
            initConnection();
        }
        return this.connection;
    }

    private void initConnection() {
        try {
            Class.forName(properties.getProperty(BIRT_JDBCDRIVER_KEY));
            this.connection = DriverManager.getConnection(properties.getProperty(BIRT_JDBCURL_KEY),
                    properties.getProperty(BIRT_JDBCUSER_KEY), properties.getProperty(BIRT_JDBCPASSWORD_KEY));
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Error: JDBC connection initialization failed", e);
            this.connection = null;
        }
    }

    private boolean connectionIsValid() {
        if (this.connection == null) {
            return false;
        }
        try {
            return this.connection.isValid(0);
        } catch (SQLException e) {
            LOGGER.error("Error: JDBC connection is not valid", e);
            return false;
        }
    }

    private boolean containsJDBCKeys() {
        return properties.containsKey(BIRT_JDBCDRIVER_KEY)
                && properties.containsKey(BIRT_JDBCPASSWORD_KEY)
                && properties.containsKey(BIRT_JDBCURL_KEY)
                && properties.containsKey(BIRT_JDBCUSER_KEY);
    }

}
