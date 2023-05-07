package com.example.loginbackend.exception;

import com.example.loginbackend.entity.AppUser;

public class UserAlreadyExistedException extends RuntimeException {
    private static final String USERNAME_EXISTED_ERROR_MESSAGE = "User with username %s already exists";
    private static final String EMAIL_EXISTED_ERROR_MESSAGE = "User with email %s already exists";
    public UserAlreadyExistedException(AppUser user, ERROR_TYPE type) {
        super(
                String.format(
                type == ERROR_TYPE.USERNAME_EXISTED ? USERNAME_EXISTED_ERROR_MESSAGE : EMAIL_EXISTED_ERROR_MESSAGE,
                type == ERROR_TYPE.USERNAME_EXISTED ? user.getUsername() : user.getEmail()
        ));
    }

    public enum ERROR_TYPE {
        USERNAME_EXISTED,
        EMAIL_EXISTED,
    }
}
