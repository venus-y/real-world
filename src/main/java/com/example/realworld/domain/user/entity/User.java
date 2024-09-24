package com.example.realworld.domain.user.entity;

import com.example.realworld.common.Address;
import com.example.realworld.common.BaseTimeEntity;
import com.example.realworld.domain.order.entity.Order;
import com.example.realworld.domain.payment.entity.Payment;
import com.example.realworld.domain.shop.entity.Shop;
import com.example.realworld.domain.user.dto.MemberRegisterDto;
import jakarta.persistence.ElementCollection;
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
    @ElementCollection
    private List<String> fcmTokenList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Shop> shopList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Payment> payments = new ArrayList<>();

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
        user.fcmTokenList.add(memberRegisterDto.getFcmToken());
        return user;
    }

}
