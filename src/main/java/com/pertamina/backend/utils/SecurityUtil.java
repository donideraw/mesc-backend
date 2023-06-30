package com.pertamina.backend.utils;

import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.dto.AppException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static AppAuth getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AppAuth) {
            return (AppAuth) authentication;
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return AppAuth.fromUsernamePasswordAuthenticationToken((UsernamePasswordAuthenticationToken) authentication);
        }
        return null;
    }

    public static void userTypeMustBe(AppUserType originUserType, AppUserType... shouldBeOneOfThisUserType) {
        for (AppUserType userType : shouldBeOneOfThisUserType) {
            if (userType == originUserType)
                return;
        }
        throw new AppException("unauthorized access");
    }
}
