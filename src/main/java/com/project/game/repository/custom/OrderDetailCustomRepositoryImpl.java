package com.project.game.repository.custom;

import com.project.game.dto.response.order.OrderDetailListResponseDto;
import com.project.game.dto.response.order.QOrderDetailListResponseDto;
import com.project.game.entity.OrdersEntity;
import com.project.game.entity.QOrdersEntity;
import com.project.game.entity.UserEntity;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QOrderDetailEntity.orderDetailEntity;

@Repository
@RequiredArgsConstructor
public class OrderDetailCustomRepositoryImpl implements OrderDetailCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderDetailListResponseDto> findAllByOrderEntityAndUserEntity(OrdersEntity ordersEntity, UserEntity userEntity) {
        JPQLQuery<OrderDetailListResponseDto> query =
                jpaQueryFactory.select(new QOrderDetailListResponseDto(
                        orderDetailEntity.gameEntity.gameId,
                        gameImageEntity.gameImageUrl,
                        orderDetailEntity.gameEntity.gameName,
                        orderDetailEntity.price,
                        orderDetailEntity.state
                ))
                        .from(orderDetailEntity)
                        .leftJoin(gameImageEntity)
                        .on(orderDetailEntity.gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .where(orderDetailEntity.ordersEntity.eq(ordersEntity))
                        .where(orderDetailEntity.ordersEntity.userEntity.eq(userEntity));

                List<OrderDetailListResponseDto> result = query.fetch();

        return result;
    }
}
