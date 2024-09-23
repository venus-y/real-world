package com.example.realworld.domain.payment.entity;

import lombok.Getter;

@Getter
public enum PayType {

    CREDIT("신용카드"), AUTOMATIC("자동이체");

    private final String name;

    private PayType(String name) {
        this.name = name;
    }
}
