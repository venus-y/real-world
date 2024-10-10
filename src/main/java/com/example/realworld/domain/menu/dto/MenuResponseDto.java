package com.example.realworld.domain.menu.dto;

import com.example.realworld.domain.menu.entity.MenuCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MenuResponseDto {
    private String name;
    private Double price;
    private MenuCategory category;

    @QueryProjection
    public MenuResponseDto(String name, Double price, MenuCategory category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }


}
