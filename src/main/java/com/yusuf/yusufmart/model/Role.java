package com.yusuf.yusufmart.model;

/**
 * User roles in YusufMart marketplace.
 */
public enum Role {
    BUYER,
    SELLER,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null) return BUYER;
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return BUYER;
        }
    }
}
