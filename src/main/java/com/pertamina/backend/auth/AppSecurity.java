package com.pertamina.backend.auth;

import com.pertamina.backend.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class AppSecurity extends WebSecurityConfigurerAdapter {
    private final AuthoFilter jwtFilter;
    private final Environment environment;
    private final TrailingLogFilter trailingLogFilter;

    public AppSecurity(AppUserRepository userRepo, AuthoFilter jwtFilter, Environment environment, TrailingLogFilter trailingLogFilter) {
        this.jwtFilter = jwtFilter;
        this.environment = environment;
        this.trailingLogFilter = trailingLogFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] swaggerResources = new String[]{"/v2/api-docs", // swagger resources
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**"};
        // define endpoint that should be secured or not.
        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/sample").permitAll()
                .antMatchers(swaggerResources).permitAll();

        http.authorizeRequests().anyRequest().fullyAuthenticated()
                .and()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(trailingLogFilter, AuthoFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
