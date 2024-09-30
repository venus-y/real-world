package com.example.realworld.domain.user.service;

import com.example.realworld.domain.fcm.service.FcmService;
import com.example.realworld.domain.geo.api.GeoCoding;
import com.example.realworld.domain.order.dto.NotificationRequestDto;
import com.example.realworld.domain.order.repository.OrderRepository;
import com.example.realworld.domain.user.dto.RiderLocationRegisterDto;
import com.example.realworld.domain.user.exception.NotFoundException;
import com.example.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RiderService {
    private static final String SHOP_GEO_KEY = "shop:locations";
    private static final String RIDER_GEO_KEY = "rider:locations";
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final OrderRepository orderRepository;


    public void registerLocation(Long riderId, RiderLocationRegisterDto locationRegisterDto) {

        Map<String, String> geoDataByAddress = GeoCoding.getGeoDataByAddress(locationRegisterDto.toString());


        if (geoDataByAddress != null) {

            double lng = Double.parseDouble(geoDataByAddress.get("lng"));
            double lat = Double.parseDouble(geoDataByAddress.get("lat"));

            saveLocation(riderId, lng, lat);

        }


    }

    public void saveLocation(Long riderId, double lng, double lat) {
        redisTemplate
                .opsForGeo()
                .add(RIDER_GEO_KEY, new Point(lng, lat), riderId.toString());
    }

    public List<GeoResult<RedisGeoCommands.GeoLocation<String>>> findNearestRiders(Long shopId, double radiusKm) {
        List<Point> shopLocation =
                redisTemplate
                        .opsForGeo()
                        .position(SHOP_GEO_KEY, shopId.toString());
        if (shopLocation.isEmpty()) {
            throw new NotFoundException("상점 위치를 찾을 수 없습니다.");
        }
        Point shopPoint = shopLocation.get(0);

        Circle circle = new Circle(shopPoint, new Distance(radiusKm, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .sortAscending()
                .limit(10)
                .includeDistance()
                .includeCoordinates();

        List<Point> test = redisTemplate.opsForGeo().position(RIDER_GEO_KEY, shopId.toString());

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius(RIDER_GEO_KEY, circle, args);

        return results.getContent();
    }


    // 라이더 위치 조회
    public void demoMethod(Long shopId, NotificationRequestDto notificationRequestDto) {

        orderRepository.findById(notificationRequestDto
                        .getOrderId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 주문정보입니다"));

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResults = findNearestRiders(shopId, notificationRequestDto.getRadius());

//        return geoResults.stream().map(geoResult -> {
//            long riderId = Long.parseLong(geoResult.getContent().getName());
//            double distance = geoResult.getDistance().getValue();
//            // 라이더 추가 정보 조회 (예: 데이터베이스에서)
//            return RiderInfoResponseDto.builder()
//                    .id(riderId)
//                    .distance(distance)
//                    .build();
//        }).collect(Collectors.toList());

        List<String> riderIds =
                geoResults
                        .stream()
                        .map(geo -> geo.getContent().getName())
                        .toList();

        List<String> multipleFcmTokens = fcmService.getMultipleFcmTokens(riderIds);
        fcmService.sendMulticastMessageAsync("Test title", "orderNum: " + notificationRequestDto.getOrderId(), multipleFcmTokens);
    }

}
