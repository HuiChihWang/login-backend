package com.example.loginbackend.exception;

public class TokenNotExistedException extends RuntimeException {
    public TokenNotExistedException(String message) {
        super(message);
    }
}
