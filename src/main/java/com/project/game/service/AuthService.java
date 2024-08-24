package com.project.game.service;

import com.project.game.dto.request.auth.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity sendEmailAuthentication(SendEmailAuthenticationRequestDto dto);
    ResponseEntity checkAuthentication(CheckAuthenticationRequestDto dto);
    ResponseEntity checkPassword(CheckPasswordRequestDto dto);
    ResponseEntity checkNickname(CheckNicknameRequestDto dto);
    ResponseEntity checkTel(CheckTelRequestDto dto);
    ResponseEntity join(JoinRequestDto dto);
    ResponseEntity login(LoginRequestDto dto);
    ResponseEntity refreshToken(String refreshToken);
    ResponseEntity logout(String refreshToken);
    ResponseEntity oauthJoin(OAuthJoinRequestDto dto);

    ResponseEntity findEmail(FindEmailRequestDto dto);

    ResponseEntity findPassword(FindPasswordRequestDto dto);

    ResponseEntity validPasswordResetToken(String token);

    ResponseEntity postNewPassword(NewPasswordRequestDto dto);
}
