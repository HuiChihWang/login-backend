package com.example.loginbackend.exception;

public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException() {
        super("User is not allowed for operation");
    }
}
