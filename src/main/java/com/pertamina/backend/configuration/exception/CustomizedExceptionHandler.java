package com.pertamina.backend.configuration.exception;

import com.pertamina.backend.model.dto.GlobalResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<GlobalResponseEntity> httpExceptionHandler(CustomException ex) {
        return GlobalResponseEntity.error(
                "99",
                ex.getMessage(),
                "",
                ex.getHttpStatus()
        );
    }

}
