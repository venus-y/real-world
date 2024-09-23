package com.example.realworld.domain.menu.entity;

import com.example.realworld.domain.order.entity.OrderMenu;
import com.example.realworld.domain.shop.entity.Shop;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    @Enumerated(EnumType.STRING)
    private MenuCategory category;
    @OneToMany(mappedBy = "menu")
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;


}
