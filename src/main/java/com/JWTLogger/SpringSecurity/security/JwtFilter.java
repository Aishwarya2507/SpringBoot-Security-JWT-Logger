package com.JWTLogger.SpringSecurity.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component // Marks this class as a Spring-managed bean so it can be injected where needed
public class JwtFilter extends OncePerRequestFilter { // Extends Spring’s filter to execute once per request

    private final JwtUtil jwtUtil; // JwtUtil instance used for generating and validating JWT tokens
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class); // Logger for logging info and errors

    // Constructor injection: Spring will inject JwtUtil bean here
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Core filter method executed for every HTTP request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // Get the Authorization header from HTTP request
        String token = null; // Initialize JWT token variable
        String username = null; // Initialize username variable extracted from token

        // Check if Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix to get only the JWT
            try {
                username = jwtUtil.extractUsername(token); // Extract username from JWT token
            } catch (Exception e) {
                // Log an error if token parsing fails (e.g., expired, malformed, or invalid)
                logger.error("JWT token parsing failed: {}", e.getMessage());
            }
        }

        // Check if username is extracted and authentication is not already set in context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate the token using JwtUtil
            if (jwtUtil.validateToken(token, username)) {
                // Create an authentication token with username, no credentials, empty authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                // Set authentication in Spring Security context so user is considered "authenticated"
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Log successful validation
                logger.info("JWT validated for user: {}", username);
            }
        }

        // Continue the filter chain (let request proceed to next filter or endpoint)
        filterChain.doFilter(request, response);
    }
}






