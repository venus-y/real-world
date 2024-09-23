package com.example.realworld.domain.admin.controller;


import com.example.realworld.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/test")
    public void test() {
        log.info("ttttttttttttttttttttttttttttttt");
    }


}
