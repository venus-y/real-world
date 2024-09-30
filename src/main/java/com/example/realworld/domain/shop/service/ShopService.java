package com.example.realworld.domain.shop.service;

import com.example.realworld.domain.geo.api.GeoCoding;
import com.example.realworld.domain.shop.dto.ShopRegisterDto;
import com.example.realworld.domain.shop.entity.Shop;
import com.example.realworld.domain.shop.repository.ShopRepository;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.realworld.common.Address.toAddress;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private static final String SHOP_GEO_KEY = "shop:locations";
    private final RedisTemplate<String, String> redisTemplate;
    private final ShopRepository shopRepository;

    public Long register(ShopRegisterDto registerDto, User user) {
        Shop shop = Shop.builder()
                .name(registerDto.getName())
                .user(user)
                .address(toAddress(registerDto.getStreet(), registerDto.getCity(), registerDto.getZipcode()))
                .build();

        Boolean exists = shopRepository.existsByName(registerDto.getName());

        if (exists) {
            throw new AlreadyExistsException(registerDto.getName() + "은 이미 존재하는 상점입니다.");
        }

        shopRepository.save(shop);
        // Reids에 상점의 위치정보를 저장
        saveShopLocation(shop);

        return shop.getId();
    }

    private void saveShopLocation(Shop shop) {
        Map<String, String> geoData = GeoCoding.getGeoDataByAddress(shop.getAddress().toString());

        if (geoData != null) {
            double lng = Double.parseDouble(geoData.get("lng"));
            double lat = Double.parseDouble(geoData.get("lat"));

            redisTemplate
                    .opsForGeo()
                    .add(SHOP_GEO_KEY, new Point(lng, lat), shop.getId().toString());
        }

    }

}
