package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerOrdersApi;
import com.project.game.dto.request.order.OrderFormRequestDto;
import com.project.game.dto.request.order.OrderRequestDto;
import com.project.game.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
@Slf4j
public class OrdersController implements SwaggerOrdersApi {

    private final OrdersService ordersService;
//    private final KakaoPayService kakaoPayService;

    // 장바구니 -> 주문페이지 넘어갈 때, 주문 페이지에서 보여줄 정보
    @PostMapping("/form")
    public ResponseEntity getOrderFormGames(@RequestBody @Valid OrderFormRequestDto dto,
                                            @AuthenticationPrincipal String email){

        return ordersService.getOrderFormProduct(dto, email);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequestDto dto){

        return ordersService.createOrder(dto);
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") String orderId){


        return ordersService.cancelOrder(orderId);
    }


    @GetMapping("/list")
    public ResponseEntity getOrderList(@AuthenticationPrincipal String email){

        return ordersService.getOrderList(email);
    }

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity getOrderDetailList(@AuthenticationPrincipal String email,
                                             @PathVariable("orderId") String orderId){

        return ordersService.getOrderDetailList(email, orderId);

    }

    /*
    // 카카오페이 부분
    @PostMapping("/pay/ready")
    public ResponseEntity payReady(@RequestBody KakaoPayReadyRequestDto dto,
                                   @AuthenticationPrincipal String email) {

        // 카카오 결제 준비하기

        return kakaoPayService.payReady(dto, email);
    }

    @GetMapping("/pay/approve")
    public ResponseEntity payApprove(@RequestParam("pg_token") String pgToken,
                                     @RequestParam("partner_order_id") String orderId) {
        // 카카오 결제 요청하기

        return kakaoPayService.payApprove(orderId, pgToken);
    }

    @GetMapping("/pay/cancel")
    public ResponseEntity payCancel(@RequestParam("partner_order_id") String orderId){
        // 사용자가 결제창을 나갔을 때

        return kakaoPayService.payCancel(orderId);
    }
    */
}
