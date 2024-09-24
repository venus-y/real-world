package com.example.realworld.domain.menu.dto;

import com.example.realworld.domain.menu.entity.MenuCategory;
import lombok.Data;

@Data
public class MenuRequestDto {

    private String name;
    private Double price;
    private Long shopId;
    private MenuCategory category;
}
