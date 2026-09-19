package com.yusuf.yusufmart.exception;

/**
 * Custom exceptions for YusufMart application.
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
