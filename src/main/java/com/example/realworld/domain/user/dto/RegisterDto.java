package com.example.realworld.domain.user.dto;


import com.example.realworld.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterDto {

    private String username;
    private String email;
    private String password;

    public static User toEntity(RegisterDto dto){
        User user = new User();
        user.changeUsername(dto.username);
        user.changePassword(dto.password);
        user.changeEmail(dto.getEmail());
        return user;
    }
}
