package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.Product;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.OrderService;
import com.yusuf.yusufmart.service.ProductService;
import com.yusuf.yusufmart.service.ServiceFactory;
import com.yusuf.yusufmart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller handling administrator dashboard, user moderation, order oversight, and listing moderation.
 * Fulfills Section 1 F7.
 */
@WebServlet({"/admin/dashboard", "/admin/users/delete", "/admin/products/delete"})
public class AdminServlet extends HttpServlet {

    private final UserService userService = ServiceFactory.getUserService();
    private final ProductService productService = ServiceFactory.getProductService();
    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required.");
            return;
        }

        List<User> users = userService.findAllUsers();
        List<Order> orders = orderService.getAllOrders();
        List<Product> products = productService.getProducts(null, null, "newest");

        long userCount = userService.getUserCount();
        long productCount = productService.getProductCount();
        long orderCount = orderService.getOrderCount();
        BigDecimal totalRevenue = orderService.getTotalRevenue();

        request.setAttribute("users", users);
        request.setAttribute("orders", orders);
        request.setAttribute("products", products);
        request.setAttribute("userCount", userCount);
        request.setAttribute("productCount", productCount);
        request.setAttribute("orderCount", orderCount);
        request.setAttribute("totalRevenue", totalRevenue);

        request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = getCurrentUser(request);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required.");
            return;
        }

        String path = request.getServletPath();

        if ("/admin/products/delete".equals(path)) {
            String prodIdStr = request.getParameter("productId");
            if (prodIdStr != null) {
                int prodId = Integer.parseInt(prodIdStr);
                productService.deleteProduct(prodId, user.getId(), true);
            }
        } else if ("/admin/users/delete".equals(path)) {
            String userIdStr = request.getParameter("userId");
            if (userIdStr != null) {
                int targetId = Integer.parseInt(userIdStr);
                if (targetId != user.getId()) { // Cannot delete own admin account
                    userService.deleteUser(targetId);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/dashboard?moderated=true");
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
