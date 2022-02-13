package com.example.projectcompass.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/no")
    public String no() {
        return "no";
    }
}
