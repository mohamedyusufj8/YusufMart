package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        userDAO = new UserDAOImpl();

        // Run schema.sql against the database
        try (InputStream is = UserDAOTest.class.getClassLoader().getResourceAsStream("schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("--") || trimmed.isEmpty()) continue;
                sql.append(line).append(" ");
                if (trimmed.endsWith(";")) {
                    stmt.execute(sql.toString().replace(";", "").trim());
                    sql.setLength(0);
                }
            }
        }
    }

    @Test
    public void testCreateAndFindUser() throws Exception {
        String testEmail = "testuser_" + System.currentTimeMillis() + "@test.com";
        User user = new User("Test User", testEmail, PasswordUtil.hash("Secret123"), Role.BUYER);

        User created = userDAO.create(user);
        assertTrue(created.getId() > 0, "Created user should receive auto-generated ID");

        Optional<User> foundByEmail = userDAO.findByEmail(testEmail);
        assertTrue(foundByEmail.isPresent(), "User should be findable by email");
        assertEquals("Test User", foundByEmail.get().getName());
        assertEquals(Role.BUYER, foundByEmail.get().getRole());

        Optional<User> foundById = userDAO.findById(created.getId());
        assertTrue(foundById.isPresent(), "User should be findable by ID");
        assertEquals(testEmail, foundById.get().getEmail());
    }

    @Test
    public void testFindByNonExistingEmail() throws Exception {
        Optional<User> nonExistent = userDAO.findByEmail("nobody_here_at_all@test.com");
        assertTrue(nonExistent.isEmpty(), "Non-existent email should return empty Optional");
    }
}
