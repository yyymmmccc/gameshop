package com.project.game.repository.custom;

import com.project.game.dto.response.cart.CartListResponseDto;
import com.project.game.dto.response.order.OrderFormGameListResponseDto;
import com.project.game.entity.UserEntity;

import java.util.List;

public interface CartCustomRepository {
    List<CartListResponseDto> findByUserEntity(UserEntity userEntity);

    List<OrderFormGameListResponseDto> findByCartId(List<Integer> cartId);
}
