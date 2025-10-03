package com.JWTLogger.SpringSecurity.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger for logging info and errors
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // This annotation tells Spring that this method should handle exceptions of type RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){

        // Log the exception message using the logger (for debugging or auditing)
        logger.error("Exception occurred: {}", ex.getMessage());

        // Return a ResponseEntity with:
        // 1. The exception message as the response body
        // 2. HTTP status code 400 (Bad Request)
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
