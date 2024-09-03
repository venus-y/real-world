package com.example.realworld.domain.user.entity;

import com.example.realworld.common.Address;
import com.example.realworld.common.ROLE;
import com.example.realworld.domain.user.dto.MemberRegisterDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.realworld.common.Address.toAddress;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private ROLE role;
    private Address address;

    public static User toUserEntity(MemberRegisterDto memberRegisterDto) {
        User user = new User();
        user.username = memberRegisterDto.getUsername();
        user.password = memberRegisterDto.getPassword();
        user.email = memberRegisterDto.getEmail();
        user.address = toAddress(
                memberRegisterDto.getStreet(),
                memberRegisterDto.getCity(),
                memberRegisterDto.getZipcode());
        user.role = ROLE.ROLE_USER;
        return user;
    }

//    public void changeUsername(String username) {
//        this.username = username;
//    }
//
//    public void changePassword(String password) {
//        this.password = password;
//    }
//
//    public void changeEmail(String email) {
//        this.email = email;
//    }
//
//    public void changeRole(ROLE role) {
//        this.role = role;
//    }
}
