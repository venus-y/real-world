package com.example.realworld.domain.order.api;

import com.example.realworld.common.aop.CheckBindingErrors;
import com.example.realworld.domain.order.dto.OrderRequestDto;
import com.example.realworld.domain.order.service.OrderService;
import com.example.realworld.domain.user.entity.CurrentUser;
import com.example.realworld.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/")
    @CheckBindingErrors
    public ResponseEntity<Object> register(@Valid @RequestBody OrderRequestDto orderRequestDto, @CurrentUser User user, BindingResult bindingResult) {
        Long orderId = orderService.processOrder(orderRequestDto, user.getUsername());
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/approval")
    public void approve(@PathVariable(name = "orderId") Long orderId, @CurrentUser User user) {
        orderService.approveOrder(orderId, user.getId());

    }

}
