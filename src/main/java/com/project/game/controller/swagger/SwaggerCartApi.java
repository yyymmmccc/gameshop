package com.project.game.controller.swagger;

import com.project.game.dto.request.cart.CartDeleteRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "유저 장바구니", description = "게임 상품 장바구니에 생성, 조회, 삭제 API")
public interface SwaggerCartApi {

    @Operation(summary = "장바구니에 게임 추가", description = "선택한 게임을 장바구니에 추가하는 API")
    @ApiResponse(responseCode = "200", description = "장바구니 추가 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "사용자 또는 게임을 찾을 수 없음")
    @ApiResponse(responseCode = "409", description = "중복된 장바구니 또는 주문")
    @PostMapping("/{gameId}")
    ResponseEntity postCart(@PathVariable("gameId") int gameId,
                            @AuthenticationPrincipal String email);

    @Operation(summary = "장바구니 목록 조회", description = "사용자의 장바구니 목록을 조회하는 API")
    @ApiResponse(responseCode = "200", description = "장바구니 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @GetMapping("/list")
    ResponseEntity getCarts(@AuthenticationPrincipal String email);

    @Operation(summary = "장바구니에서 게임 삭제", description = "선택한 게임을 장바구니에서 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "장바구니 삭제 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "사용자 또는 장바구니 항목을 찾을 수 없음")
    @DeleteMapping("")
    ResponseEntity deleteCart(@RequestBody @Valid CartDeleteRequestDto dto,
                              @AuthenticationPrincipal String email);
}
