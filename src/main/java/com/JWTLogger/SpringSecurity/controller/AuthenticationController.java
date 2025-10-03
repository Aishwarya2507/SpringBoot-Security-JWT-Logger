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


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(JwtUtil jwtUtil, UserService userService){
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        logger.info("Registering user: {}", user.getUsername());
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        logger.info("Login attempt for user: {}", user.getUsername());

        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null && user.getPassword() != null &&
                existingUser.getPassword() != null &&
                userService.getUserByUsername(user.getUsername()) != null &&
                userService.getUserByUsername(user.getUsername()).getPassword() != null &&
                existingUser.getPassword() != null)
        {

            if(existingUser != null &&
                    existingUser.getPassword() != null &&
                    user.getPassword() != null) {
                 return jwtUtil.generateToken(existingUser.getUsername());
            }
        }

        logger.warn("Invalid login attempt for user: {}", user.getUsername());
        return "Invalid Credentials";
    }

}
