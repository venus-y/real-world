package com.example.realworld.domain.order.exception;

import com.example.realworld.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ForbiddenOrderApprovalActionException extends CustomException {
    public ForbiddenOrderApprovalActionException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
