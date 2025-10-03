package com.JWTLogger.SpringSecurity.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration // Marks this class as a Spring configuration class
public class SecurityConfig {

    private final JwtFilter jwtFilter; // JwtFilter instance to validate JWT tokens for requests

    // Constructor injection: Spring will inject JwtFilter bean here
    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    // -------------------- Security Filter Chain --------------------
    // Defines how HTTP security is configured in the application
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable() // Disable CSRF protection (common for APIs using JWT)
                .authorizeHttpRequests() // Start configuring authorization rules
                .requestMatchers("/auth/**", "/h2-console/**").permitAll()
                // Allow unauthenticated access to login, registration, and H2 console
                .anyRequest().authenticated()
                // Require authentication for all other requests
                .and()
                .headers().frameOptions().disable()
                // Disable frame options to allow H2 console in browser
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Do not create HTTP session; JWT is stateless authentication

        // Add custom JWT filter before Spring Security's UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build(); // Build and return the SecurityFilterChain
    }

    // -------------------- Password Encoder --------------------
    // Bean to encode passwords using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // -------------------- Authentication Manager --------------------
    // Bean to expose AuthenticationManager, required for authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}

