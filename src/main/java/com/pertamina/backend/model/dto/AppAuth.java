package com.pertamina.backend.model.dto;

import com.pertamina.backend.helper.AppUserType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppAuth extends UsernamePasswordAuthenticationToken {
    public static AppAuth fromUsernamePasswordAuthenticationToken(UsernamePasswordAuthenticationToken authenticationToken) {
        return new AppAuth(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), authenticationToken.getAuthorities());
    }

    public AppAuth(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AppAuth(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    private Long id = 0L;
    private AppUserType userType = AppUserType.ADMIN;
    private String username = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserType getUserType() {
        return userType;
    }

    public void setUserType(AppUserType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

