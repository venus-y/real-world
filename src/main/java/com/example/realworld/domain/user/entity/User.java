package com.example.realworld.domain.user.entity;

import com.example.realworld.common.Address;
import com.example.realworld.common.BaseTimeEntity;
import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.payment.entity.Payment;
import com.example.realworld.domain.shop.entity.Shop;
import com.example.realworld.domain.user.dto.UserRegisterDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.realworld.common.Address.toAddress;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private Address address;
    @OneToMany(mappedBy = "user")
    private List<Order> orderList = new ArrayList<>();
    @Nullable
    private String fcmToken;
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Shop> shopList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Payment> payments = new ArrayList<>();

    public User(Long id) {
        this.id = id;
    }

    public static User toUserEntity(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.username = userRegisterDto.getUsername();
        user.password = userRegisterDto.getPassword();
        user.email = userRegisterDto.getEmail();
        user.address = toAddress(
                userRegisterDto.getStreet(),
                userRegisterDto.getCity(),
                userRegisterDto.getZipcode());
        user.role = ROLE.ROLE_USER;
        user.fcmToken = (userRegisterDto.getFcmToken());
        return user;
    }
}
