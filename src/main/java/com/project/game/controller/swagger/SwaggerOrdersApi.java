package com.project.game.controller.swagger;

import com.project.game.dto.request.order.OrderFormRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유저 - 주문", description = "주문내역 생성, 조회 API")
@RequestMapping("/api/order")
public interface SwaggerOrdersApi {

    @Operation(summary = "주문 페이지 정보 조회", description = "장바구니에서 주문 페이지로 넘어갈 때 보여줄 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "사용자 또는 장바구니 찾을 수 없음")
    @PostMapping("/form")
    ResponseEntity getOrderFormGames(@RequestBody @Valid OrderFormRequestDto dto,
                                     @AuthenticationPrincipal String email);

    @Operation(summary = "주문 목록 조회", description = "사용자의 주문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음")
    @GetMapping("/list")
    ResponseEntity getOrderList(@AuthenticationPrincipal String email);

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "사용자 또는 주문 찾을 수 없음")
    @GetMapping("/order-detail/{orderId}")
    ResponseEntity getOrderDetailList(@AuthenticationPrincipal String email,
                                      @PathVariable("orderId") String orderId);

    /*
    @Operation(summary = "카카오페이 결제 준비", description = "카카오페이를 이용한 결제 준비를 수행합니다.")
    @ApiResponse(responseCode = "200", description = "결제 준비 성공")
    @ApiResponse(responseCode = "500", description = "결제 준비 실패")
    @PostMapping("/pay/ready")
    ResponseEntity payReady(@RequestBody KakaoPayReadyRequestDto dto,
                            @AuthenticationPrincipal String email);

    @Operation(summary = "카카오페이 결제 승인", description = "카카오페이 결제 승인을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "결제 승인 성공")
    @ApiResponse(responseCode = "500", description = "결제 승인 실패")
    @GetMapping("/pay/approve")
    ResponseEntity payApprove(@RequestParam("pg_token") String pgToken,
                              @RequestParam("partner_order_id") String orderId);

    @Operation(summary = "카카오페이 결제 취소", description = "카카오페이 결제를 취소합니다.")
    @ApiResponse(responseCode = "200", description = "결제 취소 성공")
    @ApiResponse(responseCode = "500", description = "결제 취소 실패")
    @GetMapping("/pay/cancel")
    ResponseEntity payCancel(@RequestParam("partner_order_id") String orderId);
    */

}
