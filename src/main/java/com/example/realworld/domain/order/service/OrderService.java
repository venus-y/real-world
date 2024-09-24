package com.example.realworld.domain.order.service;

import com.example.realworld.domain.fcm.service.FcmService;
import com.example.realworld.domain.menu.entity.Menu;
import com.example.realworld.domain.menu.repository.MenuRepository;
import com.example.realworld.domain.order.dto.OrderMenuRequestDto;
import com.example.realworld.domain.order.dto.OrderRequestDto;
import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.order.entity.OrderMenu;
import com.example.realworld.domain.order.entity.OrderStatus;
import com.example.realworld.domain.order.repository.OrderMenuRepository;
import com.example.realworld.domain.order.repository.OrderRepository;
import com.example.realworld.domain.payment.entity.PayType;
import com.example.realworld.domain.payment.entity.Payment;
import com.example.realworld.domain.payment.repository.PaymentRepository;
import com.example.realworld.domain.shop.entity.Shop;
import com.example.realworld.domain.shop.repository.ShopRepository;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.NotFoundException;
import com.example.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.realworld.domain.order.entity.OrderMenu.toOrderMenu;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ShopRepository shopRepository;
    private final FcmService fcmService;

    @Transactional
    public Long processOrder(OrderRequestDto orderRequestDto, String username) {

        User findUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(username + "은 존재하지 않는 회원입니다."));

        List<OrderMenuRequestDto> menuRequestDtoList = orderRequestDto.getMenuRequestDtoList();

        double totalPrice = menuRequestDtoList
                .stream()
                .mapToDouble(OrderMenuRequestDto::calculatePrice)
                .sum();

        Payment payment = Payment.builder()
                .user(findUser)
                .paymentAmount(totalPrice)
                .deliveryCost(Payment.calculateDeliveryFee(totalPrice))
                .payType(PayType.AUTOMATIC)
                .build();

        paymentRepository.save(payment);

        Order order = Order.builder()
                .user(findUser)
                .orderStatus(OrderStatus.CREATED)
                .build();

        Order savedOrder = orderRepository.save(order);


        List<Long> menuIds = menuRequestDtoList
                .stream()
                .map(OrderMenuRequestDto::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menuList = menuRepository.findAllById(menuIds);

        Map<Long, Menu> menuMap = menuList
                .stream().
                collect(Collectors.toMap(Menu::getId, menu -> menu));


        List<OrderMenu> orderMenuList = menuRequestDtoList
                .stream()
                .map(om -> {
                            Menu menu = menuMap.get(om.getMenuId());
                            if (menu == null) {
                                throw new NotFoundException("메뉴가 존재하지 않습니다.");
                            }
                            return toOrderMenu(om.getCount(), order, menu);
                        }
                ).toList();

        orderMenuRepository.saveAll(orderMenuList);

        Shop shop = shopRepository.
                findById(orderRequestDto.getShopId()).orElseThrow(() -> new NotFoundException("존재하지 않는 상접입니다."));

        String ownerName = shop
                .getUser()
                .getUsername();

        fcmService.sendMessageToOwners(ownerName);


        // 결제 Entity


        return savedOrder.getId();
    }
}
