package com.project.game.filter;

import com.project.game.entity.UserEntity;
import com.project.game.provider.JwtProvider;
import com.project.game.repository.UserRepository;
import com.project.game.service.Impl.RedisServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.extractAccessToken(request); // 사용자에 요청에서 token을 꺼냄
        log.info("헬로우"+ accessToken);

        if (accessToken == null) {
            filterChain.doFilter(request, response); // 토큰이 없으면 다음 필터로 이동
            return;
        }

        String email = jwtProvider.accessValidate(accessToken); // 토큰 유효성 검사 및 이메일 추출
        if (email == null) {
            filterChain.doFilter(request, response); // 토큰이 유효하지 않으면 다음 필터로 이동
            return;
        }


        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
        // 인증된 사용자의 정보를 저장

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        // 시큐리티 컨텍스트에 사용자의 인증 정보를 저장

        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }

}