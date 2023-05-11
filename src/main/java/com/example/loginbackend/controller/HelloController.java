package com.example.loginbackend.controller;

import com.example.loginbackend.dto.UserDTO;
import com.example.loginbackend.entity.AppUser;
import com.example.loginbackend.exception.UserNotAllowedException;
import com.example.loginbackend.service.AppUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {
    @NonNull
    private final AppUserService userService;
    @GetMapping("/public/hello")
    public String helloFromWorld() {
        return "Hello From World";
    }

    @GetMapping("/hello/{id}")
    public String helloVIPMember(@PathVariable("id") long userId, Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        if (currentUser.getId() != userId) {
            throw new UserNotAllowedException();
        }

        UserDTO user = userService.getUserInfoById(userId);

        return String.format("Hello VIP %s", user.name());
    }
}
