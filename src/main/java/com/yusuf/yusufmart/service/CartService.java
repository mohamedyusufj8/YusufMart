package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Shopping Cart operations.
 */
public interface CartService {
    void addToCart(int userId, int productId, int quantity);
    boolean updateQuantity(int cartItemId, int userId, int quantity);
    boolean removeFromCart(int cartItemId, int userId);
    List<CartItem> getCartForUser(int userId);
    int getCartCount(int userId);
    BigDecimal calculateTotal(List<CartItem> items);
}
