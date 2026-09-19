package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.ProductDAO;
import com.yusuf.yusufmart.exception.ResourceNotFoundException;
import com.yusuf.yusufmart.exception.UnauthorizedException;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.Product;
import com.yusuf.yusufmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Product management.
 */
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product createProduct(Product product) {
        validateProduct(product);
        try {
            return productDAO.create(product);
        } catch (SQLException e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            throw new RuntimeException("Database error creating product.", e);
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        validateProduct(product);
        try {
            Optional<Product> existing = productDAO.findById(product.getId());
            if (existing.isEmpty()) {
                throw new ResourceNotFoundException("Product not found with id: " + product.getId());
            }
            if (existing.get().getSellerId() != product.getSellerId()) {
                throw new UnauthorizedException("You are not authorized to update this listing.");
            }
            return productDAO.update(product);
        } catch (SQLException e) {
            logger.error("Error updating product: {}", e.getMessage(), e);
            throw new RuntimeException("Database error updating product.", e);
        }
    }

    @Override
    public boolean deleteProduct(int productId, int requesterId, boolean isAdmin) {
        try {
            Optional<Product> existing = productDAO.findById(productId);
            if (existing.isEmpty()) {
                throw new ResourceNotFoundException("Product not found with id: " + productId);
            }
            if (!isAdmin && existing.get().getSellerId() != requesterId) {
                throw new UnauthorizedException("You are not authorized to delete this listing.");
            }
            return productDAO.delete(productId);
        } catch (SQLException e) {
            logger.error("Error deleting product: {}", e.getMessage(), e);
            throw new RuntimeException("Database error deleting product.", e);
        }
    }

    @Override
    public Optional<Product> getProductById(int id) {
        try {
            return productDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Error getting product by id: {}", e.getMessage(), e);
            throw new RuntimeException("Database error fetching product.", e);
        }
    }

    @Override
    public List<Product> getProducts(String category, String keyword, String sortBy) {
        try {
            return productDAO.findAll(category, keyword, sortBy);
        } catch (SQLException e) {
            logger.error("Error getting products: {}", e.getMessage(), e);
            throw new RuntimeException("Database error fetching products.", e);
        }
    }

    @Override
    public List<Product> getProductsBySeller(int sellerId) {
        try {
            return productDAO.findBySellerId(sellerId);
        } catch (SQLException e) {
            logger.error("Error getting products by seller: {}", e.getMessage(), e);
            throw new RuntimeException("Database error fetching seller products.", e);
        }
    }

    @Override
    public List<String> getCategories() {
        try {
            return productDAO.findDistinctCategories();
        } catch (SQLException e) {
            logger.error("Error getting categories: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public long getProductCount() {
        try {
            return productDAO.count();
        } catch (SQLException e) {
            logger.error("Error counting products: {}", e.getMessage(), e);
            return 0;
        }
    }

    private void validateProduct(Product product) {
        if (!ValidationUtil.isNotEmpty(product.getName())) {
            throw new ValidationException("Product title is required.");
        }
        if (!ValidationUtil.isNotEmpty(product.getCategory())) {
            throw new ValidationException("Category is required.");
        }
        if (!ValidationUtil.isPositive(product.getPrice())) {
            throw new ValidationException("Price must be greater than zero.");
        }
        if (!ValidationUtil.isNonNegative(product.getStockQty())) {
            throw new ValidationException("Stock quantity cannot be negative.");
        }
    }
}
