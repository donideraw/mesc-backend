package com.pertamina.backend.service;


import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.model.dto.LoginDtoReq;
import com.pertamina.backend.model.dto.LoginDtoRes;
import com.pertamina.backend.model.dto.RegisterDto;
import com.pertamina.backend.model.entity.AppUser;

public interface AuthService {
    LoginDtoRes login(LoginDtoReq req);
    AppUser addUser(RegisterDto dto, AppUserType userType);
}
