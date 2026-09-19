package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.*;

/**
 * Factory pattern implementation for instantiating DAOs and Services.
 * Fulfills Section 12 requirement: Factory (DAO/provider instantiation).
 */
public class ServiceFactory {

    private static final UserDAO userDAO = new UserDAOImpl();
    private static final ProductDAO productDAO = new ProductDAOImpl();
    private static final CartDAO cartDAO = new CartDAOImpl();
    private static final OrderDAO orderDAO = new OrderDAOImpl();
    private static final ReviewDAO reviewDAO = new ReviewDAOImpl();

    private static final UserService userService = new UserServiceImpl(userDAO);
    private static final ProductService productService = new ProductServiceImpl(productDAO);
    private static final CartService cartService = new CartServiceImpl(cartDAO, productDAO);
    private static final OrderService orderService = new OrderServiceImpl(orderDAO, cartDAO, productDAO);
    private static final ReviewService reviewService = new ReviewServiceImpl(reviewDAO, orderDAO);

    private ServiceFactory() {
    }

    public static UserService getUserService() {
        return userService;
    }

    public static ProductService getProductService() {
        return productService;
    }

    public static CartService getCartService() {
        return cartService;
    }

    public static OrderService getOrderService() {
        return orderService;
    }

    public static ReviewService getReviewService() {
        return reviewService;
    }

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    public static ProductDAO getProductDAO() {
        return productDAO;
    }

    public static CartDAO getCartDAO() {
        return cartDAO;
    }

    public static OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public static ReviewDAO getReviewDAO() {
        return reviewDAO;
    }
}
