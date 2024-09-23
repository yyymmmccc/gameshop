package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerAuthApi;
import com.project.game.dto.request.auth.user.*;
import com.project.game.global.provider.JwtProvider;
import com.project.game.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
@Slf4j
public class UserAuthController implements SwaggerAuthApi {

    private final UserAuthService userAuthService;
    private final JwtProvider jwtProvider;

    @GetMapping("/email-check")
    public ResponseEntity<?> emailDuplicateCheck(@RequestParam(name = "email") String email){

        return userAuthService.emailDuplicateCheck(email);
    }

    @PostMapping("/email-authentication")
    public ResponseEntity sendEmailAuthentication(@RequestBody @Valid SendEmailAuthenticationRequestDto dto) {

        return userAuthService.sendEmailAuthentication(dto);
    }
    @PostMapping("/check-authentication")
    public ResponseEntity checkAuthentication(@RequestBody @Valid CheckAuthenticationRequestDto dto) {

        return userAuthService.checkAuthentication(dto);
    }
    @PostMapping("/check-password")
    public ResponseEntity checkPassword(@RequestBody @Valid CheckPasswordRequestDto dto) {

        return userAuthService.checkPassword(dto);
    }
    @GetMapping("/check-nickname")
    public ResponseEntity checkNickname(@RequestParam(name = "nickname") String nickname) {

        return userAuthService.checkNickname(nickname);
    }
    @GetMapping("/check-tel")
    public ResponseEntity checkTel(@RequestParam(name = "tel") String tel) {

        return userAuthService.checkTel(tel);
    }
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinRequestDto dto) {

        return userAuthService.join(dto);
    }
    @PostMapping("/oauth-join")
    public ResponseEntity oauthJoin(@RequestBody @Valid OAuthJoinRequestDto dto) {

        return userAuthService.oauthJoin(dto);
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginRequestDto dto) {

        return userAuthService.login(dto);
    }
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        String refreshToken = jwtProvider.extractRefreshToken(request);
        return userAuthService.logout(refreshToken);
    }
    @PostMapping("/find-email")
    public ResponseEntity findEmail(@RequestBody @Valid FindEmailRequestDto dto) {

        return userAuthService.findEmail(dto);
    }
    @PostMapping("/find-password")
    public ResponseEntity findPassword(@RequestBody @Valid FindPasswordRequestDto dto) {

        return userAuthService.findPassword(dto);
    }
    @PostMapping("/new-password")
    public ResponseEntity postNewPassword(@RequestBody @Valid NewPasswordRequestDto dto) {

        return userAuthService.postNewPassword(dto);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity refreshToken(HttpServletRequest request) {

        String refreshToken = jwtProvider.extractRefreshToken(request);
        return userAuthService.refreshToken(refreshToken);
    }
}