package com.project.game.controller;

import com.project.game.dto.request.auth.admin.AdminLoginRequestDto;
import com.project.game.global.provider.JwtProvider;
import com.project.game.service.AdminAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(name = "관리자 - 인증", description = "관리자 로그인, 로그아웃")
@Slf4j
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginRequestDto dto){

        return adminAuthService.adminLogin(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> adminLogout(HttpServletRequest request){

        String refreshToken = jwtProvider.extractRefreshToken(request);
        return adminAuthService.adminLogout(refreshToken);
    }
}
