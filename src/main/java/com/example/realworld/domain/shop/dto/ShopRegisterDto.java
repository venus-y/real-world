package com.example.realworld.domain.shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ShopRegisterDto {
    @NotNull
    private String name;
}
