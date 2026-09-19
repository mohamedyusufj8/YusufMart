package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User registration, authentication, and management.
 */
public interface UserService {
    User register(String name, String email, String password, Role role);
    User authenticate(String email, String password);
    Optional<User> findById(int id);
    List<User> findAllUsers();
    boolean deleteUser(int id);
    long getUserCount();
}
