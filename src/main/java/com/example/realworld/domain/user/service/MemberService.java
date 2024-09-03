package com.example.realworld.domain.user.service;

import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.exception.AlreadyExistsException;
import com.example.realworld.domain.user.dto.MemberRegisterDto;
import com.example.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.realworld.domain.user.entity.User.toUserEntity;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public Long insert(MemberRegisterDto memberRegisterDto) {

        if(userRepository.existsByUsername(memberRegisterDto.getUsername())){
            throw new AlreadyExistsException(memberRegisterDto.getUsername() + "은 이미 존재하는 회원입니다.");
        } else {
            memberRegisterDto.setPassword(bCryptPasswordEncoder.encode(memberRegisterDto.getPassword()));
            User user = toUserEntity(memberRegisterDto);
            userRepository.save(user);

            return user.getId();
        }
    }

}
