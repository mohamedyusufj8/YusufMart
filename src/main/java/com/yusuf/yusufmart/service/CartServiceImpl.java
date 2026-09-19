package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.CartDAO;
import com.yusuf.yusufmart.dao.ProductDAO;
import com.yusuf.yusufmart.exception.InsufficientStockException;
import com.yusuf.yusufmart.exception.ResourceNotFoundException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.CartItem;
import com.yusuf.yusufmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Cart management.
 */
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public void addToCart(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero.");
        }

        try {
            Optional<Product> optProduct = productDAO.findById(productId);
            if (optProduct.isEmpty()) {
                throw new ResourceNotFoundException("Product does not exist.");
            }

            Product product = optProduct.get();
            if (product.getStockQty() < quantity) {
                throw new InsufficientStockException("Only " + product.getStockQty() + " items available in stock.");
            }

            cartDAO.addItem(userId, productId, quantity);
            logger.info("User {} added product {} (qty: {}) to cart", userId, productId, quantity);
        } catch (SQLException e) {
            logger.error("Error adding to cart: {}", e.getMessage(), e);
            throw new RuntimeException("Database error adding item to cart.", e);
        }
    }

    @Override
    public boolean updateQuantity(int cartItemId, int userId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(cartItemId, userId);
        }

        try {
            return cartDAO.updateQuantity(cartItemId, userId, quantity);
        } catch (SQLException e) {
            logger.error("Error updating cart quantity: {}", e.getMessage(), e);
            throw new RuntimeException("Database error updating cart.", e);
        }
    }

    @Override
    public boolean removeFromCart(int cartItemId, int userId) {
        try {
            return cartDAO.removeItem(cartItemId, userId);
        } catch (SQLException e) {
            logger.error("Error removing cart item: {}", e.getMessage(), e);
            throw new RuntimeException("Database error removing cart item.", e);
        }
    }

    @Override
    public List<CartItem> getCartForUser(int userId) {
        try {
            return cartDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Error fetching cart for user: {}", e.getMessage(), e);
            throw new RuntimeException("Database error fetching cart.", e);
        }
    }

    @Override
    public int getCartCount(int userId) {
        try {
            return cartDAO.countItemsByUserId(userId);
        } catch (SQLException e) {
            logger.error("Error counting cart items: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public BigDecimal calculateTotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }
}
