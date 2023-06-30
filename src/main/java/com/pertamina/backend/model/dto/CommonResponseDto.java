package com.pertamina.backend.model.dto;

import java.io.Serializable;
import java.util.Date;

public class CommonResponseDto<T> implements Serializable {
    public static final int OK_CODE = 200;
    public static final int OK_CREATED_CODE = 201;
    public static final int ERROR_CODE_REQUEST = 400;
    public static final int ERROR_CODE_SERVER = 500;
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public CommonResponseDto() {
    }

    public CommonResponseDto(int status) {
        this.status = status;
    }

    public CommonResponseDto(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    private int status;
    private T data;
    private String message;
    private Date timestamp = new Date();

    public boolean isSuccess() {
        return status == OK_CODE;
    }

    public static <T> CommonResponseDto<T> ok() {
        return new CommonResponseDto<>(OK_CODE, null, SUCCESS);
    }

    public static <T> CommonResponseDto<T> ok(T data) {
        return new CommonResponseDto<>(OK_CODE, data, SUCCESS);
    }

    public static <T> CommonResponseDto<T> ok(String message) {
        return new CommonResponseDto<>(OK_CODE, null, message);
    }

    public static <T> CommonResponseDto<T> ok(String message, T data) {
        return new CommonResponseDto<>(OK_CODE, data, message);
    }

    public static <T> CommonResponseDto<T> error() {
        return new CommonResponseDto<>(ERROR_CODE_SERVER, null, ERROR);
    }

    public static <T> CommonResponseDto<T> error(String message) {
        return new CommonResponseDto<>(ERROR_CODE_SERVER, null, message);
    }

    public static <T> CommonResponseDto<T> error(T data) {
        return new CommonResponseDto<>(ERROR_CODE_SERVER, data, ERROR);
    }

    public static <T> CommonResponseDto<T> error(String message, T data) {
        return new CommonResponseDto<>(ERROR_CODE_SERVER, data, message);
    }

    public static <T> CommonResponseDto<T> get(int status, String message, T data) {
        return new CommonResponseDto<>(status, data, message);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
