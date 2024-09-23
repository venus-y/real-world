package com.example.realworld.domain.shop.repository;

import com.example.realworld.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Boolean existsByName(String shopname);
}
