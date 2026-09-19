package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User entities.
 */
public interface UserDAO {
    User create(User user) throws SQLException;
    Optional<User> findById(int id) throws SQLException;
    Optional<User> findByEmail(String email) throws SQLException;
    List<User> findAll() throws SQLException;
    boolean update(User user) throws SQLException;
    boolean delete(int id) throws SQLException;
    long count() throws SQLException;
}