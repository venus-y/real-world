package com.example.realworld.domain.order.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {

    @NotNull
    private List<OrderMenuRequestDto> menuRequestDtoList;

    @NotNull
    private Long shopId;

    @NotEmpty
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s,.-]{3,100}$", message = "거리 주소는 3자에서 100자 사이의 한글, 영문자, 숫자, 공백, 쉼표, 점, 하이픈만 포함할 수 있습니다.")
    private String street;

    @NotEmpty
    @Pattern(regexp = "^[가-힣\\s]{2,50}$", message = "도시 이름은 2자에서 50자 사이의 한글과 공백만 포함할 수 있습니다.")
    private String city;

    @NotEmpty
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String zipcode;

}
