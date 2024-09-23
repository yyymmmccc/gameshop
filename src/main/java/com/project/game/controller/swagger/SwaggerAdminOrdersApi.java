package com.project.game.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "관리자 - 결제", description = "관리자 주문 취소 요청 조회 API")
public interface SwaggerAdminOrdersApi {

    @Operation(summary = "취소 요청 목록 조회", description = "취소된 상품 주문 목록을 조회하는 API")
    @ApiResponse(responseCode = "200", description = "취소 요청 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/cancel-list")
    ResponseEntity<?> getProductCancelRequestList();
}
