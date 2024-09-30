package com.example.realworld.domain.user.service;

import com.example.realworld.domain.user.dto.UserRegisterDto;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.AlreadyExistsException;
import com.example.realworld.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.realworld.domain.user.entity.User.toUserEntity;


@Service
@RequiredArgsConstructor
public class UserService {

    private static final String FCM_TOKEN_KEY_PREFIX = "fcm:token:";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long insert(@Valid UserRegisterDto userRegisterDto) {

        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
            throw new AlreadyExistsException(userRegisterDto.getUsername() + "은 이미 존재하는 회원입니다.");
        } else {
            userRegisterDto.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
            User user = toUserEntity(userRegisterDto);
            userRepository.save(user);

            if (userRegisterDto.getFcmToken() != null && !userRegisterDto.getFcmToken().isEmpty()) {
                saveFcmToken(user.getId(), userRegisterDto.getFcmToken());
            }

            return user.getId();
        }
    }

    public void saveFcmToken(Long userId, String fcmToken) {
        String key = Long.toString(userId);
        redisTemplate.opsForSet().add(key, fcmToken);
    }

}
