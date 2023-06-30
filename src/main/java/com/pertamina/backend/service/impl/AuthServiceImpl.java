package com.pertamina.backend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pertamina.backend.configuration.exception.CustomException;
import com.pertamina.backend.configuration.exception.GeneralException;
import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.dto.LoginDtoReq;
import com.pertamina.backend.model.dto.LoginDtoRes;
import com.pertamina.backend.model.dto.RegisterDto;
import com.pertamina.backend.model.entity.AppUser;
import com.pertamina.backend.repository.AppUserRepository;
import com.pertamina.backend.service.AuthService;
import com.pertamina.backend.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret-key:s14Pb0Ss_d0S3cR#t}")
    private String jwtSecretKey;

    public AuthServiceImpl(AppUserRepository appUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginDtoRes login(LoginDtoReq req) {
        AppUser appUser = appUserRepository.findByUsername(req.getUsername());
        if (appUser == null) {
            throw new CustomException("Username doesn't exist!", HttpStatus.BAD_REQUEST);
        }

        if (appUser.getIsDeleted()) {
            throw new CustomException("User deactivated", HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(req.getPassword(), appUser.getPassword())) {
            String token = JWT.create()
                    .withSubject(appUser.getUsername())
                    .withClaim("id", appUser.getId())
                    .withClaim(AppUserType.class.getSimpleName(), appUser.getUserType().name())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(Algorithm.HMAC512(jwtSecretKey.getBytes()));
            LoginDtoRes loginDtoRes = new LoginDtoRes();
            loginDtoRes.setUsername(appUser.getUsername());
            loginDtoRes.setToken(token);
            loginDtoRes.setUserType(appUser.getUserType());
            loginDtoRes.setName(appUser.getName());
            return loginDtoRes;
        }
        throw new CustomException("Password incorrect!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public AppUser addUser(RegisterDto dto, AppUserType userType) {
        AppAuth auth = SecurityUtil.getAuth();

        if (AppUserType.ADMIN.equals(auth.getUserType()) || AppUserType.ADMIN.equals(userType)) {
            AppUser newUser = new AppUser();
            newUser.setUsername(dto.getUsername());
            newUser.setName(dto.getName());
            newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            newUser.setUserType(userType);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setCreatedBy(auth.getUsername());
            newUser.setIsDeleted(Boolean.FALSE);
            return appUserRepository.save(newUser);
        }

        throw new GeneralException("You don't have access");
    }

}
