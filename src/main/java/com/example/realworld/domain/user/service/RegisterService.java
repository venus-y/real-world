package com.example.realworld.domain.user.service;

import com.example.realworld.domain.user.dto.RegisterDto;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.example.realworld.domain.user.dto.RegisterDto.toEntity;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    // jwt 구현 되면 WebConfig에서 주입 받는 걸로 수정해야 함
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void insert(RegisterDto dto) {
        if(userRepository.existsByUsername(dto.getUsername())){
            return;
        }else {
            User entity = toEntity(dto);
            entity.changePassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            entity.changeRole(User.ROLE.ROLE_USER);
            userRepository.save(entity);
        }
    }

}
