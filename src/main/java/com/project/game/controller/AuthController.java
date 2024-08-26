package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAuthApi;
import com.project.game.dto.request.auth.*;
import com.project.game.dto.response.auth.TokenResponseDto;
import com.project.game.provider.JwtProvider;
import com.project.game.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃, 리프레시 토큰 발급")

public class AuthController implements SwaggerAuthApi {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/email-authentication")
    public ResponseEntity sendEmailAuthentication(@RequestBody @Valid SendEmailAuthenticationRequestDto dto) {

        return authService.sendEmailAuthentication(dto);
    }
    @PostMapping("/check-authentication")
    public ResponseEntity checkAuthentication(HttpSession session, @RequestBody @Valid CheckAuthenticationRequestDto dto) {

        return authService.checkAuthentication(session, dto);
    }
    @PostMapping("/check-password")
    public ResponseEntity checkPassword(@RequestBody @Valid CheckPasswordRequestDto dto) {

        return authService.checkPassword(dto);
    }
    @PostMapping("/check-nickname")
    public ResponseEntity checkNickname(@RequestBody @Valid CheckNicknameRequestDto dto) {

        return authService.checkNickname(dto);
    }
    @PostMapping("/check-tel")
    public ResponseEntity checkTel(@RequestBody @Valid CheckTelRequestDto dto) {

        return authService.checkTel(dto);
    }
    @PostMapping("/join")
    public ResponseEntity join(HttpSession session, @RequestBody @Valid JoinRequestDto dto) {

        return authService.join(session, dto);
    }
    @PostMapping("/oauth-join")
    public ResponseEntity oauthJoin(@RequestBody @Valid OAuthJoinRequestDto dto) {

        return authService.oauthJoin(dto);
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto dto) {

        return authService.login(dto);
    }
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        String refreshToken = jwtProvider.extractRefreshToken(request);
        return authService.logout(refreshToken);
    }
    @PostMapping("/find-email")
    public ResponseEntity findEmail(@RequestBody @Valid FindEmailRequestDto dto) {

        return authService.findEmail(dto);
    }
    @PostMapping("/find-password")
    public ResponseEntity findPassword(@RequestBody @Valid FindPasswordRequestDto dto) {

        return authService.findPassword(dto);
    }
    @GetMapping("/reset-token")
    public ResponseEntity validPasswordResetToken(@RequestParam(name = "token") String token) {

        return authService.validPasswordResetToken(token);
    }
    @PostMapping("/new-password")
    public ResponseEntity postNewPassword(@RequestBody @Valid NewPasswordRequestDto dto) {

        return authService.postNewPassword(dto);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity refreshToken(HttpServletRequest request) {

        String refreshToken = jwtProvider.extractRefreshToken(request);
        return authService.refreshToken(refreshToken);
    }
}