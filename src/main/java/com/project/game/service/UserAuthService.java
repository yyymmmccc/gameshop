package com.project.game.service;

import com.project.game.dto.request.auth.user.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserAuthService {
    ResponseEntity sendEmailAuthentication(SendEmailAuthenticationRequestDto dto);
    ResponseEntity checkAuthentication(HttpSession session, CheckAuthenticationRequestDto dto);
    ResponseEntity checkPassword(CheckPasswordRequestDto dto);
    ResponseEntity checkNickname(@RequestParam(name = "nickname") String nickname);
    ResponseEntity checkTel(@RequestParam(name = "tel") String tel);
    ResponseEntity join(HttpSession session, JoinRequestDto dto);
    ResponseEntity login(UserLoginRequestDto dto);
    ResponseEntity refreshToken(String refreshToken);
    ResponseEntity logout(String refreshToken);
    ResponseEntity oauthJoin(OAuthJoinRequestDto dto);
    ResponseEntity findEmail(FindEmailRequestDto dto);
    ResponseEntity findPassword(FindPasswordRequestDto dto);
    ResponseEntity postNewPassword(NewPasswordRequestDto dto);

    ResponseEntity<?> emailDuplicateCheck(String dto);
}
