package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerCartApi;
import com.project.game.dto.request.cart.CartDeleteRequestDto;
import com.project.game.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "장바구니", description = "게임 상품 장바구니에 생성, 조회, 삭제 API")
public class CartController implements SwaggerCartApi {

    private final CartService cartService;

    @PostMapping("/{gameId}")
    public ResponseEntity postCart(@PathVariable("gameId") int gameId,
                                   @AuthenticationPrincipal String email) {

        return cartService.postCart(gameId, email);
    }

    @GetMapping("/list")
    public ResponseEntity getCarts(@AuthenticationPrincipal String email){

        return cartService.getCarts(email);
    }

    @DeleteMapping("")
    public ResponseEntity deleteCart(@RequestBody @Valid CartDeleteRequestDto dto,
                                     @AuthenticationPrincipal String email){

        return cartService.deleteCart(dto, email);
    }

}
