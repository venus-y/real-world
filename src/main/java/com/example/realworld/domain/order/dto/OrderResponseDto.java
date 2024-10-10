package com.example.realworld.domain.order.dto;


import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.order.entity.OrderMenu;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private Long shopId;
    private String orderStatus;
    private List<OrderMenuResponseDto> orderMenuResponseDtoList;

    public OrderResponseDto(Order order, List<OrderMenu> orderMenuList) {
        this.orderId = order.getId();
        this.userId = order.getUser().getId();
        this.shopId = order.getShop().getId();
        this.orderStatus = order.getOrderStatus().toString();
        this.orderMenuResponseDtoList = orderMenuList
                .stream().map(OrderMenuResponseDto::new)
                .collect(Collectors.toList());
    }
}
