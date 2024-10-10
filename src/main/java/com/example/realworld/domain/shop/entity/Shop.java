package com.example.realworld.domain.shop.entity;

import com.example.realworld.common.Address;
import com.example.realworld.domain.menu.entity.Menu;
import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.user.entity.User;
import jakarta.persistence.Entity;
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

@Builder
@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Address address;

    @OneToMany(mappedBy = "shop")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Menu> menuList = new ArrayList<>();

    public Shop(Long id) {
        this.id = id;
    }

}
