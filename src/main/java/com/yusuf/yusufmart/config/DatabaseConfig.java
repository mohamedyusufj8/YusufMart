package com.yusuf.yusufmart.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connection pooling using HikariCP.
 * Configuration can be loaded from config.properties or environment variables.
 */
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static volatile HikariDataSource dataSource;

    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }

    public static synchronized void initializeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        try {
            Properties props = new Properties();
            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input != null) {
                    props.load(input);
                }
            } catch (Exception e) {
                logger.warn("Could not load config.properties, using system defaults: {}", e.getMessage());
            }

            String jdbcUrl = System.getenv("JDBC_URL");
            if (jdbcUrl == null || jdbcUrl.isBlank()) {
                jdbcUrl = props.getProperty("db.url", "jdbc:h2:./data/yusufmart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
            }

            String user = System.getenv("JDBC_USER");
            if (user == null || user.isBlank()) {
                user = props.getProperty("db.user", "sa");
            }

            String pass = System.getenv("JDBC_PASSWORD");
            if (pass == null) {
                pass = props.getProperty("db.password", "");
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(user);
            config.setPassword(pass);
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(10000);
            config.setPoolName("YusufMartHikariPool");

            dataSource = new HikariDataSource(config);
            logger.info("HikariCP DataSource initialized successfully with URL: {}", jdbcUrl);
        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP DataSource: {}", e.getMessage(), e);
            throw new RuntimeException("DataSource initialization error", e);
        }
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static synchronized void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource closed.");
        }
    }
}