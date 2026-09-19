package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.exception.InsufficientStockException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.CartItem;
import com.yusuf.yusufmart.model.Order;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.CartService;
import com.yusuf.yusufmart.service.OrderService;
import com.yusuf.yusufmart.service.ServiceFactory;
import com.yusuf.yusufmart.service.payment.MockCardPaymentStrategy;
import com.yusuf.yusufmart.service.payment.MockUpiPaymentStrategy;
import com.yusuf.yusufmart.service.payment.PaymentStrategy;

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
 * Controller handling checkout and mock payment authorization.
 * Fulfills Section 1 F5 & Scope constraint (mock payment confirmation).
 */
@WebServlet({"/checkout", "/checkout/process"})
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = ServiceFactory.getCartService();
    private final OrderService orderService = ServiceFactory.getOrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        List<CartItem> cartItems = cartService.getCartForUser(user.getId());
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?error=Your+cart+is+empty");
            return;
        }

        BigDecimal total = cartService.calculateTotal(cartItems);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", total);

        request.getRequestDispatcher("/WEB-INF/jsp/checkout/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        String accountIdentifier = request.getParameter("accountIdentifier");

        // 1. Process payment strategy
        PaymentStrategy strategy;
        if ("upi".equalsIgnoreCase(paymentMethod)) {
            strategy = new MockUpiPaymentStrategy();
        } else {
            strategy = new MockCardPaymentStrategy();
        }

        List<CartItem> cartItems = cartService.getCartForUser(user.getId());
        BigDecimal total = cartService.calculateTotal(cartItems);

        PaymentStrategy.PaymentResult paymentResult = strategy.process(total, accountIdentifier != null ? accountIdentifier : "MOCK-ACCOUNT");
        if (!paymentResult.isSuccess()) {
            request.setAttribute("errorMessage", "Payment failed: " + paymentResult.getMessage());
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", total);
            request.getRequestDispatcher("/WEB-INF/jsp/checkout/checkout.jsp").forward(request, response);
            return;
        }

        // 2. Perform atomic order creation and inventory deduction
        try {
            Order order = orderService.checkout(user.getId());
            response.sendRedirect(request.getContextPath() + "/orders?placed=true&orderId=" + order.getId());
        } catch (InsufficientStockException | ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", total);
            request.getRequestDispatcher("/WEB-INF/jsp/checkout/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred while placing your order: " + e.getMessage());
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", total);
            request.getRequestDispatcher("/WEB-INF/jsp/checkout/checkout.jsp").forward(request, response);
        }
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
