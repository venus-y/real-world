package com.example.realworld.domain.menu.entity;

import lombok.Getter;

@Getter
public enum MenuCategory {
    FOOD("음식"),
    BEVERAGE("음료"),
    DESSERT("디저트"),
    SNACK("간식"),
    ;

    private final String name;

    MenuCategory(String name) {
        this.name = name;
    }
}
