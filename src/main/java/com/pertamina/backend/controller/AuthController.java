package com.pertamina.backend.controller;

import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.model.dto.LoginDtoReq;
import com.pertamina.backend.model.dto.LoginDtoRes;
import com.pertamina.backend.model.dto.RegisterDto;
import com.pertamina.backend.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register/admin")
    public ResponseEntity<GlobalResponseEntity> createAdmin(@RequestBody RegisterDto dto) {
        return GlobalResponseEntity.ok(service.addUser(dto, AppUserType.ADMIN));
    }

    @PostMapping("/register/staff")
    public ResponseEntity<GlobalResponseEntity> createStaff(@RequestBody RegisterDto dto) {
        return GlobalResponseEntity.ok(service.addUser(dto, AppUserType.STAFF));
    }

    @PostMapping("/register/verificator")
    public ResponseEntity<GlobalResponseEntity> createVerificator(@RequestBody RegisterDto dto) {
        return GlobalResponseEntity.ok(service.addUser(dto, AppUserType.VERIFICATOR));
    }

    @PostMapping(value = "auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalResponseEntity> validateOtpCode(@RequestBody LoginDtoReq loginReq) {
        LoginDtoRes result = service.login(loginReq);
        return GlobalResponseEntity.ok(result);
    }
}
