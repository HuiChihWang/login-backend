package com.example.loginbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/hello")
public class HelloController {
    @GetMapping
    public String helloFromWorld() {
        return "Hello From World";
    }
}
