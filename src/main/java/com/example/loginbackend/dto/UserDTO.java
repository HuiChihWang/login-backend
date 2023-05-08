package com.example.loginbackend.dto;

public record UserDTO(
        long userId,
        String name,
        String username,
        String email
) {
}
