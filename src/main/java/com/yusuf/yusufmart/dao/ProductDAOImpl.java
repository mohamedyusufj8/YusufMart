package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of ProductDAO.
 * Strictly adheres to PreparedStatement only policy and try-with-resources.
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product create(Product product) throws SQLException {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getImageUrl());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }
            return product;
        }
    }

    @Override
    public Optional<Product> findById(int id) throws SQLException {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, " +
                     "p.created_at, u.name AS seller_name, " +
                     "COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
                     "COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "WHERE p.id = ? " +
                     "GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowWithDetails(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll(String category, String keyword, String sortBy) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, " +
                "p.created_at, u.name AS seller_name, " +
                "COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
                "COUNT(r.id) AS review_count " +
                "FROM products p " +
                "JOIN users u ON p.seller_id = u.id " +
                "LEFT JOIN reviews r ON p.id = r.product_id " +
                "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All")) {
            sql.append("AND p.category = ? ");
            params.add(category.trim());
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append("GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name ");

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY avg_rating DESC, p.id DESC ");
        } else {
            sql.append("ORDER BY p.id DESC ");
        }

        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithDetails(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Product> findBySellerId(int sellerId) throws SQLException {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, " +
                     "p.created_at, u.name AS seller_name, " +
                     "COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
                     "COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "WHERE p.seller_id = ? " +
                     "GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name " +
                     "ORDER BY p.id DESC";

        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithDetails(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ? " +
                     "WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStockQty());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            ps.setInt(7, product.getId());
            ps.setInt(8, product.getSellerId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deductStock(int productId, int quantity, Connection conn) throws SQLException {
        String sql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> findDistinctCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM products WHERE category IS NOT NULL ORDER BY category ASC";
        List<String> categories = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }

    private Product mapRowWithDetails(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setSellerId(rs.getInt("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setSellerName(rs.getString("seller_name"));
        p.setAverageRating(Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0);
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
