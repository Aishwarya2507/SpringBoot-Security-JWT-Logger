package com.JWTLogger.SpringSecurity.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component // Marks this class as a Spring-managed bean so it can be injected where needed
public class JwtUtil {

    // Generate a secure 256-bit key for HS256 algorithm (automatically secure enough)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT expiration time in milliseconds (1 hour)
    private final long jwtExpiration = 3600000;

    // -------------------- Generate JWT Token --------------------
    // Takes a username and creates a JWT token with subject, issued time, expiration, and signature
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Set the username as subject claim
                .setIssuedAt(new Date()) // Current timestamp as token issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Token expiration time
                .signWith(key)  // Sign the token using HS256 secure key
                .compact();    // Build the token as a compact String
    }

    // -------------------- Extract Username --------------------
    // Parses the token and returns the 'subject' (username) from JWT claims
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // -------------------- Validate Token --------------------
    // Validates token by checking:
    // 1. Username in token matches provided username
    // 2. Token is not expired
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // -------------------- Get Claims --------------------
    // Parses JWT token and returns all claims (like subject, issuedAt, expiration, etc.)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // Use the same key to validate signature
                .build()
                .parseClaimsJws(token) // Parse the signed JWT
                .getBody();            // Return the body (claims)
    }

    // -------------------- Check Expiration --------------------
    // Returns true if token is expired
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
