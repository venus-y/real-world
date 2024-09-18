package com.example.realworld.repository;

import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;



@SpringBootTest
//@WebMvcTest
//@DataJpaTest
public class RepoTest {
    @Autowired
    UserRepository userRepository;



}
