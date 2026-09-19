package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.OrderItem;
import com.yusuf.yusufmart.model.OrderStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Orders and Order Items.
 */
public interface OrderDAO {
    Order createOrder(Order order, Connection conn) throws SQLException;
    OrderItem createOrderItem(OrderItem item, Connection conn) throws SQLException;
    Optional<Order> findById(int id) throws SQLException;
    List<Order> findByBuyerId(int buyerId) throws SQLException;
    List<Order> findIncomingOrdersBySellerId(int sellerId) throws SQLException;
    List<Order> findAllOrders() throws SQLException;
    boolean updateStatus(int orderId, OrderStatus status) throws SQLException;
    boolean hasBuyerPurchasedProduct(int buyerId, int productId) throws SQLException;
    long count() throws SQLException;
    BigDecimal calculateTotalRevenue() throws SQLException;
}
