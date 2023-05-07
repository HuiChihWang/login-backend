package com.example.loginbackend.request;

public record RegistrationRequest(
        String username,
        String password,
        String email,
        String name
) {
}
