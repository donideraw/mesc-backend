package com.pertamina.backend.controller;

import com.pertamina.backend.model.dto.GlobalResponseEntity;
import com.pertamina.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<GlobalResponseEntity> getAllUsers() {
        return GlobalResponseEntity.ok(service.findAllUser());
    }

    @GetMapping("/deactivate/{id}")
    public ResponseEntity<GlobalResponseEntity> deactivateUser(@PathVariable("id") Long id) {
        return GlobalResponseEntity.ok(service.deactivateUser(id));
    }

    @GetMapping("/activate/{id}")
    public ResponseEntity<GlobalResponseEntity> activateUser(@PathVariable("id") Long id) {
        return GlobalResponseEntity.ok(service.activateUser(id));
    }

}
