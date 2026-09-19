package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.OrderDAO;
import com.yusuf.yusufmart.dao.ReviewDAO;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.Review;
import com.yusuf.yusufmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Service implementation for Review management and eligibility validation.
 */
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewDAO reviewDAO;
    private final OrderDAO orderDAO;

    public ReviewServiceImpl(ReviewDAO reviewDAO, OrderDAO orderDAO) {
        this.reviewDAO = reviewDAO;
        this.orderDAO = orderDAO;
    }

    @Override
    public Review addReview(int userId, int productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5 stars.");
        }
        if (!ValidationUtil.isNotEmpty(comment)) {
            throw new ValidationException("Review comment is required.");
        }

        try {
            // Check eligibility per Section 1 F8
            if (!orderDAO.hasBuyerPurchasedProduct(userId, productId)) {
                throw new ValidationException("You can only review products that have been delivered to you in a completed order.");
            }

            if (reviewDAO.hasUserReviewedProduct(userId, productId)) {
                throw new ValidationException("You have already reviewed this product.");
            }

            Review review = new Review(productId, userId, rating, comment.trim());
            return reviewDAO.create(review);
        } catch (SQLException e) {
            logger.error("Error creating review: {}", e.getMessage(), e);
            throw new RuntimeException("Database error saving review.", e);
        }
    }

    @Override
    public List<Review> getReviewsForProduct(int productId) {
        try {
            return reviewDAO.findByProductId(productId);
        } catch (SQLException e) {
            logger.error("Error fetching reviews for product {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Database error fetching reviews.", e);
        }
    }

    @Override
    public boolean canUserReviewProduct(int userId, int productId) {
        try {
            boolean hasPurchased = orderDAO.hasBuyerPurchasedProduct(userId, productId);
            if (!hasPurchased) return false;
            boolean alreadyReviewed = reviewDAO.hasUserReviewedProduct(userId, productId);
            return !alreadyReviewed;
        } catch (SQLException e) {
            logger.error("Error checking review eligibility: {}", e.getMessage(), e);
            return false;
        }
    }
}
