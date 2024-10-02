package com.project.game.controller.swagger;

import com.project.game.dto.request.member.user.UserPasswordRequestDto;
import com.project.game.dto.request.member.user.UserUpdateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.security.Principal;

@Tag(name = "유저 - 마이페이지", description = "회원정보 수정, 비밀번호 변경, 회원 탈퇴 API")
@RequestMapping("/api/user")
public interface SwaggerUserApi {

    @Operation(summary = "내 정보", description = "마이페이지 내 정보 불러오기")
    @ApiResponse(responseCode = "200", description = "정보 조회 성공")
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음")
    @GetMapping("")
    ResponseEntity getUser(Principal principal);

    @Operation(summary = "마이페이지 정보 수정", description = "마이페이지에서 내 정보 수정하기")
    @ApiResponse(responseCode = "200", description = "정보 수정 성공")
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음")
    @ApiResponse(responseCode = "409", description = "닉네임 또는 전화번호 중복")
    @PatchMapping("")
    ResponseEntity patchUser(@RequestBody @Valid UserUpdateRequestDto dto,
                             Principal principal);

    @Operation(summary = "마이페이지 비밀번호 수정", description = "마이페이지에서 비밀번호 수정하기")
    @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공")
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음")
    @ApiResponse(responseCode = "400", description = "비밀번호 확인 실패")
    @PatchMapping("/password")
    ResponseEntity patchUserPassword(@RequestBody @Valid UserPasswordRequestDto dto,
                                     Principal principal);

    @Operation(summary = "회원탈퇴", description = "회원탈퇴")
    @ApiResponse(responseCode = "200", description = "회원탈퇴 성공")
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음")
    @DeleteMapping("")
    ResponseEntity deleteUser(Principal principal);
}
