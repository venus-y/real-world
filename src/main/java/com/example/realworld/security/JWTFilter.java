package com.example.realworld.security;

import com.example.realworld.domain.user.entity.User;
import com.example.realworld.domain.user.repository.UserRepository;
import com.example.realworld.domain.user.userdetails.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (jwtUtil.isEmpty(authorization)) {
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

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);


        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/login", "/users/"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::equals);
    }
}
