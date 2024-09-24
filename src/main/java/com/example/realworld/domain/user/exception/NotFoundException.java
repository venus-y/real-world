package com.example.realworld.domain.user.exception;

import com.example.realworld.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
