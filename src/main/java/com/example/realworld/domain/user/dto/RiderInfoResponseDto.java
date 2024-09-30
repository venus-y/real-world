package com.example.realworld.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RiderInfoResponseDto {
    private long id;
    private double distance;


}
