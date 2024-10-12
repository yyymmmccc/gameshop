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

    public static OrdersResponseDto of(String orderId){
        return OrdersResponseDto.builder()
                .orderId(orderId)
                .build();
    }
}
