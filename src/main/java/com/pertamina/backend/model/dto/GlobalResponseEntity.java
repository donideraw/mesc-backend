package com.pertamina.backend.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponseEntity {
    private Date timestamp;
    private String statusCode;
    private String statusDescription;
    private Object data;

    public static ResponseEntity<GlobalResponseEntity> ok(Object data) {
        return ResponseEntity.ok(new GlobalResponseEntity(
                new Date(),
                "00",
                "Success",
                data));
    }

    public static ResponseEntity<GlobalResponseEntity> ok(String description) {
        return ResponseEntity.ok(new GlobalResponseEntity(
                new Date(),
                "00",
                description,
                null));
    }

    public static ResponseEntity<GlobalResponseEntity> ok(String description, Object data) {
        return ResponseEntity.ok(new GlobalResponseEntity(
                new Date(),
                "00",
                description,
                data));
    }

    public static ResponseEntity<GlobalResponseEntity> badRequest(String description, Object data) {
        return ResponseEntity.ok(new GlobalResponseEntity(
                new Date(),
                "99",
                description,
                data));
    }

    public static ResponseEntity<GlobalResponseEntity> error(String statusCode, String description, String data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new GlobalResponseEntity(
                new Date(),
                statusCode,
                description,
                data), httpStatus);
    }

    public static ResponseEntity<GlobalResponseEntity> error(String statusCode, String description, Object data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new GlobalResponseEntity(
                new Date(),
                statusCode,
                description,
                data), httpStatus);
    }
}
