package com.example.realworld.domain.user.controller;


import com.example.realworld.domain.user.dto.RegisterDto;
import com.example.realworld.domain.user.service.RegisterService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("")
    public ResponseEntity<Object> register(@Validated @RequestBody RegisterDto dto, BindingResult bindingResult){
        registerService.insert(dto);

        if(bindingResult.hasErrors()){
            System.out.println("bindingResult = " + bindingResult);
        }

        return ResponseEntity
                .ok()
                .build();
    }
}
