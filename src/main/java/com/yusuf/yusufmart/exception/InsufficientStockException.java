package com.yusuf.yusufmart.exception;

/**
 * Thrown when attempting to purchase or add to cart more items than available in stock.
 */
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
