package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerCartApi;
import com.project.game.dto.request.cart.CartDeleteRequestDto;
import com.project.game.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController implements SwaggerCartApi {

    private final CartService cartService;

    @PostMapping("/{gameId}")
    public ResponseEntity postCart(@PathVariable("gameId") int gameId,
                                   Principal principal) {

        return cartService.postCart(gameId, principal.getName());
    }

    @GetMapping("/list")
    public ResponseEntity getCarts(Principal principal){

        return cartService.getCarts(principal.getName());
    }

    @DeleteMapping("")
    public ResponseEntity deleteCart(@RequestBody @Valid CartDeleteRequestDto dto,
                                     Principal principal){

        return cartService.deleteCart(dto, principal.getName());
    }

}
