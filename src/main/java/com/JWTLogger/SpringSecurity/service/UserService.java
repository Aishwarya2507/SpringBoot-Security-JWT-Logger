package com.JWTLogger.SpringSecurity.service;

import com.JWTLogger.SpringSecurity.model.User;
import com.JWTLogger.SpringSecurity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service // Marks this class as a Spring service bean (business logic layer)
public class UserService {

    private final UserRepository userRepository; // Repository for CRUD operations on User entity
    private final PasswordEncoder passwordEncoder; // Encoder to hash passwords before saving

    // Constructor injection: Spring injects UserRepository and PasswordEncoder beans
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------- Save User --------------------
    public User saveUser(User user){
        // Encode password using BCrypt before storing in database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save user entity to database and return the saved user
        return userRepository.save(user);
    }

    // -------------------- Get User By Username --------------------
    public User getUserByUsername(String username){
        // Find user by username using repository, return null if not found
        return userRepository.findByUsername(username).orElse(null);
    }
}

