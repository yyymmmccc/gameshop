package com.project.game.dto.request.order;

import com.project.game.entity.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayReadyRequestDto {

    private List<Integer> cartIdList;
    private List<Integer> priceList;

    public static OrdersEntity toOrderEntity(String orderId, String tid, UserEntity userEntity) {
        return com.project.game.entity.OrdersEntity.builder()
                .orderId(orderId)
                .tid(tid)
                .userEntity(userEntity)
                .state("결제중")
                .build();
    }

    public static OrderDetailEntity toOrderDetailEntity(OrdersEntity ordersEntity, CartEntity cartEntity, Integer price){
        return OrderDetailEntity.builder()
                .ordersEntity(ordersEntity)
                .gameEntity(cartEntity.getGameEntity())
                .price(price)
                .state("환불가능")
                .build();
    }

    public static List<OrderDetailEntity> convertToEntityList(OrdersEntity ordersEntity, List<CartEntity> cartEntityList, List<Integer> priceList) {
        return IntStream.range(0, cartEntityList.size())
                .mapToObj(i -> toOrderDetailEntity(ordersEntity, cartEntityList.get(i), priceList.get(i)))
                .collect(Collectors.toList());
    }
}
