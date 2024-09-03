package com.project.game.controller.swagger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유저 - 내 게임", description = "구매 상품 조회, 삭제 및 실행 API")
@RequestMapping("/api/library")
public interface SwaggerLibraryApi {

    @Operation(summary = "구매 게임목록 조회", description = "사용자의 구매한 게임 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음")
    @GetMapping("/list")
    ResponseEntity getLibraryList(@AuthenticationPrincipal String email);
}
