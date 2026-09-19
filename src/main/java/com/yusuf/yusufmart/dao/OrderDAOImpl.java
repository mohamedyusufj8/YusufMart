package com.yusuf.yusufmart.dao;

import com.yusuf.yusufmart.config.DatabaseConfig;
import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.OrderItem;
import com.yusuf.yusufmart.model.OrderStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of OrderDAO.
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order createOrder(Order order, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (buyer_id, status, total_amount) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getBuyerId());
            ps.setString(2, order.getStatus().name());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                }
            }
            return order;
        }
    }

    @Override
    public OrderItem createOrderItem(OrderItem item, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
            return item;
        }
    }

    @Override
    public Optional<Order> findById(int id) throws SQLException {
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, " +
                     "u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "WHERE o.id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(loadItemsForOrder(order.getId(), conn));
                    return Optional.of(order);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(int buyerId) throws SQLException {
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, " +
                     "u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "WHERE o.buyer_id = ? " +
                     "ORDER BY o.id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(loadItemsForOrder(order.getId(), conn));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> findIncomingOrdersBySellerId(int sellerId) throws SQLException {
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, " +
                     "u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE p.seller_id = ? " +
                     "ORDER BY o.id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(loadItemsForSellerOrder(order.getId(), sellerId, conn));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> findAllOrders() throws SQLException {
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, " +
                     "u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "ORDER BY o.id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(loadItemsForOrder(order.getId(), conn));
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public boolean updateStatus(int orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean hasBuyerPurchasedProduct(int buyerId, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status = 'DELIVERED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }

    @Override
    public BigDecimal calculateTotalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0.00) FROM orders WHERE status != 'CANCELLED'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setBuyerId(rs.getInt("buyer_id"));
        o.setStatus(OrderStatus.fromString(rs.getString("status")));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setBuyerEmail(rs.getString("buyer_email"));
        return o;
    }

    private List<OrderItem> loadItemsForOrder(int orderId, Connection conn) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
                     "p.name AS product_name, p.image_url AS product_image, p.seller_id " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ?";

        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }
        return items;
    }

    private List<OrderItem> loadItemsForSellerOrder(int orderId, int sellerId, Connection conn) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
                     "p.name AS product_name, p.image_url AS product_image, p.seller_id " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ? AND p.seller_id = ?";

        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }
        return items;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setProductName(rs.getString("product_name"));
        item.setProductImage(rs.getString("product_image"));
        item.setSellerId(rs.getInt("seller_id"));
        return item;
    }
}
