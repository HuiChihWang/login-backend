package com.example.loginbackend.controller;

import com.example.loginbackend.entity.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {
    @GetMapping
    public String getIndex(Authentication auth) {
        AppUser k = (AppUser) auth.getPrincipal();
        System.out.println(k.getAuthorities());
        return "I m Groot.";
    }
}
