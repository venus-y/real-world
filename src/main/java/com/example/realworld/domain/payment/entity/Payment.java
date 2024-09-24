package com.example.realworld.domain.payment.entity;

import com.example.realworld.common.BaseTimeEntity;
import com.example.realworld.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    
    private Double paymentAmount;

    private Integer deliveryCost;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    public static Integer calculateDeliveryFee(Double price) {
        return price >= 50000 ? 0 : 3000;
    }

}
