package com.project.game.dto.request.order;

import com.project.game.entity.*;
import com.project.game.global.code.OrderType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    private List<Integer> cartIdList;
    private List<Integer> gamePriceList;
    private int totalAmount;
    private String email;

    public OrdersEntity toEntity(UserEntity userEntity, String orderId){
        return OrdersEntity.builder()
                .orderId(orderId)
                .userEntity(userEntity)
                .totalAmount(totalAmount)
                .orderStatus(String.valueOf(OrderType.ORDERING))
                .build();
    }

    public static OrderDetailEntity toOrderDetailEntity(OrdersEntity ordersEntity, CartEntity cartEntity, Integer price){
        return OrderDetailEntity.builder()
                .ordersEntity(ordersEntity)
                .gameEntity(cartEntity.getGameEntity())
                .price(price)
                .build();
    }

    public static List<OrderDetailEntity> convertToEntityList(OrdersEntity ordersEntity, List<CartEntity> cartEntityList, List<Integer> priceList) {
        return IntStream.range(0, cartEntityList.size())
                .mapToObj(i -> toOrderDetailEntity(ordersEntity, cartEntityList.get(i), priceList.get(i)))
                .collect(Collectors.toList());
    }
}
