package com.project.game.global.filter;

import com.project.game.global.provider.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtProvider.extractAccessToken(request); // 사용자 요청에서 토큰 추출
        log.info("오류 엑세스 토큰 테스트 : " + accessToken);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtProvider.accessValidate(accessToken); // 토큰 유효성 검사
        log.info("claims 테스트 " + claims);
        if (claims == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String role = claims.get("role", String.class);
        String email = claims.get("email", String.class);

        if (role == null || email == null) {
            filterChain.doFilter(request, response);
            return;
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken); // 인증된 사용자 정보를 저장

        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }
}
