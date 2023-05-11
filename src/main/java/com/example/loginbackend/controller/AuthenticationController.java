package com.example.loginbackend.controller;

import com.example.loginbackend.request.AuthenticationRequest;
import com.example.loginbackend.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authManager;

    private final UserDetailsService userService;

    private final JwtUtility jwtUtility;

    @PostMapping
    ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails user = userService.loadUserByUsername(request.username());
        String newJwtToken = jwtUtility.issueToken(user);

        return ResponseEntity.ok(newJwtToken);
    }
}