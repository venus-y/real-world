package com.example.realworld.domain.delivery.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryResponseDto {
    private Long id;
    private Long orderId;
    private Long riderId;
}
