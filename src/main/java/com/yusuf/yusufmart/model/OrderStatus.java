package com.yusuf.yusufmart.model;

/**
 * Order status lifecycle: Pending -> Confirmed -> Shipped -> Delivered (or Cancelled).
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static OrderStatus fromString(String statusStr) {
        if (statusStr == null) return PENDING;
        try {
            return OrderStatus.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}
