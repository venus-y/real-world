package com.example.realworld.domain.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NotificationRequestDto {
    @NotNull
    private Long orderId;
    @NotNull
    @Min(5)
    @Max(1500)
    private double radius;
}
