package com.example.realworld.domain.delivery.service;

import com.example.realworld.domain.delivery.dto.DeliveryResponseDto;
import com.example.realworld.domain.delivery.entity.Delivery;
import com.example.realworld.domain.delivery.entity.DeliveryStatus;
import com.example.realworld.domain.delivery.exception.ConcurrentAccessException;
import com.example.realworld.domain.delivery.repository.DeliveryRepository;
import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.order.entity.OrderStatus;
import com.example.realworld.domain.order.exception.OrderAlreadyAssigedException;
import com.example.realworld.domain.order.repository.OrderRepository;
import com.example.realworld.domain.user.dto.RiderLocationResponseDto;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.NotFoundException;
import com.example.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.example.realworld.domain.user.dto.RiderLocationResponseDto.toRiderLocationResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryService {
    private static final String LOCK_KEY_PREFIX = "order_lock:";
    private static final long LOCK_TIMEOUT = 10;
    private static final String RIDER_GEO_KEY = "rider:locations";
    private final RedisTemplate<String, String> redisTemplate;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DeliveryResponseDto acceptDelivery(Long orderId, Long riderId) {
        String lockKey = LOCK_KEY_PREFIX + orderId;

        Boolean locked = redisTemplate
                .opsForValue()
                .setIfAbsent(lockKey, riderId.toString(), LOCK_TIMEOUT, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(locked)) {
            try {
                // 주문 수락 로직
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

                User rider = userRepository.findById(riderId)
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 라이더입니다"));

                if (order.getOrderStatus()
                        .equals(OrderStatus.ASSIGNED)) {
                    throw new OrderAlreadyAssigedException("이미 다른 라이더에게 배정된 주문입니다.");
                }

                Delivery delivery = Delivery.builder()
                        .rider(rider)
                        .order(order)
                        .pickupTime(LocalDateTime.now())
                        .status(DeliveryStatus.ASSIGNED)
                        .address(order.getUser().getAddress())
                        .build();


                order.updateOrderStatus(OrderStatus.ASSIGNED);

                deliveryRepository.save(delivery);

                return DeliveryResponseDto.builder()
                        .id(delivery.getId())
                        .orderId(order.getId())
                        .riderId(rider.getId())
                        .build();
            } finally {
                // 락 해제
                redisTemplate.delete(lockKey);
            }
        } else {
            throw new ConcurrentAccessException("다른 라이더가 이미 이 주문을 처리 중입니다.");
        }
    }

    public RiderLocationResponseDto getRiderLocation(Long riderId) {
        String address = redisTemplate.opsForValue().get("rider:" + riderId + ":address");
        if (address == null) {
            throw new NotFoundException("유효하지 않은 접근입니다");
        }
        return toRiderLocationResponseDto(address);
    }
}
