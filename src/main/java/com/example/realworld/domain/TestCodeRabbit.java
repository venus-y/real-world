package com.example.realworld.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbits")
public class TestCodeRabbit {
    @GetMapping("/")
    public ResponseEntity<Object> response() {

        return ResponseEntity.ok("t");
    }
}
