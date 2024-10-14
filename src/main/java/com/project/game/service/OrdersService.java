package com.project.game.service;

import com.project.game.dto.request.order.OrderFormRequestDto;
import com.project.game.dto.request.order.OrderRequestDto;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface OrdersService {
    ResponseEntity getOrderFormProduct(OrderFormRequestDto dto, String email);
    ResponseEntity getOrderList(String email);
    ResponseEntity getOrderDetailList(String email, String orderId);
    ResponseEntity createOrder(OrderRequestDto dto);
    ResponseEntity cancelOrder(String orderId);
    ResponseEntity userConfirmPurchase(String orderId, String email);
    ResponseEntity userCancelOrder(String orderId, String email) throws IamportResponseException, IOException;

    ResponseEntity<?> purchaseNow(int gameId, String email);
}
