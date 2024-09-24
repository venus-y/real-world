package com.example.realworld.domain.user.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MemberRegisterDto {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z\\d]{8,12}$", message = "아이디는 8자에서 12자 사이의 영문자와 숫자로 구성되어야 합니다.")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[a-z\\d@$!%*?&#]{8,}$", message = "비밀번호는 최소 8자 이상이며, 소문자, 숫자, 특수 문자를 각각 하나 이상 포함해야 합니다.")
    private String password;

    @NotEmpty
    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s,.-]{3,100}$", message = "거리 주소는 3자에서 100자 사이의 한글, 영문자, 숫자, 공백, 쉼표, 점, 하이픈만 포함할 수 있습니다.")
    private String street;

    @NotEmpty
    @Pattern(regexp = "^[가-힣\\s]{2,50}$", message = "도시 이름은 2자에서 50자 사이의 한글과 공백만 포함할 수 있습니다.")
    private String city;

    @NotEmpty
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String zipcode;

    @Nullable
    private String fcmToken;


}
