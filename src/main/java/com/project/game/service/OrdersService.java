package com.project.game.service;

import com.project.game.dto.request.order.OrderFormRequestDto;
import org.springframework.http.ResponseEntity;

public interface OrdersService {
    ResponseEntity getOrderFormGames(OrderFormRequestDto dto, String email);
    ResponseEntity getOrderList(String email);
    ResponseEntity getOrderDetailList(String email, String orderId);

}
