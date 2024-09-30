package com.example.realworld.domain.order.service;

import com.example.realworld.domain.fcm.service.FcmService;
import com.example.realworld.domain.menu.entity.Menu;
import com.example.realworld.domain.menu.repository.MenuRepository;
import com.example.realworld.domain.order.dto.OrderMenuRequestDto;
import com.example.realworld.domain.order.dto.OrderRequestDto;
import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.order.entity.OrderMenu;
import com.example.realworld.domain.order.entity.OrderStatus;
import com.example.realworld.domain.order.exception.ForbiddenOrderApprovalActionException;
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
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ShopRepository shopRepository;
    private final FcmService fcmService;

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

        Shop shop = shopRepository.
                findById(orderRequestDto.getShopId()).orElseThrow(() -> new NotFoundException("존재하지 않는 상점입니다."));

        Order order = Order.builder()
                .user(findUser)
                .orderStatus(OrderStatus.CREATED)
                .shop(shop)
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


        String ownerName = shop
                .getUser()
                .getUsername();

        fcmService.sendMessageByToken("테스트제목", "테스트바디", ownerName);


        // 결제 Entity


        return savedOrder.getId();
    }

    public void approveOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("존재하지 않는 주문정보입니다"));
        User user = order.getShop().getUser();
        if (user.getId().equals(userId)) {
            order.updateOrderStatus(OrderStatus.APPROVED);
            // 주문요청한 회원에게 푸시 메시지 전송
            fcmService.sendMessageByToken("테스트제목", "테스트바디", order.getUser().getUsername());
//            fcmService.sendMessageToRiders();<<<
        } else {
            throw new ForbiddenOrderApprovalActionException("주문을 승인할 권한이 없습니다");
        }
    }
}
