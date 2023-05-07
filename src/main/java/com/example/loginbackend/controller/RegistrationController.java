package com.example.loginbackend.controller;

import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.request.RegistrationRequest;
import com.example.loginbackend.service.RegistrationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/register")
@RequiredArgsConstructor
public class RegistrationController {
    @NonNull
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<AppUser> register(@RequestBody RegistrationRequest request) {
        AppUser user = registrationService.registration(request);
        return ResponseEntity.ok(user);
    }
}
