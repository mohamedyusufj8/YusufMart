package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Product operations and catalog management.
 */
public interface ProductService {
    Product createProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int productId, int requesterId, boolean isAdmin);
    Optional<Product> getProductById(int id);
    List<Product> getProducts(String category, String keyword, String sortBy);
    List<Product> getProductsBySeller(int sellerId);
    List<String> getCategories();
    long getProductCount();
}
