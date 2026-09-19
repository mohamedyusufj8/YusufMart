package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.model.Review;

import java.util.List;

/**
 * Service interface for customer reviews and star ratings.
 */
public interface ReviewService {
    Review addReview(int userId, int productId, int rating, String comment);
    List<Review> getReviewsForProduct(int productId);
    boolean canUserReviewProduct(int userId, int productId);
}
