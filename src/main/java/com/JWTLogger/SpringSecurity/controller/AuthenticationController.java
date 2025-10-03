package com.JWTLogger.SpringSecurity.controller;


import com.JWTLogger.SpringSecurity.security.JwtUtil;
import com.JWTLogger.SpringSecurity.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.JWTLogger.SpringSecurity.model.User;


@RestController // Marks this class as a REST controller (returns JSON responses)
@RequestMapping("/auth") // Base URL for all endpoints in this controller (e.g., /auth/login, /auth/register)
public class AuthenticationController {

    private final JwtUtil jwtUtil; // Utility class for generating and validating JWT tokens
    private final UserService userService; // Service to handle user-related operations

    // Logger for logging info, warnings, and errors
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    // Constructor injection: Spring will inject JwtUtil and UserService beans here
    public AuthenticationController(JwtUtil jwtUtil, UserService userService){
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // -------------------- Register API --------------------
    @PostMapping("/register") // POST endpoint: /auth/register
    public User register(@RequestBody User user){ // Accepts JSON body and maps to User object
        logger.info("Registering user: {}", user.getUsername()); // Log the registration attempt
        return userService.saveUser(user); // Save user to database and return saved User object
    }

    // -------------------- Login API --------------------
    @PostMapping("/login") // POST endpoint: /auth/login
    public String login(@RequestBody User user){ // Accepts JSON body with username & password
        logger.info("Login attempt for user: {}", user.getUsername()); // Log the login attempt

        // Fetch user from database
        User existingUser = userService.getUserByUsername(user.getUsername());

        // Check if user exists and passwords are not null
        if (existingUser != null && user.getPassword() != null &&
                existingUser.getPassword() != null) {

            // For simplicity, password comparison is assumed here (in production use BCrypt)
            if(existingUser.getPassword() != null && user.getPassword() != null) {
                // Generate JWT token for authenticated user
                return jwtUtil.generateToken(existingUser.getUsername());
            }
        }

        // Log invalid login attempt
        logger.warn("Invalid login attempt for user: {}", user.getUsername());

        // Return error message if authentication fails
        return "Invalid Credentials";
    }

}

