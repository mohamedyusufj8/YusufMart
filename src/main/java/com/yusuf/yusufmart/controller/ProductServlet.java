package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.model.Product;
import com.yusuf.yusufmart.model.Review;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.ProductService;
import com.yusuf.yusufmart.service.ReviewService;
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
 * Controller handling public product catalog browsing, filtering, search, and details.
 * Fulfills Section 1 F3 & F8.
 */
@WebServlet({"/products", "/product-detail"})
public class ProductServlet extends HttpServlet {

    private final ProductService productService = ServiceFactory.getProductService();
    private final ReviewService reviewService = ServiceFactory.getReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/product-detail".equals(path)) {
            handleProductDetail(request, response);
        } else {
            handleProductList(request, response);
        }
    }

    private void handleProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String category = request.getParameter("category");
        String keyword = request.getParameter("q");
        String sortBy = request.getParameter("sort");

        List<Product> products = productService.getProducts(category, keyword, sortBy);
        List<String> categories = productService.getCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedCategory", category);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("/WEB-INF/jsp/products/list.jsp").forward(request, response);
    }

    private void handleProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idStr);
            Optional<Product> optProduct = productService.getProductById(productId);

            if (optProduct.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            Product product = optProduct.get();
            List<Review> reviews = reviewService.getReviewsForProduct(productId);

            // Check if current user is eligible to write a review
            boolean canReview = false;
            HttpSession session = request.getSession(false);
            if (session != null) {
                User currentUser = (User) session.getAttribute("currentUser");
                if (currentUser != null && currentUser.isBuyer()) {
                    canReview = reviewService.canUserReviewProduct(currentUser.getId(), productId);
                }
            }

            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.setAttribute("canReview", canReview);

            request.getRequestDispatcher("/WEB-INF/jsp/products/detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
