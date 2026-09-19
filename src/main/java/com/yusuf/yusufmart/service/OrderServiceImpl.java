package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.dao.CartDAO;
import com.yusuf.yusufmart.dao.OrderDAO;
import com.yusuf.yusufmart.dao.ProductDAO;
import com.yusuf.yusufmart.exception.InsufficientStockException;
import com.yusuf.yusufmart.exception.ResourceNotFoundException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.CartItem;
import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.OrderItem;
import com.yusuf.yusufmart.model.OrderStatus;
import com.yusuf.yusufmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Orders with atomic transactional checkout.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Order checkout(int buyerId) {
        Connection conn = null;
        try {
            List<CartItem> cartItems = cartDAO.findByUserId(buyerId);
            if (cartItems.isEmpty()) {
                throw new ValidationException("Cannot place order: your cart is empty.");
            }

            // Calculate total amount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItem item : cartItems) {
                totalAmount = totalAmount.add(item.getSubtotal());
            }

            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Validate & Deduct stock atomically
            for (CartItem item : cartItems) {
                Optional<Product> optProduct = productDAO.findById(item.getProductId());
                if (optProduct.isEmpty()) {
                    throw new ResourceNotFoundException("Product " + item.getProductId() + " not found.");
                }
                Product p = optProduct.get();
                if (p.getStockQty() < item.getQuantity()) {
                    throw new InsufficientStockException("Insufficient stock for: " + p.getName() + 
                            " (Available: " + p.getStockQty() + ", Requested: " + item.getQuantity() + ")");
                }

                boolean deducted = productDAO.deductStock(item.getProductId(), item.getQuantity(), conn);
                if (!deducted) {
                    throw new InsufficientStockException("Could not deduct stock for: " + p.getName());
                }
            }

            // 2. Create Order
            Order order = new Order();
            order.setBuyerId(buyerId);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setTotalAmount(totalAmount);
            Order createdOrder = orderDAO.createOrder(order, conn);

            // 3. Create OrderItems
            for (CartItem item : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(createdOrder.getId());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(item.getProduct().getPrice());
                orderDAO.createOrderItem(orderItem, conn);
            }

            // 4. Clear user's cart
            cartDAO.clearCart(buyerId, conn);

            conn.commit(); // Commit transaction
            logger.info("Order #{} placed successfully for buyer id={}", createdOrder.getId(), buyerId);

            return createdOrder;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.warn("Transaction rolled back for checkout: {}", e.getMessage());
                } catch (SQLException rollbackEx) {
                    logger.error("Rollback failed: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Checkout failed.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    logger.error("Failed to close connection: {}", closeEx.getMessage());
                }
            }
        }
    }

    @Override
    public Optional<Order> getOrderById(int orderId) {
        try {
            return orderDAO.findById(orderId);
        } catch (SQLException e) {
            logger.error("Error fetching order by id {}: {}", orderId, e.getMessage(), e);
            throw new RuntimeException("Database error fetching order.", e);
        }
    }

    @Override
    public List<Order> getOrdersForBuyer(int buyerId) {
        try {
            return orderDAO.findByBuyerId(buyerId);
        } catch (SQLException e) {
            logger.error("Error fetching orders for buyer {}: {}", buyerId, e.getMessage(), e);
            throw new RuntimeException("Database error fetching buyer orders.", e);
        }
    }

    @Override
    public List<Order> getIncomingOrdersForSeller(int sellerId) {
        try {
            return orderDAO.findIncomingOrdersBySellerId(sellerId);
        } catch (SQLException e) {
            logger.error("Error fetching incoming orders for seller {}: {}", sellerId, e.getMessage(), e);
            throw new RuntimeException("Database error fetching seller orders.", e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        try {
            return orderDAO.findAllOrders();
        } catch (SQLException e) {
            logger.error("Error fetching all orders: {}", e.getMessage(), e);
            throw new RuntimeException("Database error fetching all orders.", e);
        }
    }

    @Override
    public boolean updateOrderStatus(int orderId, OrderStatus newStatus) {
        try {
            return orderDAO.updateStatus(orderId, newStatus);
        } catch (SQLException e) {
            logger.error("Error updating order {} status to {}: {}", orderId, newStatus, e.getMessage(), e);
            throw new RuntimeException("Database error updating order status.", e);
        }
    }

    @Override
    public long getOrderCount() {
        try {
            return orderDAO.count();
        } catch (SQLException e) {
            logger.error("Error counting orders: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public BigDecimal getTotalRevenue() {
        try {
            return orderDAO.calculateTotalRevenue();
        } catch (SQLException e) {
            logger.error("Error calculating total revenue: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }
}
