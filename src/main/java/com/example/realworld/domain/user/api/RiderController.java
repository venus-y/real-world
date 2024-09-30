package com.example.realworld.domain.user.api;

import com.example.realworld.domain.user.dto.RiderLocationRegisterDto;
import com.example.realworld.domain.user.service.RiderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;


    @PostMapping("/{riderId}/location")
    public void registerLocation(@PathVariable(name = "riderId") Long riderId, @Valid @RequestBody RiderLocationRegisterDto locationRegisterDto, BindingResult result) {

        riderService.registerLocation(riderId, locationRegisterDto);
    }


}
