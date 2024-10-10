package com.example.realworld.domain.delivery.exception;

import com.example.realworld.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ConcurrentAccessException extends CustomException {

    public ConcurrentAccessException(String message) {
        super(message, HttpStatus.LOCKED);
    }
}
