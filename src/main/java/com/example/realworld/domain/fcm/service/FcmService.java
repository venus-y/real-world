package com.example.realworld.domain.fcm.service;

import com.example.realworld.domain.user.exception.NotFoundException;
import com.example.realworld.domain.user.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class FcmService {

    private static final String FCM_TOKEN_KEY_PREFIX = "fcm:token:";
    private static final int BATCH_SIZE = 500;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;


    @Value("${spring.fcm.service-account-file}")
    private String serviceAccountFilePath;

    @Value("${spring.fcm.topic-name}")
    private String topicName;

    @Value("${spring.fcm.project-id}")
    private String projectId;

    @PostConstruct
    public void initialize() throws IOException, IOException {
        //Firebase 프로젝트 정보를 FireBaseOptions에 입력해준다.
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
                .setProjectId(projectId)
                .build();

        //입력한 정보를 이용하여 initialze 해준다.
        FirebaseApp.initializeApp(options);
    }

    public void sendMessageByToken(String title, String body, String username) {
        String fcmToken = getFcmToken(username);

        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(fcmToken)
                    .build());
        } catch (FirebaseMessagingException e) {
            throw new CompletionException("메세지 전송에 실패했습니다.", e.getCause());
        }
    }


    // 라이더에게 메시지 발송 시 사용
//    public void sendMessageToRiders(String userName) {
//
//        ArrayList<String> fcmTokens = getFcmTokens(userName);
//
//        ArrayList<CompletableFuture<BatchResponse>> futures = new ArrayList<>();
//
//        for (int i = 0; i < fcmTokens.size(); i += BATCH_SIZE) {
//            List<String> batchList = fcmTokens.subList(i, Math.min(fcmTokens.size(), i + BATCH_SIZE));
//            futures.add(sendMulticastMessageAsync("테스트제목", "테스트바디", batchList));
//        }
//
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//
//        int totalSuccess = futures.stream()
//                .mapToInt(f -> {
//                    try {
//                        return f.join()
//                                .getSuccessCount();
//                    } catch (CompletionException e) {
//                        throw e;
//                    }
//                }).sum();
//
//    }

    public String getFcmToken(String userName) {
        String key = userName;

        Set<String> members = redisTemplate.opsForSet().members(key);


        if (!members.isEmpty()) {
            String token = members.stream()
                    .findFirst()
                    .get();

            return token;
        }

        throw new NotFoundException("등록된 기기가 존재하지 않습니다");
    }

    public List<String> getMultipleFcmTokens(List<String> userIds) {


        List<String> fcmTokens
                = userIds.stream()
                .map(userId -> redisTemplate.opsForSet().members(userId))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .toList();

        if (!fcmTokens.isEmpty()) {
            return fcmTokens;
        }

        throw new NotFoundException("등록된 기기가 존재하지 않습니다");
    }

    @Async
    public CompletableFuture<BatchResponse> sendMulticastMessageAsync(String title, String body, List<String> tokens) {
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .addAllTokens(tokens)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return FirebaseMessaging
                        .getInstance()
                        .sendEachForMulticastAsync(message).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new CompletionException("Failed to send multicast message", e.getCause());
            }
        });

    }


}
