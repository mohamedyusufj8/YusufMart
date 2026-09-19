package com.yusuf.yusufmart.dto;

import com.yusuf.yusufmart.model.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Complex Order Summary DTO utilizing the Builder pattern.
 * Fulfills Section 12 requirement: Builder (complex DTO construction).
 */
public class OrderSummaryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int orderId;
    private final int buyerId;
    private final String buyerName;
    private final String buyerEmail;
    private final OrderStatus status;
    private final BigDecimal totalAmount;
    private final int itemCount;
    private final Timestamp createdAt;

    private OrderSummaryDTO(Builder builder) {
        this.orderId = builder.orderId;
        this.buyerId = builder.buyerId;
        this.buyerName = builder.buyerName;
        this.buyerEmail = builder.buyerEmail;
        this.status = builder.status;
        this.totalAmount = builder.totalAmount;
        this.itemCount = builder.itemCount;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private int orderId;
        private int buyerId;
        private String buyerName;
        private String buyerEmail;
        private OrderStatus status;
        private BigDecimal totalAmount;
        private int itemCount;
        private Timestamp createdAt;

        public Builder orderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder buyerId(int buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        public Builder buyerName(String buyerName) {
            this.buyerName = buyerName;
            return this;
        }

        public Builder buyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder itemCount(int itemCount) {
            this.itemCount = itemCount;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderSummaryDTO build() {
            return new OrderSummaryDTO(this);
        }
    }
}
