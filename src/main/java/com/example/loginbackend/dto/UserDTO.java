package com.example.loginbackend.dto;

public record UserDTO(
        long id,
        String name,
        String username,
        String email
) {
}
