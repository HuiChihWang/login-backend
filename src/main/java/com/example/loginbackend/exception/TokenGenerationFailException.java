package com.example.loginbackend.exception;

public class TokenGenerationFailException extends RuntimeException {
    public TokenGenerationFailException(String message) {
        super(message);
    }
}
