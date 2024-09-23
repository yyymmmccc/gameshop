package com.project.game.controller.swagger;

import com.project.game.dto.request.auth.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "유저 - 인증", description = "유저 회원가입, 로그인, 로그아웃 등")
public interface SwaggerAuthApi {

    @Operation(summary = "이메일 인증", description = "회원가입 이메일 중복체크 후 인증번호 발송")
    @ApiResponse(responseCode = "200", description = "인증번호 발송 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "409", description = "중복된 이메일")
    @PostMapping("/email-authentication")
    ResponseEntity sendEmailAuthentication(@RequestBody @Valid SendEmailAuthenticationRequestDto dto);

    @Operation(summary = "인증번호 체크", description = "회원가입 인증번호 전송 후 받은 인증번호 체크")
    @ApiResponse(responseCode = "200", description = "인증번호 체크 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증번호 실패")
    @PostMapping("/check-authentication")
    ResponseEntity checkAuthentication(@RequestBody @Valid CheckAuthenticationRequestDto dto);

    @Operation(summary = "비밀번호 체크", description = "회원가입 비밀번호, 비밀번호 확인 두 개가 일치하는지 체크")
    @ApiResponse(responseCode = "200", description = "비밀번호 체크 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "400", description = "비밀번호 불일치")
    @PostMapping("/check-password")
    ResponseEntity checkPassword(@RequestBody @Valid CheckPasswordRequestDto dto);

    @Operation(summary = "닉네임 체크", description = "회원가입 닉네임 중복되는지 체크")
    @ApiResponse(responseCode = "200", description = "닉네임 체크 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "409", description = "중복된 닉네임")
    @GetMapping("/check-nickname")
    ResponseEntity checkNickname(@RequestParam(name = "nickname") String nickname);

    @Operation(summary = "전화번호 체크", description = "회원가입 전화번호 중복되는지 체크")
    @ApiResponse(responseCode = "200", description = "전화번호 체크 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "409", description = "중복된 전화번호")
    @GetMapping("/check-tel")
    ResponseEntity checkTel(@RequestParam(name = "tel") String tel);

    @Operation(summary = "회원가입", description = "회원가입에 필요한 모든 정보를 입력 후 체크하고 저장하는 API")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "409", description = "중복된 이메일, 닉네임, 전화번호")
    @PostMapping("/join")
    ResponseEntity join(@RequestBody @Valid JoinRequestDto dto);

    @Operation(summary = "OAuth 회원가입", description = "OAuth 로그인 후 회원가입에 필요한 추가 정보를 저장하는 API")
    @ApiResponse(responseCode = "200", description = "OAuth 회원가입 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @ApiResponse(responseCode = "409", description = "중복된 닉네임, 전화번호")
    @PostMapping("/oauth-join")
    ResponseEntity oauthJoin(@RequestBody @Valid OAuthJoinRequestDto dto);

    @Operation(summary = "로그인", description = "사용자가 로그인 하는 API")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호 오류")
    @PostMapping("/login")
    ResponseEntity login(@RequestBody @Valid UserLoginRequestDto dto);

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃 하는 API")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "로그아웃 실패")
    @PostMapping("/logout")
    ResponseEntity logout(HttpServletRequest request);

    @Operation(summary = "이메일 찾기", description = "사용자가 이메일(아이디)를 잊어버려서 찾는 API")
    @ApiResponse(responseCode = "200", description = "이메일 찾기 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @PostMapping("/find-email")
    ResponseEntity findEmail(@RequestBody @Valid FindEmailRequestDto dto);

    @Operation(summary = "비밀번호 찾기 메일 전송", description = "사용자가 비밀번호를 잊어버려서 찾는 API")
    @ApiResponse(responseCode = "200", description = "비밀번호 찾기 메일 전송 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping("/find-password")
    ResponseEntity findPassword(@RequestBody @Valid FindPasswordRequestDto dto);

    @Operation(summary = "비밀번호 찾기 변경", description = "토큰 검증이 완료된 후 비밀번호 변경")
    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "비밀번호 리셋 토큰 없음")
    @ApiResponse(responseCode = "409", description = "새 비밀번호가 현재 비밀번호와 동일함")
    @PostMapping("/new-password")
    ResponseEntity postNewPassword(@RequestBody @Valid NewPasswordRequestDto dto);

    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰 만료직전 리프레시 토큰으로 요청하여 액세스 토큰 재발급")
    @ApiResponse(responseCode = "200", description = "액세스 토큰 재발급 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "리프레시 토큰 없음")
    @ApiResponse(responseCode = "401", description = "사용자 인증 실패")
    @PostMapping("/refresh-token")
    ResponseEntity refreshToken(HttpServletRequest request);
}

