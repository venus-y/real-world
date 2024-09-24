package com.example.realworld.domain.user.service;


import com.example.realworld.domain.menu.dto.MenuRequestDto;
import com.example.realworld.domain.menu.entity.Menu;
import com.example.realworld.domain.menu.repository.MenuRepository;
import com.example.realworld.domain.shop.entity.Shop;
import com.example.realworld.domain.shop.repository.ShopRepository;
import com.example.realworld.domain.user.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerService {
    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;


    public Long insert(MenuRequestDto menuRequestDto) {

        Long shopId = menuRequestDto.getShopId();

        Shop shop = shopRepository
                .findById(shopId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상점입니다."));

        Menu menu = Menu.builder()
                .price(menuRequestDto.getPrice())
                .name(menuRequestDto.getName())
                .category(menuRequestDto.getCategory())
                .shop(shop)
                .build();

        menuRepository.save(menu);

        return menu.getId();
    }
}
