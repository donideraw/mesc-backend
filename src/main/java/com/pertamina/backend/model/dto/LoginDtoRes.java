package com.pertamina.backend.model.dto;

import com.pertamina.backend.helper.AppUserType;
import lombok.Data;

@Data
public class LoginDtoRes {
    private String token;
    private String username;
    private AppUserType userType;
    private String name;
}
