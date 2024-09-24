package com.example.realworld.domain.shop.controller;

import com.example.realworld.domain.shop.dto.ShopRegisterDto;
import com.example.realworld.domain.shop.service.ShopService;
import com.example.realworld.domain.user.entity.CurrentUser;
import com.example.realworld.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/")
    public ResponseEntity<Object> register(@Valid @RequestBody ShopRegisterDto registerDto, @CurrentUser User user, BindingResult bindingResult) {
        Long id = shopService.register(registerDto, user);
        return new ResponseEntity<>(id, HttpStatus.OK);
        

    }
}
