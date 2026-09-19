package com.yusuf.yusufmart.controller;

import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.User;
import com.yusuf.yusufmart.service.ReviewService;
import com.yusuf.yusufmart.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller handling customer product reviews and star ratings.
 * Fulfills Section 1 F8.
 */
@WebServlet("/reviews/add")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = ServiceFactory.getReviewService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String productIdStr = request.getParameter("productId");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        try {
            int productId = Integer.parseInt(productIdStr);
            int rating = Integer.parseInt(ratingStr);

            reviewService.addReview(user.getId(), productId, rating, comment);
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&reviewed=true");
        } catch (ValidationException e) {
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productIdStr + "&error=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productIdStr + "&error=Unable+to+submit+review");
        }
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
