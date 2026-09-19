package com.yusuf.yusufmart.exception;

/**
 * Thrown when an authenticated user attempts to access a resource not permitted for their role.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
