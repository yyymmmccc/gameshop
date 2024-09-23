package com.project.game.service;

import com.project.game.dto.request.auth.user.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface UserAuthService {
    ResponseEntity<?> sendEmailAuthentication(SendEmailAuthenticationRequestDto dto);
    ResponseEntity<?> checkAuthentication(CheckAuthenticationRequestDto dto);
    ResponseEntity<?> checkPassword(CheckPasswordRequestDto dto);
    ResponseEntity<?> checkNickname(@Valid CheckNicknameRequestDto dto);
    ResponseEntity<?> checkTel(CheckTelRequestDto dto);
    ResponseEntity<?>join(JoinRequestDto dto);
    ResponseEntity<?> login(UserLoginRequestDto dto);
    ResponseEntity<?> refreshToken(String refreshToken);
    ResponseEntity<?> logout(String refreshToken);
    ResponseEntity<?> oauthJoin(OAuthJoinRequestDto dto);
    ResponseEntity<?> findEmail(FindEmailRequestDto dto);
    ResponseEntity<?> findPassword(FindPasswordRequestDto dto);
    ResponseEntity<?>postNewPassword(NewPasswordRequestDto dto);

    ResponseEntity<?> emailDuplicateCheck(CheckEmailRequestDto dto);
}
