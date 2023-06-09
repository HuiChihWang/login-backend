package com.example.loginbackend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @Pattern(regexp = "^[a-zA-Z0-9]+$")
        @Size(min = 7)
        String username,

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()-=_+{};':\"\\\\|,.<>/?]).+$")
        @Size(min = 7)
        String password,

        @Email
        String email,

        @NotEmpty
        String name
) {
}
