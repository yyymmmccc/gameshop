package com.project.game.repository.custom;

import com.project.game.dto.response.order.*;
import com.project.game.entity.UserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.game.entity.QGameImageEntity.gameImageEntity;
import static com.project.game.entity.QOrderDetailEntity.orderDetailEntity;
import static com.project.game.entity.QOrdersEntity.ordersEntity;

@RequiredArgsConstructor
@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderListResponseDto> findAllByUserEntity(UserEntity userEntity) {
        JPQLQuery<OrderListResponseDto> query =
        jpaQueryFactory.select(Projections.bean(OrderListResponseDto.class,
                        ordersEntity.orderId,
                        ordersEntity.orderStatus,
                        ordersEntity.orderDate,
                        Projections.list(
                                Projections.bean(OrderProductListResponseDto.class,
                                        orderDetailEntity.gameEntity.gameId.as("gameId"),
                                        gameImageEntity.gameImageUrl.as("gameImage"),
                                        orderDetailEntity.gameEntity.gameName.as("gameName"),
                                        orderDetailEntity.price.as("price"),
                                        orderDetailEntity.orderReview.as("reviewStatus")
                                ).as("list")
                        )
                ))
                        .from(ordersEntity)
                        .join(orderDetailEntity)
                        .on(ordersEntity.orderId.eq(orderDetailEntity.ordersEntity.orderId)
                                .and(ordersEntity.userEntity.eq(userEntity)))
                        .leftJoin(gameImageEntity)
                        .on(orderDetailEntity.gameEntity.gameId.eq(gameImageEntity.gameEntity.gameId)
                                .and(gameImageEntity.thumbnail.eq("Y")))
                        .orderBy(ordersEntity.orderDate.desc());

        List<OrderListResponseDto> result = query.fetch();

        return result;
    }
}
