package com.project.game.service;

import com.project.game.dto.request.order.OrderFormRequestDto;
import com.project.game.dto.request.order.OrderRequestDto;
import org.springframework.http.ResponseEntity;

public interface OrdersService {
    ResponseEntity getOrderFormProduct(OrderFormRequestDto dto, String email);
    ResponseEntity getOrderList(String email);
    ResponseEntity getOrderDetailList(String email, String orderId);

    ResponseEntity createOrder(OrderRequestDto dto);
}
