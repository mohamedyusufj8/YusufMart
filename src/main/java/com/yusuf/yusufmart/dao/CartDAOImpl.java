package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.model.CartItem;
import com.yusuf.yusufmart.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of CartDAO using PreparedStatements.
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public void addItem(int userId, int productId, int quantity) throws SQLException {
        // Upsert cart item using H2 MERGE or SELECT then INSERT/UPDATE
        String checkSql = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            checkPs.setInt(2, productId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    int existingId = rs.getInt("id");
                    int existingQty = rs.getInt("quantity");
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, existingQty + quantity);
                        updatePs.setInt(2, existingId);
                        updatePs.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, userId);
                        insertPs.setInt(2, productId);
                        insertPs.setInt(3, quantity);
                        insertPs.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public boolean updateQuantity(int cartItemId, int userId, int quantity) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeItem(int cartItemId, int userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public void clearCart(int userId, Connection conn) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        boolean closeConn = false;
        Connection activeConn = conn;
        if (activeConn == null) {
            activeConn = DatabaseConfig.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = activeConn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } finally {
            if (closeConn && activeConn != null) {
                activeConn.close();
            }
        }
    }

    @Override
    public List<CartItem> findByUserId(int userId) throws SQLException {
        String sql = "SELECT c.id AS cart_id, c.user_id, c.product_id, c.quantity, " +
                     "p.id AS p_id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, " +
                     "u.name AS seller_name " +
                     "FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "WHERE c.user_id = ? " +
                     "ORDER BY c.id ASC";

        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("cart_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));

                    Product p = new Product();
                    p.setId(rs.getInt("p_id"));
                    p.setSellerId(rs.getInt("seller_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStockQty(rs.getInt("stock_qty"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setSellerName(rs.getString("seller_name"));

                    item.setProduct(p);
                    list.add(item);
                }
            }
        }
        return list;
    }

    @Override
    public int countItemsByUserId(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }
}
