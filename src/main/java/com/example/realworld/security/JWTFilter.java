package com.example.realworld.security;

import com.example.realworld.common.ROLE;
import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.userdetails.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if(jwtUtil.isEmpty(authorization)) {
            log.info("===토큰이 비어있습니다.===");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("===인증을 시작합니다.===");

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            log.info("===토큰이 만료되었습니다.===");
            filterChain.doFilter(request, response);

            return;
        }

        User user = User.builder()
                .username(jwtUtil.getUsername(token))
                .password("temp")
                .role(ROLE.valueOf(jwtUtil.getRole(token)))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        filterChain.doFilter(request,response);
    }
}
