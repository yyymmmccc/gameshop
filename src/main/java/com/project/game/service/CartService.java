package com.project.game.service;

import com.project.game.dto.request.cart.CartDeleteRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    ResponseEntity postCart(int gameId, String email);
    ResponseEntity getCarts(String email);
    ResponseEntity deleteCart(CartDeleteRequestDto dto, String email);
}
