package com.project.game.repository.custom;

import com.project.game.dto.response.cart.CartListResponseDto;
import com.project.game.dto.response.cart.QCartListResponseDto;
import com.project.game.dto.response.order.OrderFormGameListResponseDto;
import com.project.game.dto.response.order.QOrderFormGameListResponseDto;
import com.project.game.entity.UserEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QCartEntity.cartEntity;
import static com.project.game.entity.QGameEntity.gameEntity;
import static com.project.game.entity.QGameImageEntity.gameImageEntity;

@Repository
@RequiredArgsConstructor
public class CartCustomRepositoryImpl implements CartCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CartListResponseDto> findByUserEntity(UserEntity userEntity) {

        JPQLQuery<CartListResponseDto> query =
                jpaQueryFactory.select(new QCartListResponseDto(
                        cartEntity.cartId,
                        cartEntity.gameEntity.gameId,
                        cartEntity.gameEntity.gameName,
                        cartEntity.gameEntity.price,
                        gameImageEntity.gameImageUrl
                        ))
                        .from(cartEntity)
                        .leftJoin(gameImageEntity)
                        .on(cartEntity.gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .where(cartEntity.userEntity.eq(userEntity));

        List<CartListResponseDto> result = query.fetch();

        return result;
    }

    @Override
    public List<OrderFormGameListResponseDto> findByCartId(List<Integer> cartIdList) {
        JPQLQuery<OrderFormGameListResponseDto> query =
                jpaQueryFactory.select(
                                new QOrderFormGameListResponseDto(
                                        cartEntity.cartId,
                                        gameEntity.gameId,
                                        gameImageEntity.gameImageUrl,
                                        gameEntity.gameName,
                                        gameEntity.price
                                ))
                        .from(cartEntity)
                        .join(gameEntity)
                        .on(cartEntity.gameEntity.gameId.eq(gameEntity.gameId))
                        .leftJoin(gameImageEntity)
                        .on(gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .where(cartEntity.cartId.in(cartIdList));

        List<OrderFormGameListResponseDto> result = query.fetch();

        return result;
    }
}
