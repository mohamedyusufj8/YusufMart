package com.yusuf.yusufmart.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Customer product reviews and star ratings.
 */
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int productId;
    private int userId;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    // Joined fields
    private String userName;
    private String productName;

    public Review() {
    }

    public Review(int id, int productId, int userId, int rating, String comment, Timestamp createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Review(int productId, int userId, int rating, String comment) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
