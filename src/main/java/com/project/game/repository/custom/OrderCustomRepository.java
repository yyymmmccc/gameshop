package com.project.game.repository.custom;

import com.project.game.dto.response.order.OrderDetailListResponseDto;
import com.project.game.dto.response.order.OrderListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.UserEntity;

import java.util.List;

public interface OrderCustomRepository {
    List<OrderListResponseDto> findAllByUserEntity(UserEntity userEntity);
}
