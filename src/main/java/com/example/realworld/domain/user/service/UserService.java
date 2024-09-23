package com.example.realworld.domain.user.service;

import com.example.realworld.domain.user.dto.MemberRegisterDto;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.AlreadyExistsException;
import com.example.realworld.domain.user.repository.UserRepository;
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
    public Long insert(MemberRegisterDto memberRegisterDto) {

        if (userRepository.existsByUsername(memberRegisterDto.getUsername())) {
            throw new AlreadyExistsException(memberRegisterDto.getUsername() + "은 이미 존재하는 회원입니다.");
        } else {
            memberRegisterDto.setPassword(bCryptPasswordEncoder.encode(memberRegisterDto.getPassword()));
            User user = toUserEntity(memberRegisterDto);
            userRepository.save(user);

            if (memberRegisterDto.getFcmToken() != null && !memberRegisterDto.getFcmToken().isEmpty()) {
                saveFcmToken(memberRegisterDto.getUsername(), memberRegisterDto.getFcmToken());
            }

            return user.getId();
        }
    }

    public void saveFcmToken(String username, String fcmToken) {
        String key = FCM_TOKEN_KEY_PREFIX + username;
        redisTemplate.opsForSet().add(key, fcmToken);
    }

}
