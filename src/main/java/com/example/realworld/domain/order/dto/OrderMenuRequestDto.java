package com.example.realworld.domain.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderMenuRequestDto {

    @NotNull
    private Long menuId;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer count;

    @NotNull
    private Double price;

    public Double calculatePrice() {
        return this.count * this.price;
    }


}
