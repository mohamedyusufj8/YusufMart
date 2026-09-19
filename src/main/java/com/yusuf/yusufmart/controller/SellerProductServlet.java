package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.Product;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.ProductService;
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
import java.util.Optional;

/**
 * Controller handling seller product listing CRUD operations.
 * Fulfills Section 1 F2.
 */
@WebServlet({"/seller/products", "/seller/products/save", "/seller/products/delete"})
public class SellerProductServlet extends HttpServlet {

    private final ProductService productService = ServiceFactory.getProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null || (!user.isSeller() && !user.isAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller access required.");
            return;
        }

        List<Product> sellerProducts = productService.getProductsBySeller(user.getId());
        List<String> categories = productService.getCategories();

        request.setAttribute("products", sellerProducts);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/jsp/seller/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null || (!user.isSeller() && !user.isAdmin())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller access required.");
            return;
        }

        String path = request.getServletPath();

        if ("/seller/products/delete".equals(path)) {
            handleDelete(request, response, user);
        } else if ("/seller/products/save".equals(path)) {
            handleSave(request, response, user);
        } else {
            response.sendRedirect(request.getContextPath() + "/seller/products");
        }
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceParam = request.getParameter("price");
        String stockParam = request.getParameter("stockQty");
        String category = request.getParameter("category");
        String imageUrl = request.getParameter("imageUrl");

        try {
            BigDecimal price = new BigDecimal(priceParam);
            int stock = Integer.parseInt(stockParam);

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStockQty(stock);
            product.setCategory(category);
            product.setImageUrl(imageUrl);
            product.setSellerId(user.getId());

            if (idParam != null && !idParam.isBlank()) {
                product.setId(Integer.parseInt(idParam));
                productService.updateProduct(product);
            } else {
                productService.createProduct(product);
            }

            response.sendRedirect(request.getContextPath() + "/seller/products?saved=true");
        } catch (NumberFormatException | ValidationException e) {
            request.setAttribute("errorMessage", "Error saving listing: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            int productId = Integer.parseInt(idParam);
            productService.deleteProduct(productId, user.getId(), user.isAdmin());
        }
        response.sendRedirect(request.getContextPath() + "/seller/products?deleted=true");
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
