package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Products.
 */
public interface ProductDAO {
    Product create(Product product) throws SQLException;
    Optional<Product> findById(int id) throws SQLException;
    List<Product> findAll(String category, String keyword, String sortBy) throws SQLException;
    List<Product> findBySellerId(int sellerId) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean delete(int id) throws SQLException;
    boolean deductStock(int productId, int quantity, Connection conn) throws SQLException;
    List<String> findDistinctCategories() throws SQLException;
    long count() throws SQLException;
}
