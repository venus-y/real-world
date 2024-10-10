package com.example.realworld.domain.menu.dto;

import com.example.realworld.domain.menu.entity.MenuCategory;
import lombok.Getter;

@Getter
public class MenuSearchCond {
    private String menuName;
    private MenuCategory category;


}
