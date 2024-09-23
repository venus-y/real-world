package com.example.realworld.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageRequestDto {
    private String title;
    private String body;
    private String targetToken;
}
