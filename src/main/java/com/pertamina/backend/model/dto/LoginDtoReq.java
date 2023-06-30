package com.pertamina.backend.model.dto;

import lombok.Data;

@Data
public class LoginDtoReq {
    private String username;
    private String password;
}
