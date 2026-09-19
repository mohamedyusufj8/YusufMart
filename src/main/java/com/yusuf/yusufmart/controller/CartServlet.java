package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.exception.InsufficientStockException;
import com.yusuf.yusufmart.model.CartItem;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.CartService;
import com.yusuf.yusufmart.service.ServiceFactory;

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
 * Controller handling shopping cart operations: view, add, update quantity, remove.
 * Fulfills Section 1 F4.
 */
@WebServlet({"/cart", "/cart/add", "/cart/update", "/cart/remove"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = ServiceFactory.getCartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        List<CartItem> cartItems = cartService.getCartForUser(user.getId());
        BigDecimal total = cartService.calculateTotal(cartItems);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", total);

        request.getRequestDispatcher("/WEB-INF/jsp/cart/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        String path = request.getServletPath();

        try {
            switch (path) {
                case "/cart/add":
                    handleAdd(request, response, user.getId());
                    break;
                case "/cart/update":
                    handleUpdate(request, response, user.getId());
                    break;
                case "/cart/remove":
                    handleRemove(request, response, user.getId());
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/cart");
            }
        } catch (InsufficientStockException e) {
            request.getSession().setAttribute("cartError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (Exception e) {
            request.getSession().setAttribute("cartError", "Action could not be completed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = 1;
        String qtyParam = request.getParameter("quantity");
        if (qtyParam != null && !qtyParam.isBlank()) {
            quantity = Math.max(1, Integer.parseInt(qtyParam));
        }

        cartService.addToCart(userId, productId, quantity);

        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isBlank()) {
            response.sendRedirect(request.getContextPath() + redirect + (redirect.contains("?") ? "&" : "?") + "added=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/cart?added=true");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        cartService.updateQuantity(cartItemId, userId, quantity);
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void handleRemove(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        cartService.removeFromCart(cartItemId, userId);
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
