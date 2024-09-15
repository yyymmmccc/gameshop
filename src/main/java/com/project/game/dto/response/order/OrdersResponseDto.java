package com.project.game.dto.response.order;

import com.project.game.entity.OrdersEntity;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersResponseDto {

    private String orderId;
    private String status;
    private String orderDate;

    public static OrdersResponseDto of(OrdersEntity ordersEntity){
        return OrdersResponseDto.builder()
                .orderId(ordersEntity.getOrderId())
                .status(ordersEntity.getOrderStatus())
                .orderDate(ordersEntity.getOrderDate())
                .build();
    }
}
