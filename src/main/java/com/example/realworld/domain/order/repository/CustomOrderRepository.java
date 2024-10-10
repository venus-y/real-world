package com.example.realworld.domain.order.repository;

import com.example.realworld.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOrderRepository {
    public Page<Order> getOrderList(Pageable pageable);
}
