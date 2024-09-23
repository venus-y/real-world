package com.example.realworld.domain.order.entity;

import com.example.realworld.common.BaseTimeEntity;
import com.example.realworld.domain.menu.entity.Menu;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
@Table(name = "order_menu")

public class OrderMenu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer count;

    public static OrderMenu toOrderMenu(Integer count, Order order, Menu menu) {
        OrderMenu orderMenu = new OrderMenu();
        orderMenu.order = order;
        orderMenu.menu = menu;
        orderMenu.count = count;
        return orderMenu;
    }
}
