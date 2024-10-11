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
    private int originalAmount;
    private int rewardPoints;

    public static OrderFormResponseDto of(List<OrderFormGameListResponseDto> orderList, UserEntity userEntity, int originalAmount){
        return OrderFormResponseDto.builder()
                .orderList(orderList)
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .tel(userEntity.getTel())
                .rewardPoints(userEntity.getRewardPoints())
                .originalAmount(originalAmount)
                .build();
    }
}
