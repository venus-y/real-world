package com.example.realworld.domain.user.controller;


import com.example.realworld.domain.user.dto.MemberRegisterDto;
import com.example.realworld.domain.user.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService registerService;

    @PostMapping("/")
    public ResponseEntity<Object> register(@Valid @RequestBody MemberRegisterDto memberRegisterDto, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {

            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.info("Validation errors; {} ", errorMessage);

            throw new BindException(bindingResult);
        }

        Long id = registerService.insert(memberRegisterDto);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }


}
