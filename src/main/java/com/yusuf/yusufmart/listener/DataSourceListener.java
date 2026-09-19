package com.yusuf.yusufmart.listener;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.ServiceFactory;
import com.yusuf.yusufmart.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

/**
 * ServletContextListener managing the HikariCP connection pool lifecycle,
 * schema initialization, and demo data seeding.
 * Fulfills Section 4 Rule 5 and Section 12 Singleton requirement.
 */
@WebListener
public class DataSourceListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing YusufMart database and connection pool...");

        // 1. Initialize HikariCP pool
        DatabaseConfig.initializeDataSource();

        // 2. Execute schema.sql to ensure tables and indexes exist
        executeSqlScript("schema.sql");

        // 3. Ensure Default Users exist (Admin, Seller, Buyer)
        seedDefaultUsers();

        // 4. If catalog is empty, run seed.sql to populate demo products
        seedDemoCatalogIfEmpty();

        logger.info("YusufMart application initialized successfully!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down YusufMart connection pool...");
        DatabaseConfig.closeDataSource();
    }

    private void executeSqlScript(String scriptName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(scriptName)) {
            if (is == null) {
                logger.warn("SQL script {} not found in classpath.", scriptName);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                 Connection conn = DatabaseConfig.getConnection();
                 Statement stmt = conn.createStatement()) {

                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sql.append(line).append(" ");
                    if (trimmed.endsWith(";")) {
                        String statementStr = sql.toString().replace(";", "").trim();
                        if (!statementStr.isEmpty()) {
                            try {
                                stmt.execute(statementStr);
                            } catch (Exception sqlEx) {
                                logger.debug("Statement execution note: {}", sqlEx.getMessage());
                            }
                        }
                        sql.setLength(0);
                    }
                }
                logger.info("Executed SQL script {} successfully.", scriptName);
            }
        } catch (Exception e) {
            logger.error("Error executing {}: {}", scriptName, e.getMessage(), e);
        }
    }

    private void seedDefaultUsers() {
        try {
            var userDAO = ServiceFactory.getUserDAO();

            // Admin account
            if (userDAO.findByEmail("admin@yusufmart.com").isEmpty()) {
                User admin = new User(
                        "Admin Yusuf",
                        "admin@yusufmart.com",
                        PasswordUtil.hash("Admin@123"),
                        Role.ADMIN
                );
                userDAO.create(admin);
                logger.info("Seeded default admin: admin@yusufmart.com / Admin@123");
            }

            // Seller account
            if (userDAO.findByEmail("seller@yusufmart.com").isEmpty()) {
                User seller = new User(
                        "Sarah Seller",
                        "seller@yusufmart.com",
                        PasswordUtil.hash("Seller@123"),
                        Role.SELLER
                );
                userDAO.create(seller);
                logger.info("Seeded default seller: seller@yusufmart.com / Seller@123");
            }

            // Buyer account
            if (userDAO.findByEmail("buyer@yusufmart.com").isEmpty()) {
                User buyer = new User(
                        "Brian Buyer",
                        "buyer@yusufmart.com",
                        PasswordUtil.hash("Buyer@123"),
                        Role.BUYER
                );
                userDAO.create(buyer);
                logger.info("Seeded default buyer: buyer@yusufmart.com / Buyer@123");
            }
        } catch (Exception e) {
            logger.error("Error seeding default users: {}", e.getMessage(), e);
        }
    }

    private void seedDemoCatalogIfEmpty() {
        try {
            var productDAO = ServiceFactory.getProductDAO();
            if (productDAO.count() == 0) {
                logger.info("Catalog is empty. Running seed.sql...");
                executeSqlScript("seed.sql");
            }
        } catch (Exception e) {
            logger.error("Error checking product catalog count: {}", e.getMessage(), e);
        }
    }
}
