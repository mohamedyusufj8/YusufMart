package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.model.Order;
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
import java.util.Optional;

/**
 * Controller handling buyer order history and order details.
 * Fulfills Section 1 F6.
 */
@WebServlet({"/orders", "/order-detail"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/orders");
            return;
        }

        String path = request.getServletPath();

        if ("/order-detail".equals(path)) {
            handleOrderDetail(request, response, user);
        } else {
            handleOrderList(request, response, user);
        }
    }

    private void handleOrderList(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        List<Order> orders = orderService.getOrdersForBuyer(user.getId());
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/WEB-INF/jsp/orders/history.jsp").forward(request, response);
    }

    private void handleOrderDetail(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam);
            Optional<Order> optOrder = orderService.getOrderById(orderId);

            if (optOrder.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }

            Order order = optOrder.get();
            // Ensure buyer only views their own order (unless admin)
            if (order.getBuyerId() != user.getId() && !user.isAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied to this order.");
                return;
            }

            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/jsp/orders/detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
