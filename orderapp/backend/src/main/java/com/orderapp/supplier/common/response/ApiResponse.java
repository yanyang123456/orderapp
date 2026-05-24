package com.orderapp.supplier.common.response;

public class ApiResponse<T> {

    private int error;
    private T body;
    private String message;
    private boolean success;

    public static <T> ApiResponse<T> ok(T body, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(0);
        response.setBody(body);
        response.setMessage(message);
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> fail(int error, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(error);
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
