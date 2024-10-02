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

import java.security.Principal;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
@Slf4j
public class OrdersController implements SwaggerOrdersApi {

    private final OrdersService ordersService;
    //private final KakaoPayService kakaoPayService;

    // 장바구니 -> 주문페이지 넘어갈 때, 주문 페이지에서 보여줄 정보
    @PostMapping("/form")
    public ResponseEntity getOrderFormGames(@RequestBody @Valid OrderFormRequestDto dto,
                                            Principal principal){

        return ordersService.getOrderFormProduct(dto, principal.getName());
    }

    // 결제 버튼 눌렀을 때
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequestDto dto){

        return ordersService.createOrder(dto);
    }

    // 결제 Api 닫기 버튼 눌렀을 때 -> 자동 주문취소
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") String orderId){

        return ordersService.cancelOrder(orderId);
    }

    @PatchMapping("/user-cancel/{orderId}")
    public ResponseEntity<?> userCancelOrder(@PathVariable("orderId") String orderId,
                                             Principal principal){

        return ordersService.userCancelOrder(orderId, principal.getName());
    }

    @GetMapping("/list")
    public ResponseEntity getOrderList(Principal principal){

        return ordersService.getOrderList(principal.getName());
    }

    @GetMapping("/order-detail/{orderId}")
    public ResponseEntity getOrderDetailList(Principal principal,
                                             @PathVariable("orderId") String orderId){

        return ordersService.getOrderDetailList(principal.getName(), orderId);

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
