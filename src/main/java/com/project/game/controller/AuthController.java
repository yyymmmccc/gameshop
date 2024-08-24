package com.project.game.controller;

import com.project.game.dto.request.auth.*;
import com.project.game.provider.JwtProvider;
import com.project.game.service.AuthService;
import com.project.game.service.Impl.RedisServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃, 리프레시 토큰 발급")

public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;


    @Operation(summary = "이메일 인증", description = "회원가입 이메일 중복체크 후 인증번호 발송")
    @PostMapping("/email-authentication") // 이메일에 인증 누르기 -> 중복되면 중복된다 -> 안되면 이메일 전송
    public ResponseEntity sendEmailAuthentication(@RequestBody @Valid SendEmailAuthenticationRequestDto dto){

        return authService.sendEmailAuthentication(dto);
    }

    @Operation(summary = "인증번호 체크", description = "회원가입 인증번호 전송 후 받은 인증번호 체크")
    @PostMapping("/check-authentication")
    public ResponseEntity checkAuthentication(@RequestBody @Valid CheckAuthenticationRequestDto dto){

        return authService.checkAuthentication(dto);
    }

    @Operation(summary = "비밀번호 체크", description = "회원가입 비밀번호, 비밀번호 확인 두 개가 일치하는지 체크")
    @PostMapping("/check-password")
    public ResponseEntity checkPassword(@RequestBody @Valid CheckPasswordRequestDto dto){

        return authService.checkPassword(dto);
    }

    @Operation(summary = "닉네임 체크", description = "회원가입 닉네임 중복되는지 체크")
    @PostMapping("/check-nickname")
    public ResponseEntity checkNickname(@RequestBody @Valid CheckNicknameRequestDto dto){

        return authService.checkNickname(dto);
    }
    @Operation(summary = "전화번호 체크", description = "회원가입 전화번호 중복되는지 체크")
    @PostMapping("/check-tel")
    public ResponseEntity checkTel(@RequestBody @Valid CheckTelRequestDto dto){

        return authService.checkTel(dto);
    }
    @Operation(summary = "회원가입", description = "회원가입에 필요한 모든 정보를 입력 후 체크하고 저장하는 API")
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinRequestDto dto){

        return authService.join(dto);
    }
    @Operation(summary = "OAuth 회원가입", description = "OAuth 로그인 후 회원가입에 필요한 추가 정보를 저장하는 API")
    @PostMapping("/oauth-join")
    public ResponseEntity oauthJoin(@RequestBody @Valid OAuthJoinRequestDto dto) {

        return authService.oauthJoin(dto);
    }
    @Operation(summary = "로그인", description = "사용자가 로그인 하는 API")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto dto){

        return authService.login(dto);
    }
    @Operation(summary = "로그아웃", description = "사용자가 로그아웃 하는 API")
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){

        String refreshToken = jwtProvider.extractRefreshToken(request);

        return authService.logout(refreshToken);
    }
    @Operation(summary = "이메일 찾기", description = "사용자가 이메일(아이디)를 잊어버려서 찾는 API")
    @PostMapping("/find-email")
    public ResponseEntity findEmail(@RequestBody @Valid FindEmailRequestDto dto){
        return authService.findEmail(dto);
    }

    @Operation(summary = "비밀번호 찾기 메일 전송", description = "사용자가 비밀번호를 잊어버려서 찾는 API")
    @PostMapping("/find-password")
    public ResponseEntity findPassword(@RequestBody @Valid FindPasswordRequestDto dto){
        return authService.findPassword(dto);
        // 1. 이메일 인증번호를 전송 후 -> 인증번호가 맞으면 -> 새로운 비밀번호창으로
        // 2. 이메일로 비밀번호 재설정 버튼 전송 -> 버튼 누르면 재설정 버튼
    }

    @Operation(summary = "비밀번호 리셋 토큰 검증", description = "메일을 통해 받은 비밀번호 재설정 버튼을 눌렀을 시 토큰 검증")
    @GetMapping("/reset-token")
    public ResponseEntity validPasswordResetToken(@RequestParam(name = "token") String token){

        return authService.validPasswordResetToken(token);
    }

    @Operation(summary = "비밀번호 찾기 변경", description = "토큰 검증이 완료된 후 비밀빈호 변경")
    @PostMapping("/new-password")
    public ResponseEntity postNewPassword(@RequestBody @Valid NewPasswordRequestDto dto){

        return authService.postNewPassword(dto);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰 만료직전 리프레시 토큰으로 요청하여 액세스 토큰 재발급")
    @PostMapping("/refresh-token")
    public ResponseEntity refreshToken(HttpServletRequest request){

        String refreshToken = jwtProvider.extractRefreshToken(request);

        return authService.refreshToken(refreshToken);
    }
}
