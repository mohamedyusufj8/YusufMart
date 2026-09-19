package com.yusuf.yusufmart.exception;

/**
 * Thrown when an entity (product, order, user) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
