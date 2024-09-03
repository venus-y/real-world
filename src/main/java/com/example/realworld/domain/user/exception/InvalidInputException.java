package com.example.realworld.domain.user.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }
}
