package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Order processing, atomic transactions, and lifecycle tracking.
 */
public interface OrderService {
    Order checkout(int buyerId);
    Optional<Order> getOrderById(int orderId);
    List<Order> getOrdersForBuyer(int buyerId);
    List<Order> getIncomingOrdersForSeller(int sellerId);
    List<Order> getAllOrders();
    boolean updateOrderStatus(int orderId, OrderStatus newStatus);
    long getOrderCount();
    BigDecimal getTotalRevenue();
}
