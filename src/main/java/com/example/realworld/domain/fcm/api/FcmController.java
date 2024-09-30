package com.example.realworld.domain.fcm.api;

import com.example.realworld.domain.fcm.dto.MessageRequestDto;
import com.example.realworld.domain.fcm.service.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/message/fcm/token")
    public ResponseEntity<Object> sendMessageToken(@RequestBody MessageRequestDto requestDto) throws IOException, FirebaseMessagingException {
        fcmService.sendMessageByToken(requestDto.getTitle(), requestDto.getBody(), requestDto.getTargetToken());
        return ResponseEntity
                .ok()
                .build();
    }
}
