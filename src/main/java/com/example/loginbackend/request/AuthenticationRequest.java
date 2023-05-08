package com.example.loginbackend.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @Pattern(regexp = "^[a-zA-Z0-9]+$")
        @Size(min = 7)
        String username,

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()-=_+{};':\"\\\\|,.<>/?]).+$")
        @Size(min = 7)
        String password
) {
}

