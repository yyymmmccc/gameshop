package com.project.game.dto.response.order;

import com.project.game.entity.UserEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFormResponseDto {

    private List<OrderFormGameListResponseDto> orderList;
    private String email;
    private String name;
    private String tel;
    private int totalPrice;

    public static OrderFormResponseDto of(List<OrderFormGameListResponseDto> orderList, UserEntity userEntity, int totalPrice){
        return OrderFormResponseDto.builder()
                .orderList(orderList)
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .tel(userEntity.getTel())
                .totalPrice(totalPrice)
                .build();
    }
}
