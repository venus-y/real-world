package com.example.realworld.domain.user.api;

import com.example.realworld.domain.menu.dto.MenuRequestDto;
import com.example.realworld.domain.user.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/menus")
    public ResponseEntity<Object> register(@Valid @RequestBody MenuRequestDto menuRequestDto) {
        Long insert = ownerService.insert(menuRequestDto);

        return new ResponseEntity<>(insert, HttpStatus.OK);
    }
}
