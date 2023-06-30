package com.pertamina.backend.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pertamina.backend.helper.AppUserType;
import com.pertamina.backend.helper.Constant;
import com.pertamina.backend.model.dto.AppAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class AuthoFilter extends OncePerRequestFilter {
    @Value("${app.jwt.secret-key:s14Pb0Ss_d0S3cR#tt}")
    private String jwtSecretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorization = request.getHeader(Constant.HEADER_AUTHORIZATION);

        if (StringUtils.isEmpty(authorization)) {
            chain.doFilter(request, response);
            return;
        }
        // type Bearer
        if (authorization.startsWith(Constant.AUTHORIZATION_TYPE_BEARER)) {
            AppAuth authentication = getAuthentication(authorization);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
            return;
        }
        // type Key
        if (authorization.startsWith(Constant.AUTHORIZATION_TYPE_KEY)) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    private AppAuth getAuthentication(String authorization) {
        String username = null;
        long id = 0L;
        AppUserType userType = null;
        try {
            // parse the token.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtSecretKey.getBytes()))
                    .build()
                    .verify(authorization.substring(Constant.AUTHORIZATION_TYPE_BEARER.length() + 1));
            username = decodedJWT.getSubject();
            id = !decodedJWT.getClaim("id").isNull() ? decodedJWT.getClaim("id").asLong() : 0;
            userType = decodedJWT.getClaim(AppUserType.class.getSimpleName()).isNull() ?
                    null :
                    AppUserType.valueOf(decodedJWT.getClaim(AppUserType.class.getSimpleName()).asString());
        } catch (Exception e) {
        }
        if (username != null) {
            AppAuth appAuth = new AppAuth(username, null, Collections.emptyList());
            appAuth.setId(id);
            appAuth.setUserType(userType);
            appAuth.setUsername(username);
            return appAuth;
        }
        return null;
    }
}

