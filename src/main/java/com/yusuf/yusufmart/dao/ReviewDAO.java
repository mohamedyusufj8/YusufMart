package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.model.Review;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for product reviews.
 */
public interface ReviewDAO {
    Review create(Review review) throws SQLException;
    List<Review> findByProductId(int productId) throws SQLException;
    List<Review> findByUserId(int userId) throws SQLException;
    boolean hasUserReviewedProduct(int userId, int productId) throws SQLException;
}
