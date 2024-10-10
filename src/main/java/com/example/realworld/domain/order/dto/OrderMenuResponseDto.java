package com.example.realworld.domain.order.dto;

import com.example.realworld.domain.order.entity.OrderMenu;
import lombok.Getter;

@Getter
public class OrderMenuResponseDto {
    private Long orderMenuId;
    private String menuName;
    private Integer count;


    public OrderMenuResponseDto(OrderMenu orderMenu) {
        this.orderMenuId = orderMenu.getId();
        this.menuName = orderMenu.getMenu().getName();
        this.count = orderMenu.getCount();
    }
}
