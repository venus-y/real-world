package com.example.realworld.domain.user.exception;

import com.example.realworld.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends CustomException {

    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
