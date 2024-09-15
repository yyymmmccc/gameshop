package com.project.game.dto.response.payment.admin;

import com.project.game.entity.OrdersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCancelReqListResponseDto {

    private String orderId;
    private String email;
    private int totalAmount;
    private String status;
    private String orderDate;

    public static PaymentCancelReqListResponseDto of(OrdersEntity ordersEntity){
        return PaymentCancelReqListResponseDto.builder()
                .orderId(ordersEntity.getOrderId())
                .email(ordersEntity.getUserEntity().getEmail())
                .totalAmount(ordersEntity.getTotalAmount())
                .status(ordersEntity.getOrderStatus())
                .orderDate(ordersEntity.getOrderDate())
                .build();
    }
}
