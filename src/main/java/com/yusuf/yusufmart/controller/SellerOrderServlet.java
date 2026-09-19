package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.OrderStatus;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.OrderService;
import com.yusuf.yusufmart.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controller handling seller incoming order tracking and status workflow management.
 * Fulfills Section 1 F6 & O2.
 */
@WebServlet({"/seller/orders", "/seller/orders/status"})
public class SellerOrderServlet extends HttpServlet {

    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null || (!user.isSeller() && !user.isAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller access required.");
            return;
        }

        List<Order> incomingOrders = orderService.getIncomingOrdersForSeller(user.getId());
        request.setAttribute("orders", incomingOrders);

        request.getRequestDispatcher("/WEB-INF/jsp/seller/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = getCurrentUser(request);
        if (user == null || (!user.isSeller() && !user.isAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller access required.");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        String statusParam = request.getParameter("status");

        if (orderIdParam != null && statusParam != null) {
            try {
                int orderId = Integer.parseInt(orderIdParam);
                OrderStatus newStatus = OrderStatus.fromString(statusParam);
                orderService.updateOrderStatus(orderId, newStatus);
            } catch (Exception ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/seller/orders?updated=true");
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
