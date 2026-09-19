package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.UserDAO;
import com.yusuf.yusufmart.exception.AuthenticationException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.util.PasswordUtil;
import com.yusuf.yusufmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation containing business validation and user management rules.
 */
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User register(String name, String email, String password, Role role) {
        // Validation at top of service method per spec Section 13 Rule 5
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Full name is required.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ValidationException("A valid email address is required.");
        }
        if (!ValidationUtil.isMinLength(password, 6)) {
            throw new ValidationException("Password must be at least 6 characters long.");
        }
        if (role == null || role == Role.ADMIN) {
            // Admin role cannot be self-registered (Section 1 F1)
            role = Role.BUYER;
        }

        try {
            if (userDAO.findByEmail(email).isPresent()) {
                throw new ValidationException("An account with this email already exists.");
            }

            String hashedPassword = PasswordUtil.hash(password);
            User user = new User(name.trim(), email.trim().toLowerCase(), hashedPassword, role);
            User created = userDAO.create(user);
            logger.info("New user registered successfully: id={}, email={}, role={}", created.getId(), created.getEmail(), created.getRole());
            return created;
        } catch (SQLException e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            throw new RuntimeException("Database error during registration.", e);
        }
    }

    @Override
    public User authenticate(String email, String password) {
        if (!ValidationUtil.isValidEmail(email) || !ValidationUtil.isNotEmpty(password)) {
            throw new AuthenticationException("Invalid email or password format.");
        }

        try {
            Optional<User> optUser = userDAO.findByEmail(email.trim().toLowerCase());
            if (optUser.isEmpty()) {
                throw new AuthenticationException("Invalid email or password.");
            }

            User user = optUser.get();
            if (!PasswordUtil.check(password, user.getPasswordHash())) {
                throw new AuthenticationException("Invalid email or password.");
            }

            logger.info("User logged in successfully: id={}, role={}", user.getId(), user.getRole());
            return user;
        } catch (SQLException e) {
            logger.error("Error authenticating user: {}", e.getMessage(), e);
            throw new RuntimeException("Database error during login.", e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            return userDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Error fetching user by id: {}", e.getMessage(), e);
            throw new RuntimeException("Database error.", e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error fetching all users: {}", e.getMessage(), e);
            throw new RuntimeException("Database error.", e);
        }
    }

    @Override
    public boolean deleteUser(int id) {
        try {
            return userDAO.delete(id);
        } catch (SQLException e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
            throw new RuntimeException("Database error.", e);
        }
    }

    @Override
    public long getUserCount() {
        try {
            return userDAO.count();
        } catch (SQLException e) {
            logger.error("Error counting users: {}", e.getMessage(), e);
            return 0;
        }
    }
}
