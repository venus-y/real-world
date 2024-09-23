package com.example.realworld.repository;

import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
//@WebMvcTest
//@DataJpaTest
public class RepoTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void insert() {
//    RegisterDto registerDto = new RegisterDto();
//    registerDto.setUsername("venus");
//    registerDto.setPassword("geum0830");
//    registerDto.setEmail("ex@.com");
//    userRepository.save(toEntity(registerDto));
    }


    @Test
    public void findByUsername() {
        Optional<User> venus = userRepository.findByUsername("venus");
        System.out.println(venus.orElseThrow());
    }
}
