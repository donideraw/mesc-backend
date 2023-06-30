package com.pertamina.backend.model.dto;

public class AppException extends RuntimeException {
    private int status = 400;
    private Object data;

    public AppException() {
        super("Something bad happen on app server please try again later, contact support for this error");
    }

    public AppException(String message, int status) {
        super(message);
        this.status = status;
    }

    public AppException(String message, int status, Object data) {
        super(message);
        this.status = status;
        this.data = data;
    }

    public AppException(String message) {
        super(message);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

