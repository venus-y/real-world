package com.example.realworld.domain.shop.service;

import com.example.realworld.domain.shop.dto.ShopRegisterDto;
import com.example.realworld.domain.shop.entity.Shop;
import com.example.realworld.domain.shop.repository.ShopRepository;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;

    public Long register(ShopRegisterDto registerDto, User user) {
        Shop shop = Shop.builder()
                .name(registerDto.getName())
                .user(user)
                .build();

        Boolean exists = shopRepository.existsByName(registerDto.getName());

        if (exists) {
            throw new AlreadyExistsException(registerDto.getName() + "은 이미 존재하는 상점입니다.");
        }

        shopRepository.save(shop);

        return shop.getId();
    }
}
