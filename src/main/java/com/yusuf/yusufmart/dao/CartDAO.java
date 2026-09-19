package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.model.CartItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Cart items.
 */
public interface CartDAO {
    void addItem(int userId, int productId, int quantity) throws SQLException;
    boolean updateQuantity(int cartItemId, int userId, int quantity) throws SQLException;
    boolean removeItem(int cartItemId, int userId) throws SQLException;
    void clearCart(int userId, Connection conn) throws SQLException;
    List<CartItem> findByUserId(int userId) throws SQLException;
    int countItemsByUserId(int userId) throws SQLException;
}
