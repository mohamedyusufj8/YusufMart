package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ReviewDAO using PreparedStatements.
 */
public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public Review create(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, review.getProductId());
            ps.setInt(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getInt(1));
                }
            }
            return review;
        }
    }

    @Override
    public List<Review> findByProductId(int productId) throws SQLException {
        String sql = "SELECT r.id, r.product_id, r.user_id, r.rating, r.comment, r.created_at, " +
                     "u.name AS user_name, p.name AS product_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN products p ON r.product_id = p.id " +
                     "WHERE r.product_id = ? " +
                     "ORDER BY r.created_at DESC";

        List<Review> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getInt("id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductName(rs.getString("product_name"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    @Override
    public List<Review> findByUserId(int userId) throws SQLException {
        String sql = "SELECT r.id, r.product_id, r.user_id, r.rating, r.comment, r.created_at, " +
                     "u.name AS user_name, p.name AS product_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN products p ON r.product_id = p.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.created_at DESC";

        List<Review> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getInt("id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductName(rs.getString("product_name"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    @Override
    public boolean hasUserReviewedProduct(int userId, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }
}
