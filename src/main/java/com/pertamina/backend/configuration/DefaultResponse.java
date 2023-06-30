package com.pertamina.backend.configuration;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DefaultResponse<T> {
    private int status;
    private String message;
    private T data;


    public DefaultResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> DefaultResponse<T> ok(T data){
        return new DefaultResponse(HttpStatus.OK.value(), "SUCCESS", data);
    }

    public static <T> DefaultResponse<T> ok(T data, String message){
        return new DefaultResponse(HttpStatus.OK.value(), message, data);
    }

    public static <T> DefaultResponse<T> error(String message){
        return new DefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static <T> DefaultResponse<T> noContent(String message){
        return new DefaultResponse(HttpStatus.NO_CONTENT.value(), message, null);
    }
}
