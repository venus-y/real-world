package com.example.realworld.domain.user.dto;

import lombok.Getter;

@Getter
public class RiderLocationResponseDto {
    private String address;

    public static RiderLocationResponseDto toRiderLocationResponseDto(String address) {
        RiderLocationResponseDto riderLocationResponseDto = new RiderLocationResponseDto();
        riderLocationResponseDto.address = address;
        return riderLocationResponseDto;
    }


}
