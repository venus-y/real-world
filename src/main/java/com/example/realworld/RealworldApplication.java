package com.example.realworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// 시큐리티 설정 후에 지워야함
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class RealworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealworldApplication.class, args);
    }

}
