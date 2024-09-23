package com.project.game.controller.swagger;

import com.project.game.dto.request.auth.admin.AdminLoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "관리자 - 인증", description = "관리자 로그인, 로그아웃 API")
public interface SwaggerAdminAuthApi {

    @Operation(summary = "관리자 로그인", description = "관리자가 로그인하는 API")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/login")
    ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginRequestDto dto);

    @Operation(summary = "관리자 로그아웃", description = "관리자가 로그아웃하는 API")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/logout")
    ResponseEntity<?> adminLogout(HttpServletRequest request);
}
