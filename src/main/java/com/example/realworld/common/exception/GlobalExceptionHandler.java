package com.example.realworld.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<List<String>> handleCustomException(CustomException e) {
        List<String> messageList = List.of(e.getMessage());
        return new ResponseEntity<>(messageList, e.getHttpStatus());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Response> handleInputException(BindException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<Response.ErrorField> errorFields = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorFields.add(new Response.ErrorField(fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
        }

        return ResponseEntity
                .badRequest()
                .body(new Response("Bind Exception", errorFields));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        String message = e.getMessage();
        return ResponseEntity
                .internalServerError()
                .body(message);
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private String message;
        private List<ErrorField> errors;

        @AllArgsConstructor
        @Getter
        public static class ErrorField {
            private Object value;
            private String message;
        }

    }
}
