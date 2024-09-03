package com.project.game.global.handler;


import com.project.game.oauth2.CustomOAuth2User;
import com.project.game.global.provider.JwtProvider;
import com.project.game.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
        if (oAuth2User.getRole().equals("ROLE_GUEST")) {
            String accessToken = jwtProvider.createAccessToken(oAuth2User.getEmail(), oAuth2User.getRole());

            response.sendRedirect("http://localhost:3000/auth/oauth-join/" + accessToken); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
        }

        else {
            loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {

        String accessToken = jwtProvider.createAccessToken(oAuth2User.getEmail(), oAuth2User.getRole());
        String refreshToken = jwtProvider.createRefreshToken();

        redisService.setValues(refreshToken, oAuth2User.getEmail(), Duration.ofDays(14));

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Authorization-Refresh", "Bearer " + refreshToken);

    }
}
