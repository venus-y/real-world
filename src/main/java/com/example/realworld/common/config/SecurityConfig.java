package com.example.realworld.common.config;

import com.example.realworld.domain.user.repository.UserRepository;
import com.example.realworld.security.JWTFilter;
import com.example.realworld.security.JWTUtil;
import com.example.realworld.security.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PERMIT_ALL_OPERATIONS = {"/login", "/", "/users/", "/swagger-ui/**", "/v3/api-docs/**", "/error"};
    private static final String[] ADMIN_OWNER_OPERATIONS = {"/owners/**", "/shops/**", "/orders/{orderId}/approval"};
    private final AuthenticationConfiguration configuration;
    private final JWTUtil jwtUtil;
    private final AuthenticationEntryPoint entryPoint;
    private final AccessDeniedHandler deniedHandler;
    private final UserRepository userRepository;

    @Bean

    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/test").hasRole("ADMIN")
                        .requestMatchers(PERMIT_ALL_OPERATIONS).permitAll()
                        .requestMatchers(ADMIN_OWNER_OPERATIONS).hasAnyRole("OWNER", "ADMIN")
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil, userRepository), LoginFilter.class);


        http
                .addFilterAt(new LoginFilter(authenticationManager(configuration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
//                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint)
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(deniedHandler);


        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
