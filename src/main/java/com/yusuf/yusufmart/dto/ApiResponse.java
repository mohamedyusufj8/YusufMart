package com.yusuf.yusufmart.dto;

import java.io.Serializable;

/**
 * Standard API response envelope as specified in Section 13.
 *
 * Success format:
 * { "success": true, "data": { ... }, "error": null }
 *
 * Error format:
 * { "success": false, "data": null, "error": { "code": "VALIDATION_ERROR", "message": "..." } }
 */
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private T data;
    private ApiError error;

    public ApiResponse() {
    }

    private ApiResponse(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code, message));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }

    public static class ApiError implements Serializable {
        private static final long serialVersionUID = 1L;

        private String code;
        private String message;

        public ApiError() {
        }

        public ApiError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
