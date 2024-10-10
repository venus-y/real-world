package com.example.realworld.domain.order.exception;

import com.example.realworld.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OrderAlreadyAssigedException extends CustomException {

    public OrderAlreadyAssigedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
