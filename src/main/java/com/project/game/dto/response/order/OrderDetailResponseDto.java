package com.project.game.dto.response.order;

import com.project.game.entity.OrdersEntity;
import com.project.game.entity.UserEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDto {

    private String orderDate;
    private String orderId;
    private String name;
    private String tel;
    private int totalPrice;
    private List<OrderDetailListResponseDto> orderList;

    public static OrderDetailResponseDto of(OrdersEntity ordersEntity,
                                            UserEntity userEntity,
                                            List <OrderDetailListResponseDto> orderDetailListResponseDto,
                                            int totalPrice){
        return OrderDetailResponseDto.builder()
                .orderDate(ordersEntity.getOrderDate())
                .orderId(ordersEntity.getOrderId())
                .name(userEntity.getName())
                .tel(userEntity.getTel())
                .totalPrice(totalPrice)
                .orderList(orderDetailListResponseDto)
                .build();
    }
}
