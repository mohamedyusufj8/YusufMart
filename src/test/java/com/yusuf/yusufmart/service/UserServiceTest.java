package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.UserDAO;
import com.yusuf.yusufmart.exception.AuthenticationException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        when(userDAO.findByEmail("newbuyer@example.com")).thenReturn(Optional.empty());
        when(userDAO.create(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(10);
            return u;
        });

        User user = userService.register("New Buyer", "newbuyer@example.com", "Password@123", Role.BUYER);
        assertNotNull(user);
        assertEquals(10, user.getId());
        assertEquals("newbuyer@example.com", user.getEmail());
        assertTrue(PasswordUtil.check("Password@123", user.getPasswordHash()));
        verify(userDAO).create(any(User.class));
    }

    @Test
    public void testRegistrationFailsOnDuplicateEmail() throws Exception {
        User existing = new User(1, "Existing", "exist@example.com", "hash", Role.BUYER, null);
        when(userDAO.findByEmail("exist@example.com")).thenReturn(Optional.of(existing));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            userService.register("Duplicate", "exist@example.com", "Password@123", Role.BUYER);
        });

        assertTrue(ex.getMessage().contains("already exists"));
        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    public void testAuthenticationSuccess() throws Exception {
        String hashed = PasswordUtil.hash("Secret123");
        User user = new User(5, "John", "john@example.com", hashed, Role.BUYER, null);
        when(userDAO.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User authenticated = userService.authenticate("john@example.com", "Secret123");
        assertNotNull(authenticated);
        assertEquals("John", authenticated.getName());
    }

    @Test
    public void testAuthenticationFailsOnWrongPassword() throws Exception {
        String hashed = PasswordUtil.hash("CorrectPassword");
        User user = new User(5, "John", "john@example.com", hashed, Role.BUYER, null);
        when(userDAO.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class, () -> {
            userService.authenticate("john@example.com", "WrongPassword");
        });
    }
}
